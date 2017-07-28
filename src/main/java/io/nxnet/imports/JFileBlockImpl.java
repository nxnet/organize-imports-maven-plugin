package io.nxnet.imports;

import java.util.Set;
import javax.annotation.Generated;

public class JFileBlockImpl implements JFileBlock
{
    protected int index;

    protected int size;

    protected String name;

    protected String content;

    protected JFileBlockType type;

    protected Set<JFileBlockAccessModifier> accessModifiers;

    protected String upperBoundary;

    protected String lowerBoundary;

    @Generated("SparkTools")
    private JFileBlockImpl(Builder builder)
    {
        this.index = builder.index;
        this.size = builder.size;
        this.name = builder.name;
        this.content = builder.content;
        this.type = builder.type;
        this.accessModifiers = builder.accessModifiers;
        this.upperBoundary = builder.upperBoundary;
        this.lowerBoundary = builder.lowerBoundary;
    }

    @Override
    public int getIndex()
    {
        return this.index;
    }

    @Override
    public int getSize()
    {
        return this.size;
    }

    @Override
    public String getContent()
    {
        return this.content;
    }

    @Override
    public JFileBlockType getType()
    {
        return this.type;
    }

    @Override
    public Set<JFileBlockAccessModifier> getAccessModifiers()
    {
        return this.accessModifiers;
    }

    @Override
    public String getUpperBoundary()
    {
        return this.upperBoundary;
    }

    @Override
    public String getLowerBoundary()
    {
        return this.lowerBoundary;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void switchPositionWith(JFileBlock b)
    {
        if (JFileBlockImpl.class.isAssignableFrom(b.getClass()))
        {
            this.switchPositionWith((JFileBlockImpl)b);
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString()
    {
        return "JFileBlockImpl [index=" + index + ", size=" + size + ", name=" + name + ", content=" + content
                + ", type=" + type + ", accessModifiers=" + accessModifiers + ", upperBoundary=" + upperBoundary
                + ", lowerBoundary=" + lowerBoundary + "]";
    }

    private void switchPositionWith(JFileBlockImpl b)
    {
        int myIndex = this.index;
        this.index = b.getIndex();
        b.index = myIndex;
    }

    /**
     * Creates builder to build {@link JFileBlockImpl}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link JFileBlockImpl}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private int index;
        private int size;
        private String name;
        private String content;
        private JFileBlockType type;
        private Set<JFileBlockAccessModifier> accessModifiers;
        private String upperBoundary;
        private String lowerBoundary;

        private Builder()
        {
        }

        public Builder withIndex(int index)
        {
            this.index = index;
            return this;
        }

        public Builder withSize(int size)
        {
            this.size = size;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withContent(String content)
        {
            this.content = content;
            return this;
        }

        public Builder withType(JFileBlockType type)
        {
            this.type = type;
            return this;
        }

        public Builder withAccessModifiers(Set<JFileBlockAccessModifier> accessModifiers)
        {
            this.accessModifiers = accessModifiers;
            return this;
        }

        public Builder withUpperBoundary(String upperBoundary)
        {
            this.upperBoundary = upperBoundary;
            return this;
        }

        public Builder withLowerBoundary(String lowerBoundary)
        {
            this.lowerBoundary = lowerBoundary;
            return this;
        }

        public JFileBlockImpl build()
        {
            return new JFileBlockImpl(this);
        }
    }

    @Override
    public int compareTo(JFileBlock o)
    {
        return this.index - o.getIndex();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JFileBlockImpl other = (JFileBlockImpl) obj;
        if (index != other.index)
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
