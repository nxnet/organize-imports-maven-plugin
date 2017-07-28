package io.nxnet.imports;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface JFileBlockParser
{
    Set<JFileBlock> parse(InputStream javaFileInputStream) throws IOException;
}
