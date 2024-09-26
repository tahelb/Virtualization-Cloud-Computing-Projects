package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class Mapper3 extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String[] items = line.split(" ");
        String indicator = items[2].split(",")[1].replaceAll("\\)","");
        if(indicator.equals("1")){
            String pair = items[0].split("\t")[1] + "," + items[1];
            context.write(new Text(pair), new IntWritable(1));
        }
    }
}


