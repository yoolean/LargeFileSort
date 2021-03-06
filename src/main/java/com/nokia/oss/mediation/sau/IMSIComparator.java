package com.nokia.oss.mediation.sau;

import java.util.Comparator;


/**
 * Created by harchen on 9/11/2015.
 */
public class IMSIComparator
    implements Comparator<String>
{
    @Override
    public int compare( String first, String second )
    {
        if( first == null )
        {
            return -1;
        }
        if( second == null )
        {
            return 1;
        }
       return SAURecord.getIMSI(first).compareTo( SAURecord.getIMSI(second) );
    }
}
