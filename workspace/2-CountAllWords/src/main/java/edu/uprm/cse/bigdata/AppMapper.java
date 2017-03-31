package edu.uprm.cse.bigdata;

import com.vdurmont.emoji.EmojiParser;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // Source: http://www.ranks.nl/stopwords
    public static final String[] STOP_WORDS = new String[] {"a", "an", "and", "are", "as", "at", "be", "by", "for",
    "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with"};
    public static final Set<String> STOP_SET = new HashSet<>(Arrays.asList(STOP_WORDS));

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String rawTweet = value.toString();

        try {
            Status tweetStatus = TwitterObjectFactory.createStatus(rawTweet);
            // Remove Emojis
            String text = EmojiParser.removeAllEmojis(tweetStatus.getText());
            // Remove the following expressions
            String email = "[\\w\\d.%+-]+@[\\w\\d.-]+\\.[\\w]{2,}";
            String url_mention = "(http|www\\.|@).*?(\\s|$)";
            String non_ascii_word = "\\w*[^\\x00-\\x7F]+\\w*";
            String word_suffix_nt = "\\w+n't\\b"; // (...n't) All should belong in the stopwords set anyway
            // Concat regex
            String remove_regex = url_mention + "|" + email + "|" + non_ascii_word + "|" + word_suffix_nt;
            // Remove regex matches
            String text_parsed = text.replaceAll(remove_regex, "").trim();
            // Search for words
            String keyword = "[A-Za-z]+";
            Pattern checkRegex = Pattern.compile(keyword);
            Matcher regexMatcher = checkRegex.matcher(text_parsed);
            // Write if it's length > 2 and is not a stop word
            while (regexMatcher.find()) {
              String word = regexMatcher.group().toLowerCase();
              if(word.length() > 2 && !STOP_SET.contains(word))
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
