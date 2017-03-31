package edu.uprm.cse.bigdata;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AppReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        int count = 0;
        String replies = "";

        for (Text value: values) {
            replies+= value.toString() + "-";
            count++;
        }
        String value = count + "\t" + replies.substring(0, replies.length()-1);

        context.write(key, new Text(value));
    }
}
