package purchase_count;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Random;

public class Main extends Configured implements Tool {

    // ------------- DRIVER ------------
    @Override
    public int run(String[] args) throws Exception {
        Path tempDir1 = new Path("data/temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        Path tempDir2 = new Path("data/temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        Path tempDir3 = new Path("data/temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));

        Configuration conf = getConf();
        //FileSystem.get(conf).delete(new Path("data/output"), true);
        FileSystem.get(conf).delete(new Path(args[1]), true);

        try {
            // ----------- Stage 1 -----------
            System.out.println("-------Stage 1-------");
            Job stage_one = Job.getInstance(conf);
            stage_one.setJobName("stage_one");
            stage_one.setJarByClass(Main.class);

            stage_one.setMapOutputValueClass(Text.class);
            stage_one.setOutputKeyClass(LongWritable.class);
            stage_one.setOutputValueClass(Text.class);

            stage_one.setMapperClass(Mapper1.class);
            stage_one.setReducerClass(Reducer1.class);

            Path inputFilePath = new Path(args[0]);
            //Path inputFilePath = new Path("data/input");

            FileInputFormat.addInputPath(stage_one, inputFilePath);
            FileOutputFormat.setOutputPath(stage_one, tempDir1);

            if (!stage_one.waitForCompletion(true)) {
                return 1;
            }

            // ----------- Stage 2-----------
            System.out.println("-------Stage 2-------");
            conf = new Configuration();
            Job stage_two = Job.getInstance(conf);
            stage_two.setJobName("stage_two");

            stage_two.setJarByClass(Main.class);
            FileInputFormat.setInputPaths(stage_two, tempDir1);
            FileOutputFormat.setOutputPath(stage_two, tempDir2);

            stage_two.setMapOutputValueClass(Text.class);
            stage_two.setOutputKeyClass(LongWritable.class);
            stage_two.setOutputValueClass(Text.class);

            stage_two.setMapperClass(Mapper2.class);
            stage_two.setReducerClass(Reducer2.class);

            if (!stage_two.waitForCompletion(true)) {
                return 1;
            }

            // ----------- Stage 3 -----------
            System.out.println("-------Stage 3-------");
            conf = new Configuration();
            Job stage_three = Job.getInstance(conf);
            stage_three.setJobName("stage_three");

            stage_three.setJarByClass(Main.class);
            FileInputFormat.setInputPaths(stage_three, tempDir2);
            FileOutputFormat.setOutputPath(stage_three, tempDir3);

            stage_three.setMapOutputValueClass(IntWritable.class);
            stage_three.setOutputKeyClass(Text.class);
            stage_three.setOutputValueClass(LongWritable.class);

            stage_three.setMapperClass(Mapper3.class);
            stage_three.setReducerClass(Reducer3.class);
            if (!stage_three.waitForCompletion(true)) {
                return 1;
            }

            // ----------- Stage 4 -----------
            System.out.println("-------Stage 4-------");
            conf = new Configuration();
            Job stage_four = Job.getInstance(conf);
            stage_four.setJobName("stage_four");

            stage_four.setJarByClass(Main.class);
            FileInputFormat.setInputPaths(stage_four, tempDir3);

            //FileOutputFormat.setOutputPath(stage_four, new Path("data/output"));
            FileOutputFormat.setOutputPath(stage_four, new Path(args[1]));

            stage_four.setMapOutputValueClass(Text.class);
            stage_four.setOutputKeyClass(LongWritable.class);
            stage_four.setOutputValueClass(LongWritable.class);

            stage_four.setMapperClass(Mapper4.class);
            stage_four.setReducerClass(Reducer4.class);

            return stage_four.waitForCompletion(true) ? 0 : 1;
        } finally {
            FileSystem.get(conf).delete(tempDir1, true);
            FileSystem.get(conf).delete(tempDir2, true);
            FileSystem.get(conf).delete(tempDir3, true);
        }
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Main(), args);
        System.exit(exitCode);
    }
}
