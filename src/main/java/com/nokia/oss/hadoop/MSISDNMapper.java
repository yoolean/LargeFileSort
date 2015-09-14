package com.nokia.oss.hadoop;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * Created by harchen on 9/14/2015.
 */
public class MSISDNMapper
    extends Mapper<Object, Text, Text, Text>
{
    @Override
    protected void map( Object key, Text value, Context context ) throws IOException, InterruptedException
    {
        String imsi = null, msisdn = null;
        String stringValue = value.toString().trim();
        int index = stringValue.indexOf( '\t' );
        if( index > 0 )
        {
            imsi = stringValue.substring( 0, index );
            msisdn = stringValue.substring( index + 1 );
        }
        else
        {
            msisdn = stringValue;
        }
        context.write( new Text( msisdn ), new Text( imsi ) );
    }
}
