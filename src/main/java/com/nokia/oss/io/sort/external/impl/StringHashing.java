package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.Hashing;

/**
 * Created by harchen on 9/11/2015.
 */
public class StringHashing
    implements Hashing<String>
{
    @Override
    public int hash( String string )
    {
        if( string == null )
        {
            return 0;
        }
        return string.hashCode();
    }

}
