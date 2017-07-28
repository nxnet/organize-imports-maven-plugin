package io.nxnet.imports;

import java.util.Set;

public interface JFileBlock extends Comparable<JFileBlock>
{
    int getIndex();

    int getSize();

    String getName();

    String getContent();

    JFileBlockType getType();

    Set<JFileBlockAccessModifier> getAccessModifiers();

    String getUpperBoundary();

    String getLowerBoundary();

    void switchPositionWith(JFileBlock b);
}
