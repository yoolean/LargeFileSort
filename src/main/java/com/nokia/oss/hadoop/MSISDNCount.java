package com.nokia.oss.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * Created by harchen on 9/14/2015.
 */
public class MSISDNCount
{

    public static void main( String[] args )
    {
        try
        {
            long start=System.currentTimeMillis();
            Configuration conf = new Configuration();
            Job job = new Job( conf, "SAU MSISDN count" );
            job.setJarByClass( MSISDNCount.class );
            job.setMapperClass(MSISDNMapper.class);
            job.setReducerClass(SAUReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.waitForCompletion(true);
            System.out.println("Time costs:" + (System.currentTimeMillis() - start) + " ms");
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
