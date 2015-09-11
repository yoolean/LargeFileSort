package com.nokia.oss.test;

import com.nokia.oss.mediation.sau.SAUAnalyzer;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by harchen on 9/10/2015.
 */
public class LargeTextFileTest
{
    public static void main( String[] args ) throws Exception
    {
        File source = new File( "D:\\data\\saudata.txt" );
        File target = new File( "D:\\data\\saudata_sorted.txt");

        ExecutorService executorService = Executors.newFixedThreadPool( 4 );
        SAUAnalyzer analyzer = new SAUAnalyzer( executorService );
        analyzer.analyze( source, target );
        executorService.shutdownNow();
    }

}
