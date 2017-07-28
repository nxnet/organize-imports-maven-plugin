package io.nxnet.imports.eclipse;

import javax.annotation.Generated;

public class ImportGroupDefinition
{
    private int order;

    private ImportExpression expression;

    @Generated("SparkTools")
    private ImportGroupDefinition(Builder builder)
    {
        this.order = builder.order;
        this.expression = builder.expression;
    }

    public int getOrder()
    {
        return order;
    }

    public ImportExpression getExpression()
    {
        return expression;
    }

    public boolean matches(String importStatment)
    {
        return this.expression.matches(importStatment);
    }

    /**
     * Creates builder to build {@link ImportGroupDefinition}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link ImportGroupDefinition}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private int order;
        private ImportExpression expression;

        private Builder()
        {
        }

        public Builder withOrder(int order)
        {
            this.order = order;
            return this;
        }

        public Builder withExpression(ImportExpression expression)
        {
            this.expression = expression;
            return this;
        }

        public ImportGroupDefinition build()
        {
            return new ImportGroupDefinition(this);
        }
    }

}
