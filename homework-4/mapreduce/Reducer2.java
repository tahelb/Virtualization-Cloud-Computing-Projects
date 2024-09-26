package purchase_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reducer2 extends Reducer<LongWritable, Text, LongWritable, Text> {

    @Override
    public void reduce(LongWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        for (Text value : values) {
            String line = value.toString();
            String[] visits = line.split(";");
            String triplet ="";
            if (visits.length < 3){
                int a = visits.length == 1 ? 0 : 1;
                triplet = String.join(" ",visits[0],visits[a],visits[a]).replaceAll(",1",",0");
                context.write(key, new Text(triplet));
            }

            for(int i = 0; i <= visits.length-3; i++){
                triplet = String.join(" ",visits[i],visits[i+1],visits[i+2]);
                context.write(key, new Text(triplet));
            }


        }
    }
}

