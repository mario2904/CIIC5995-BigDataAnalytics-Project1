package edu.uprm.cse.bigdata;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.io.IOException;

public class AppMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String rawTweet = value.toString();

        try {
            Status tweetStatus = TwitterObjectFactory.createStatus(rawTweet);
            if(tweetStatus.isRetweet()) {
                Status originalTweetStatus = tweetStatus.getRetweetedStatus();
                String originalStatusId = String.valueOf(originalTweetStatus.getId());
                String retweetStatusId = String.valueOf(tweetStatus.getId());
                context.write(new Text(originalStatusId), new Text(retweetStatusId));
            }
        }
        catch(TwitterException e){
            // ignore bad tweets
            Logger logger = LogManager.getRootLogger();
            logger.trace("Bad Tweet: " + rawTweet);
        }

    }

}
