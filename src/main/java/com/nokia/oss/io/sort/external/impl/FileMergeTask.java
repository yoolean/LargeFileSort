package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.FileMerger;

import java.io.File;
import java.util.concurrent.Callable;


/**
 * Created by harchen on 9/11/2015.
 */
public class FileMergeTask
    implements Callable<File>
{
    private File firstFile;
    private File secondFile;
    private FileMerger merger;


    public FileMergeTask( FileMerger merger,File firstFile,  File secondFile )
    {
        this.firstFile = firstFile;
        this.merger = merger;
        this.secondFile = secondFile;
    }


    @Override
    public File call() throws Exception
    {
        return merger.merge( firstFile, secondFile );
    }
}
