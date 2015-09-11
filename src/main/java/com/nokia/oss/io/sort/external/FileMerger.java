package com.nokia.oss.io.sort.external;

import java.io.File;
import java.io.IOException;


/**
 * Created by harchen on 9/10/2015.
 */
public interface FileMerger
{
    File merge( File firstFile, File secondFile ) throws IOException;
}
