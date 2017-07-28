package io.nxnet.imports;

import java.io.IOException;
import java.io.InputStream;

public interface JFileFormatter
{
    String format(InputStream javaFileInputStream) throws IOException;
}
