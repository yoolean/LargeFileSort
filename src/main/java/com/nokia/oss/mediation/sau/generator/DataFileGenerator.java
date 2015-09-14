package com.nokia.oss.mediation.sau.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class DataFileGenerator
{

    private static final int NUMBER_RANGE = 10;

    private static final int IMSI_LENGTH = 15;
    private static final int MSISDN_LENGTH = 15;

    private static final int PAIR_LENGTH = 1000 * 10000;

    public static final String DATA_FILE = "D:\\data\\saudata.txt";


    private static String generateData( int length )
    {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < length; i++ )
        {
            buffer.append( random.nextInt( NUMBER_RANGE ) );
        }
        return buffer.toString();
    }


    private static void generateDataFile( int count, String output ) throws IOException
    {

        PrintWriter writer = new PrintWriter( new FileWriter( output ) );

        for( int i = 0; i < count; i++ )
        {
            writer.println( generateData( IMSI_LENGTH ) + " " + generateData( MSISDN_LENGTH ) );
        }

        writer.close();
    }


    public static void main( String[] args )
    {
        try
        {
            generateDataFile( Integer.parseInt( args[0] ), args[1] );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }
}
