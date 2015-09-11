package com.nokia.oss.io.sort.external.impl;

import com.nokia.oss.io.sort.external.FileSorter;
import com.nokia.oss.io.sort.external.Hashing;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Created by harchen on 9/10/2015.
 */
public class TextFileSorter
    implements FileSorter
{
    private static  final Logger LOG= LoggerFactory.getLogger(TextFileSorter.class);
    private Hashing<String> hashing;
    private Comparator<String> comparator;
    private boolean removeDuplication;


    public TextFileSorter()
    {
        this.comparator = new StringComparator();
        this.hashing = new StringHashing();
        this.removeDuplication = false;
    }


    public TextFileSorter( Comparator<String> comparator, Hashing<String> hashing, boolean removeDuplication )
    {
        this.comparator = comparator;
        this.hashing = hashing;
        this.removeDuplication = removeDuplication;
    }


    @Override
    public void sort( File source, File target ) throws IOException
    {
        LOG.info("Sort file " + source.getAbsolutePath() + " --> " + target.getAbsolutePath());
        long start=System.currentTimeMillis();
        Collection<LineRecord> records = removeDuplication?readUniqueLineRecords(source):readLineRecords(source);
        LOG.debug("Line record found:" + records.size());
        List<LineRecord> recordList = new LinkedList<>( records );
        Collections.sort( recordList );
        LOG.debug("Line records sorted");
        writeLineRecords(recordList, target);
        LOG.debug("Write line record to " + target.getAbsolutePath());
        LOG.info("Sort file " + source.getAbsolutePath() + " --> " + target.getAbsolutePath() + " finished in " + (System.currentTimeMillis()-start)+" ms");
    }


    private void writeLineRecords( List<LineRecord> lines, File target ) throws IOException
    {
        BufferedWriter bufferedWriter = null;
        try
        {
            bufferedWriter = new BufferedWriter( new FileWriter( target ) );
            for( LineRecord record : lines )
            {
                bufferedWriter.write( record.getLine() );
                bufferedWriter.newLine();
            }
        }
        finally
        {
            IOUtils.closeQuietly( bufferedWriter );
        }
    }


    private Collection<LineRecord> readUniqueLineRecords( File file ) throws IOException
    {
        Set<LineRecord> records = new LinkedHashSet<>();
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader( new FileReader( file ) );
            String line = null;
            while( (line = bufferedReader.readLine()) != null )
            {
                LineRecord record = new LineRecord( comparator, hashing, line );
                records.add( record );
            }
        }
        finally
        {
            IOUtils.closeQuietly( bufferedReader );
        }
        return records;
    }


    private Collection<LineRecord> readLineRecords( File file ) throws IOException
    {
        List<LineRecord> records = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader( new FileReader( file ) );
            String line = null;
            while( (line = bufferedReader.readLine()) != null )
            {
                LineRecord record = new LineRecord( comparator, hashing, line );
                records.add( record );
            }
        }
        finally
        {
            IOUtils.closeQuietly( bufferedReader );
        }
        return records;
    }

}
