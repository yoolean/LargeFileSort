package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.FileMerger;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;


/**
 * Created by harchen on 9/10/2015.
 */
public class TextFileMerger
    implements FileMerger
{
    private static final Logger LOG= LoggerFactory.getLogger(TextFileSorter.class);
    private Comparator<String> comparator;
    private boolean removeDuplication;


    public TextFileMerger( Comparator<String> comparator, boolean removeDuplication )
    {
        this.comparator = comparator;
        this.removeDuplication = removeDuplication;
    }


    @Override
    public File merge( File firstFile, File secondFile ) throws IOException
    {
        LOG.info("Merge file "+firstFile.getAbsolutePath()+"+"+secondFile.getAbsolutePath());
        File merged = createMergedFile( firstFile, secondFile );
        BufferedReader firstReader = null;
        BufferedReader secondReader = null;
        PrintWriter mergedWriter = null;
        try
        {
            firstReader = new BufferedReader( new FileReader( firstFile ) );
            secondReader = new BufferedReader( new FileReader( secondFile ) );
            mergedWriter = new PrintWriter( new FileWriter( merged ) );
            String lineFromFirstReader = firstReader.readLine();
            String lineFromSecondReader = secondReader.readLine();
            while( true )
            {
                if( lineFromFirstReader == null )
                {
                    while( lineFromSecondReader != null )
                    {
                        mergedWriter.println( lineFromSecondReader );
                        lineFromSecondReader = secondReader.readLine();

                    }
                    break;
                }
                else
                {
                    if( lineFromSecondReader == null )
                    {
                        while( lineFromFirstReader != null )
                        {
                            mergedWriter.println( lineFromFirstReader );
                            lineFromFirstReader = firstReader.readLine();
                        }
                        break;
                    }
                    else
                    {
                        if( comparator.compare( lineFromFirstReader, lineFromSecondReader ) < 0 )
                        {
                            mergedWriter.println( lineFromFirstReader );
                            lineFromFirstReader = firstReader.readLine();
                        }
                        else if( comparator.compare( lineFromFirstReader, lineFromSecondReader ) == 0 )
                        {
                            if( !removeDuplication )
                            {
                                mergedWriter.println( lineFromSecondReader );
                            }
                            lineFromSecondReader = secondReader.readLine();
                        }
                        else
                        {
                            mergedWriter.println( lineFromSecondReader );
                            lineFromSecondReader = secondReader.readLine();
                        }
                    }
                }
            }
        }
        finally
        {
            IOUtils.closeQuietly( firstReader );
            IOUtils.closeQuietly( secondReader );
            IOUtils.closeQuietly( mergedWriter );
        }
        return merged;
    }


    private File createMergedFile( File firstFile, File secondFile )
    {
        String name = firstFile.getName() + secondFile.getName().substring( secondFile.getName().indexOf( '_' ) + 1 );
        return new File( firstFile.getParent(), name );
    }
}
