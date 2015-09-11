package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.FileSorter;

import java.io.File;
import java.util.concurrent.Callable;


/**
 * Created by harchen on 9/10/2015.
 */
public class FileSortTask
    implements Callable<File>
{
    private FileSorter sorter;
    private File source;
    private File target;


    public FileSortTask( FileSorter sorter, File source, File target )
    {
        this.sorter = sorter;
        this.source = source;
        this.target = target;
    }


    @Override
    public File call() throws Exception
    {
        sorter.sort( source, target );
        return target;
    }
}
