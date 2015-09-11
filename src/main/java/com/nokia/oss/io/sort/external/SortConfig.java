package com.nokia.oss.io.sort.external;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by harchen on 9/11/2015.
 */
public class SortConfig
{
    private Comparator<String> comparator;
    private Hashing<String> hashing;
    private int memoryFootprint = 10 * 1024 * 1024;
    private int concurrencyDegree = 1;
    private ExecutorService executor;
    private boolean removeDuplication = false;


    public void validate()
    {
        checkNotNull( comparator, "The comparator can not be null" );
        checkNotNull( hashing, "Hashing should be provided" );
        checkArgument( memoryFootprint > 0, "Memory footprint should be greater than 0" );
        checkArgument( concurrencyDegree > 0, "Invalid concurrency degree,should be greater than 0" );
        if( concurrencyDegree > 1 )
        {
            checkNotNull( executor, "ExecutorService must be specified if concurrency degree is greater than 1" );
        }
    }


    public Comparator<String> getComparator()
    {
        return comparator;
    }


    public void setComparator( Comparator<String> comparator )
    {
        this.comparator = comparator;
    }


    public int getConcurrencyDegree()
    {
        return concurrencyDegree;
    }


    public void setConcurrencyDegree( int concurrencyDegree )
    {
        this.concurrencyDegree = concurrencyDegree;
    }


    public ExecutorService getExecutor()
    {
        return executor;
    }


    public void setExecutor( ExecutorService executor )
    {
        this.executor = executor;
    }


    public Hashing<String> getHashing()
    {
        return hashing;
    }


    public void setHashing( Hashing<String> hashing )
    {
        this.hashing = hashing;
    }


    public int getMemoryFootprint()
    {
        return memoryFootprint;
    }


    public void setMemoryFootprint( int memoryFootprint )
    {
        this.memoryFootprint = memoryFootprint;
    }


    public boolean isRemoveDuplication()
    {
        return removeDuplication;
    }


    public void setRemoveDuplication( boolean removeDuplication )
    {
        this.removeDuplication = removeDuplication;
    }
}
