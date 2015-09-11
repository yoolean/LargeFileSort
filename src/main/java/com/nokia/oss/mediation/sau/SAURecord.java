package com.nokia.oss.mediation.sau;

/**
 * Created by harchen on 9/11/2015.
 */
public class SAURecord
{
    public static String getIMSI( String string )
    {
        String[] segments = string.split( "\\s+" );
        if( segments.length <= 1 )
        {
            return string;
        }
        return segments[0];
    }


    public static String getMSISDN( String string )
    {
        String[] segments = string.split( "\\s+" );
        if( segments.length <= 1 )
        {
            return string;
        }
        return segments[1];
    }
}
