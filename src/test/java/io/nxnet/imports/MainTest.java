package io.nxnet.imports;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import io.nxnet.imports.javaparser.JFileBlockParserImpl;

/**
 * 
 * @author nruzic
 *
 */
@RunWith(JUnitPlatform.class)
public class MainTest
{
    private JFileBlockParser jFileBlockParser;

    @BeforeEach
    public void setUp() throws Exception
    {
        jFileBlockParser = new JFileBlockParserImpl();
    }

    @Test
    @DisplayName("General test for current tasks")
    public void testSomething() throws Exception
    {
        // creates an input stream for the file to be parsed
        InputStream in = new BufferedInputStream(
                this.getClass().getResourceAsStream("/io/nxnet/imports/UnsortedImports.java"));

        // parse the file
        Set<JFileBlock> jFileBlocks = this.jFileBlockParser.parse(in);

        // Sort alphabetically
        System.out.println("====================== Sorted ==========================");
        System.out.print(String.join("\n", JFileBlocks.sortImports(jFileBlocks,
                (i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()))
                .stream().map(fb -> fb.getContent()).collect(Collectors.toList())));
        System.out.println("====================== Sorted ==========================");
    }
    
}
