package io.nxnet.imports.javaparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.github.javaparser.utils.Pair;

import io.nxnet.imports.JFileBlock;
import io.nxnet.imports.JFileBlockParser;

public class JFileBlockParserImpl implements JFileBlockParser
{
    @Override
    public Set<JFileBlock> parse(InputStream javaFileInputStream) throws IOException
    {
        // Parse the input stream into compilation unit
        Pair<ParseResult<CompilationUnit>, LexicalPreservingPrinter> parseResultAndPrinter = LexicalPreservingPrinter
                .setup(ParseStart.COMPILATION_UNIT, new StreamProvider(javaFileInputStream));
        CompilationUnit cu = parseResultAndPrinter.a.getResult().get();
        LexicalPreservingPrinter preservingPrinter = parseResultAndPrinter.b;

        // Visit compilation unit
        JFileBlockVisitor visitor = new JFileBlockVisitor(preservingPrinter);
        visitor.visit(cu, null);

        // return file blocks
        return visitor.getJFileBlocks();
    }

}
