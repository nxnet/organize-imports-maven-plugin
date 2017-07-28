package io.nxnet.imports;

import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
public class JFileBlocksTest
{
    @Test
    public void sortImports() throws Exception
    {
        assertTrue("org.junit.Rule".compareTo("org.junit.platform.runner.JUnitPlatform") < 0);
        assertTrue("org.junit.Rule".compareToIgnoreCase("org.junit.platform.runner.JUnitPlatform") > 0);

        SortedSet<JFileBlock> sortedImports = JFileBlocks.sortImports(Stream.of(
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.platform.runner.JUnitPlatform").withIndex(25).build(),
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.platform.runner.JUnitPlatform").withIndex(29).build(),
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.Rule").withIndex(30).build()).collect(Collectors.toList()),
                (fb1, fb2) -> fb1.getName().compareToIgnoreCase(fb2.getName()));
        sortedImports.forEach(System.out::println);

        JFileBlock[] expected = new JFileBlock[] {
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.platform.runner.JUnitPlatform").withIndex(25).build(),
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.platform.runner.JUnitPlatform").withIndex(29).build(),
                JFileBlockImpl.builder().withType(JFileBlockType.IMPORT)
                    .withName("org.junit.Rule").withIndex(30).build()};
        JFileBlock[] actual = sortedImports.stream().collect(Collectors.toList()).toArray(new JFileBlock[] {});
        int index = 0;
        for (JFileBlock expectedBlock : expected)
        {
            assertEquals(expectedBlock, actual[index++]);
        }
    }
}
