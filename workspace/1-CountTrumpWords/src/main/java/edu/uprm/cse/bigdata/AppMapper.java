package edu.uprm.cse.bigdata;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    public static final String[] TRUMP_WORDS = new String[] {"trump", "maga", "dictator", "impeach", "drain", "swamp", "change"};
    public static final Set<String> TRUMP_SET = new HashSet<>(Arrays.asList(TRUMP_WORDS));

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String rawTweet = value.toString();

        try {
            Status tweetStatus = TwitterObjectFactory.createStatus(rawTweet);
            // Remove URL's, emojis and punctuation marks .,;:"'<>?/~`!@#$%^&*()_-=+[]{}! (leave only alphanumeric characters)
            String parsed_text = tweetStatus.getText().replaceAll("http.*?(\\s|$)|\\P{Print}|[^A-Za-z]+", " ").trim();
            String[] words = parsed_text.toLowerCase().split("\\s+");
            for (String word: words) {
                if(TRUMP_SET.contains(word))
                    context.write(new Text(word), new IntWritable(1));
            }
        }
        catch(TwitterException e){
            // Ignore bad tweets
            Logger logger = LogManager.getRootLogger();
            logger.trace("Bad Tweet: " + rawTweet);
        }

    }

}
