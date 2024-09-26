package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Mapper2 extends Mapper<LongWritable, Text, LongWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString().replaceAll("\\[","").replaceAll("\\]","");
        String[] events = line.split(", ");
        String new_line = "";
        String sid="";
        for (String event : events) {
            String ppid = event.split(" ")[2];
            String pi = event.split(" ")[3];
            new_line += "(" + ppid +"," + pi + ");";
            sid = event.split(" ")[1];
        }
        context.write(new LongWritable(Long.parseLong(sid)), new Text(new_line));
    }
}


