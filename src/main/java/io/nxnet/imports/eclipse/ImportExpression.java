package io.nxnet.imports.eclipse;

public interface ImportExpression extends Comparable<ImportExpression>
{
    boolean matches(String importStatement);

    int getSpecializationFactor();
}
