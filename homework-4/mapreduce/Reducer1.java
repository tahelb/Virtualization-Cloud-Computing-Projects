package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

import java.util.TreeSet;

public class Reducer1 extends Reducer<LongWritable, Text, LongWritable, Text> {

    @Override
    public void reduce(LongWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        TreeSet<String> ts = new TreeSet<String>();

        for (Text value : values) {
            ts.add(value.toString());
        }
        context.write(key, new Text(ts.toString()));
    }
}

