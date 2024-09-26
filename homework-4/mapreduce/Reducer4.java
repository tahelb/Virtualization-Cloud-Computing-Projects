package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

import java.util.TreeSet;

public class Reducer4 extends Reducer<LongWritable, Text, Text, LongWritable> {

    @Override
    public void reduce(LongWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        TreeSet<String> ts = new TreeSet<String>();
        for (Text value: values ){
            ts.add(value.toString());
        }

        for (String value: ts){
            long count = -Long.parseLong(key.toString());
            String new_key = value.split("\\|")[1];
            context.write(new Text(new_key), new LongWritable(count));
        }


    }
}

