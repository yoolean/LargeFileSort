package com.nokia.oss.io.sort.external;

import com.nokia.oss.io.sort.external.impl.FileMergeTask;
import com.nokia.oss.io.sort.external.impl.FileSortTask;
import com.nokia.oss.io.sort.external.impl.TextFileMerger;
import com.nokia.oss.io.sort.external.impl.TextFileSorter;
import com.nokia.oss.io.sort.external.impl.TextFileSplitter;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;


/**
 * Created by harchen on 9/11/2015.
 */
public class ExternalSortEngine
{
    private static final Logger LOG = LoggerFactory.getLogger( ExternalSortEngine.class );
    private SortConfig config;
    private FileSplitter splitter;
    private FileSorter sorter;
    private FileMerger merger;


    public ExternalSortEngine( SortConfig config )
    {
        config.validate();
        this.config = config;
        splitter = new TextFileSplitter( config.getMemoryFootprint() );
        sorter = new TextFileSorter( config.getComparator(), config.getHashing(), config.isRemoveDuplication() );
        merger = new TextFileMerger( config.getComparator(), config.isRemoveDuplication() );
    }


    public void sort( File source, File target ) throws IOException, ExecutionException, InterruptedException
    {
        LOG.info( "Sorting for " + source.getAbsolutePath() + " started, target " + target.getAbsolutePath() );
        long start = System.currentTimeMillis();
        LOG.info("Split into small files ...");
        List<File> files = split( source );
        LOG.info("Sort each small file ...");
        sort( files );
        LOG.info("Merge small file ...");
        merge( target, files );
        LOG.info( "Sorting for " + source.getAbsolutePath() + " finished in " + (System.currentTimeMillis() - start) +
            " ms" );
    }


    private void merge( File target, List<File> files ) throws IOException, ExecutionException, InterruptedException
    {
        File merged = null;
        if( config.getConcurrencyDegree() > 1 )
        {
            merged = mergeInMultiThread( files );
        }
        else
        {
            merged = mergeInSingleThread( files );
        }

        FileUtils.copyFile( merged, target );
    }


    private File mergeInMultiThread( List<File> files ) throws InterruptedException, ExecutionException
    {
        Queue<File> mergeQueue = new ConcurrentLinkedQueue<>( files );
        while( mergeQueue.size() > 1 )
        {
            CompletionService<File> completionService = getCompletionService();
            int taskCount = 0;
            while( !mergeQueue.isEmpty() )
            {
                LOG.debug( "Files left for merge:" + mergeQueue.size() );
                if( mergeQueue.peek() == null )
                {
                    break;
                }
                File firstFile = mergeQueue.poll();
                if( mergeQueue.peek() == null )
                {
                    break;
                }
                File secondFile = mergeQueue.poll();
                FileMergeTask task = new FileMergeTask( merger, firstFile, secondFile );
                completionService.submit( task );
                taskCount++;
            }
            for( int i = 0; i < taskCount; i++ )
            {
                File file = completionService.take().get();
                LOG.debug( "File merged:" + file.getAbsolutePath() );
                mergeQueue.offer( file );
            }
        }
        return mergeQueue.poll();
    }


    private CompletionService getCompletionService()
    {
        return new ExecutorCompletionService( config.getExecutor() );
    }


    private File mergeInSingleThread( List<File> files ) throws IOException
    {
        List<File> mergeList = new LinkedList<>( files );
        while( mergeList.size() > 1 )
        {
            LOG.debug( "Files left for merge:" + mergeList.size() );
            File firstFile = mergeList.get( 0 );
            File secondFile = mergeList.get( 1 );
            File merged = merger.merge( firstFile, secondFile );
            mergeList.remove( firstFile );
            mergeList.remove( secondFile );
            mergeList.add( merged );
        }
        return mergeList.get( 0 );
    }


    private void sort( List<File> files ) throws IOException, ExecutionException, InterruptedException
    {
        if( config.getConcurrencyDegree() > 1 )
        {
            sortInMultiThread( files );
        }
        else
        {
            sortInSingleThread( files );
        }
    }


    private void sortInMultiThread( List<File> files ) throws InterruptedException, ExecutionException
    {
        CompletionService<File> completionService = getCompletionService();
        for( File file : files )
        {
            FileSortTask task = new FileSortTask( sorter, file, file );
            completionService.submit( task );
        }
        for( int i = 0; i < files.size(); i++ )
        {
            File file = completionService.take().get();
            LOG.debug( "Sorting finished for " + file );
        }
    }


    private void sortInSingleThread( List<File> files ) throws IOException
    {

        for( File file : files )
        {
            sorter.sort( file, file );
        }
    }


    private List<File> split( File source ) throws IOException
    {
        return splitter.split( source );
    }
}
