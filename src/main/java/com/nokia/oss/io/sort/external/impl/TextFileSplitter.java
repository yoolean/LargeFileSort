package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.FileSplitter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by harchen on 9/10/2015.
 */
public class TextFileSplitter
    implements FileSplitter
{
    private static final Logger LOG = LoggerFactory.getLogger( TextFileSplitter.class );
    private int unitFileSize;


    public TextFileSplitter( int unitFileSize )
    {
        this.unitFileSize = unitFileSize;
    }


    @Override
    public List<File> split( File source ) throws IOException
    {
        List<File> files = new LinkedList<>();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try
        {
            int currentFileSize = 0;
            int fileCount = 0;
            bufferedReader = new BufferedReader( new FileReader( source ) );
            File outputFile = new File( source.getParentFile(), source.getName() + "_" + fileCount );
            files.add( outputFile );
            bufferedWriter = new BufferedWriter( new FileWriter( outputFile ) );
            LOG.info( "split into " + outputFile );
            String line = null;
            while( (line = bufferedReader.readLine()) != null )
            {
                currentFileSize = currentFileSize + line.length() * 2;
                if( currentFileSize > unitFileSize )
                {
                    currentFileSize = 0;
                    fileCount++;
                    bufferedWriter.close();
                    outputFile = new File( source.getParentFile(), source.getName() + "_" + fileCount );
                    LOG.info( "split into " + outputFile );
                    files.add( outputFile );
                    bufferedWriter = new BufferedWriter( new FileWriter( outputFile ) );
                }
                bufferedWriter.write( line );
                bufferedWriter.newLine();
            }

        }
        finally
        {
            IOUtils.closeQuietly( bufferedReader );
            IOUtils.closeQuietly( bufferedWriter );
        }
        return files;
    }
}
