package io.nxnet.imports;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JFileBlocks
{
    public static SortedSet<JFileBlock> sortImports(Collection<JFileBlock> original, Comparator<JFileBlock> comparator)
    {
        return sort(original, comparator, fb -> fb.getType().equals(JFileBlockType.IMPORT));
    }

    public static SortedSet<JFileBlock> sort(Collection<JFileBlock> original, Comparator<JFileBlock> comparator,
            Predicate<JFileBlock> filter)
    {
        List<JFileBlock> matchedBlocks = original.stream().filter(filter).collect(Collectors.toList());
        List<JFileBlock> ignoredBlocks = original.stream().filter(filter.negate()).collect(Collectors.toList());
        for (JFileBlock mb1 : matchedBlocks)
        {
            for (JFileBlock mb2 : matchedBlocks)
            {
                if (comparator.compare(mb1, mb2) < 0 && mb1.getIndex() - mb2.getIndex() > 0
                        || comparator.compare(mb1, mb2) > 0 && mb1.getIndex() - mb2.getIndex() < 0)
                {
                    mb1.switchPositionWith(mb2);
                }
            }
        }

        SortedSet<JFileBlock> result = new TreeSet<>(matchedBlocks);
        result.addAll(ignoredBlocks);
        return result;
    }
}
