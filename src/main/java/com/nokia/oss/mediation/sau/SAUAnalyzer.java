package com.nokia.oss.mediation.sau;

import com.nokia.oss.io.sort.external.ExternalSortEngine;
import com.nokia.oss.io.sort.external.Hashing;
import com.nokia.oss.io.sort.external.SortConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by harchen on 9/11/2015.
 */
public class SAUAnalyzer
{
    private static final Logger LOG = LoggerFactory.getLogger( SAUAnalyzer.class );
    private ExecutorService executorService;
    private final Comparator<String> imsiComparator = new IMSIComparator();
    private final Comparator<String> msisdnComparator = new MSISDNComparator();
    private final Hashing<String> imsiHashing = new IMSIHashing();
    private final Hashing<String> msisdnHashing = new MSISDNHashing();


    public SAUAnalyzer( ExecutorService executorService )
    {
        this.executorService = executorService;
    }


    public void analyze( File source, File target )
    {
        sort( source, target, imsiComparator, imsiHashing );
        // sort( target, target, msisdnComparator, msisdnHashing );
    }


    private void sort( File source, File target, Comparator<String> comparator, Hashing<String> hashing )
    {
        SortConfig config = new SortConfig();
        config.setComparator( imsiComparator );
        config.setHashing( imsiHashing );
        config.setMemoryFootprint( 100 * 1024 * 1024 );
        config.setConcurrencyDegree( 4 );
        config.setExecutor( executorService );
        config.setRemoveDuplication( true );
        try
        {
            ExternalSortEngine engine = new ExternalSortEngine( config );
            engine.sort( source, target );
        }
        catch( Exception e )
        {
            LOG.error( "Failed to sort SAU record file " + source.getAbsolutePath(), e );
        }
    }


    public static void main( String[] args ) throws Exception
    {
        File source = new File( "D:\\data\\saudata.txt" );
        File target = new File( "D:\\data\\saudata_sorted.txt" );

        ExecutorService executorService = Executors.newFixedThreadPool( 4 );
        SAUAnalyzer analyzer = new SAUAnalyzer( executorService );
        analyzer.analyze( source, target );
        executorService.shutdownNow();
    }

}
