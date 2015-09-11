package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.Hashing;

import java.util.Comparator;


/**
 * Created by harchen on 9/11/2015.
 */
public class LineRecord
    implements Comparable
{
    private String line;
    private Comparator<String> comparator;
    private Hashing<String> hashing;


    public LineRecord( Comparator<String> comparator, Hashing<String> hashing, String line )
    {
        this.comparator = comparator;
        this.hashing = hashing;
        this.line = line;
    }


    public Comparator<String> getComparator()
    {
        return comparator;
    }


    public void setComparator( Comparator<String> comparator )
    {
        this.comparator = comparator;
    }


    public Hashing<String> getHashing()
    {
        return hashing;
    }


    public void setHashing( Hashing<String> hashing )
    {
        this.hashing = hashing;
    }


    public String getLine()
    {
        return line;
    }


    public void setLine( String line )
    {
        this.line = line;
    }


    @Override
    public int compareTo( Object that )
    {
        if( !(that instanceof LineRecord) )
        {
            return -1;
        }
        LineRecord lineRecord = (LineRecord)that;
        return comparator.compare( this.line, lineRecord.getLine() );
    }


    @Override
    public boolean equals( Object o )
    {
        if( this == o )
            return true;
        if( !(o instanceof LineRecord) )
            return false;
        LineRecord that = (LineRecord)o;
        return comparator.compare( this.getLine(), that.getLine() ) == 0;
    }


    @Override
    public int hashCode()
    {
        return hashing.hash( this.getLine() );
    }
}
