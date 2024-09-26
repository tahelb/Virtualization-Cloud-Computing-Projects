package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Mapper4 extends Mapper<LongWritable, Text, LongWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String pair = line.split("\t")[0];
        String ppid1 = pair.split(",")[0].replaceAll("\\(","");
        String ppid2 = pair.split(",")[2].replaceAll("\\(","");
        String tie_break = String.join(",", ppid1, ppid2);
        String new_val= String.join("|", tie_break,pair);
        long count = -Long.parseLong(line.split("\t")[1]);

        context.write(new LongWritable(count), new Text(new_val));
    }
}


