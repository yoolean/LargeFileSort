package com.nokia.oss.hadoop;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * Created by harchen on 9/14/2015.
 */
public class SAUReducer
    extends Reducer<Text, Text, Text, Text>
{
    @Override
    protected void reduce( Text key, Iterable<Text> values, Context context ) throws IOException, InterruptedException
    {
        context.write( key, values.iterator().next() );
    }
}
