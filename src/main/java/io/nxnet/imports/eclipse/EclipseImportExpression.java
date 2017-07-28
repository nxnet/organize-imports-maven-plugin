package io.nxnet.imports.eclipse;

import java.util.regex.Pattern;
import javax.annotation.Generated;

public class EclipseImportExpression implements ImportExpression
{
    private String prefix;

    private Boolean statical;

    private int specializationFactor;

    private Pattern importDefinitionPattern;

    @Generated("SparkTools")
    private EclipseImportExpression(Builder builder)
    {
        this.prefix = builder.prefix;
        this.statical = builder.statical;
        this.importDefinitionPattern = Pattern.compile(new StringBuilder("import\\s+")
                .append(this.getStaticExpression()).append(this.getPrefixExpression()).toString());
        this.specializationFactor = this.prefix.split("\\.").length;
    }

    @Override
    public int compareTo(ImportExpression o)
    {
        return o.getSpecializationFactor() - this.specializationFactor;
    }

    @Override
    public boolean matches(String importStatement)
    {
        return this.importDefinitionPattern.matcher(importStatement).matches();
    }

    @Override
    public int getSpecializationFactor()
    {
        return this.specializationFactor;
    }

    /**
     * Creates builder to build {@link EclipseImportExpression}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link EclipseImportExpression}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private String prefix;
        private Boolean statical;

        private Builder()
        {
        }

        public Builder withPrefix(String prefix)
        {
            this.prefix = prefix;
            return this;
        }

        public Builder withStatical(Boolean statical)
        {
            this.statical = statical;
            return this;
        }

        public EclipseImportExpression build()
        {
            return new EclipseImportExpression(this);
        }
    }

    String getStaticExpression()
    {
        if (this.statical == null)
        {
            return "(static\\\\s+|)";
        }
        else if (this.statical)
        {
            return "static\\s+";
        }
        return "";
    }
    
    String getPrefixExpression()
    {
        return prefix.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");
    }
}
