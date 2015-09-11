package com.nokia.oss.io.sort.external;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by harchen on 9/10/2015.
 */
public interface FileSplitter
{
    List<File> split( File source ) throws IOException;
}
