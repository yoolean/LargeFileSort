package com.nokia.oss.io.sort.external.impl;

import java.util.Comparator;


/**
 * Created by harchen on 9/11/2015.
 */
public class StringComparator
    implements Comparator<String>
{
    @Override
    public int compare( String first, String second )
    {
        if( first == null )
        {
            return -1;
        }
        return first.compareTo( second );
    }
}
