#!/bin/bash

# Declare an array of program names
declare -a arr=("1-CountTrumpWords"
                "2-CountAllWords"
                "3-FindUniqueScreenNames"
                "4-FindRetweets4EachMessage"
                "5-FindReplies4EachMessage"
                "6-CountMessagesPostedByEachUser"
                )

# Go to workspace directory
cd workspace
# Decompress tweet data
unzip -o trump_tweets.zip
# Create input directory in hdfs
hdfs dfs -mkdir /input
# Create output directory in hdfs
hdfs dfs -mkdir /output
# Put tweet data in hdfs
hdfs dfs -put trump_tweets.txt /input

# Loop through all the programs and run them
for i in "${!arr[@]}"
do
  #  Go to program directory
  cd "${arr[$i]}"
  # Build JAR
  mvn package
  # Run program
  hadoop jar "target/${arr[$i]}-1.0-SNAPSHOT.jar" /input "/output/$((i+1))"
  # Remove previous data if any
  rm -f "../web/$((i+1))/data.tsv" "../web/$((i+1))/data.min.tsv"
  # Extract output data from hdfs to local
  hdfs dfs -get "/output/$((i+1))/part-r-00000" "../web/$((i+1))/data.tsv"
  # Sort data by 2nd column
  sort -nrk 2 -o "../web/$((i+1))/data.tsv" "../web/$((i+1))/data.tsv"
  # Extract top 20 and put it in another file
  head -20 "../web/$((i+1))/data.tsv" > "../web/$((i+1))/data.min.tsv"
  # Exit program directory
  cd ..
done

echo -e "Finish. \e[32mAll Programs have been executed. You can view the data at: http://localhost:8080"
