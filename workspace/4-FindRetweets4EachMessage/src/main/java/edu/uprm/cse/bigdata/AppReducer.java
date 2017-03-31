package edu.uprm.cse.bigdata;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AppReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        int count = 0;
        String retweets = "";

        for (Text value: values) {
            retweets+= value.toString() + "-";
            count++;
        }
        String value = count + "\t" + retweets.substring(0, retweets.length()-1);

        context.write(key, new Text(value));
    }
}
