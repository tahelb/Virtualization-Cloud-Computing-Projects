package purchase_count;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Mapper1 extends Mapper<LongWritable, Text, LongWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString().replaceAll(",", " ");
        String sid = line.split(" ")[1];
        context.write(new LongWritable(Long.parseLong(sid)), new Text(line));
    }
}
