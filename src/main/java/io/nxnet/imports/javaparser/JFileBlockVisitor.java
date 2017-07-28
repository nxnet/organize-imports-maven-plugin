package io.nxnet.imports.javaparser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import io.nxnet.imports.JFileBlock;
import io.nxnet.imports.JFileBlockAccessModifier;
import io.nxnet.imports.JFileBlockImpl;
import io.nxnet.imports.JFileBlockType;

public class JFileBlockVisitor extends VoidVisitorAdapter<Integer>
{
    final LexicalPreservingPrinter preservingPrinter;

    SortedMap<Position, String> codeChunkByPosition = new TreeMap<>();

    Map<JFileBlock, SortedSet<Position>> fileBlockPositions = new HashMap<>();

    public JFileBlockVisitor(LexicalPreservingPrinter preservingPrinter)
    {
        super();
        this.preservingPrinter = preservingPrinter;
    }

    @Override
    public void visit(CompilationUnit n, Integer arg)
    {
        // Visit comment
        if (n.getComment().isPresent())
        {
            this.visitComment(n.getComment().get(), JFileBlockImpl.builder()
                    .withType(JFileBlockType.COMMENT)
                    .withName(UUID.randomUUID().toString())
                    .build());
        }

        // Visit orphan comments
        n.getOrphanComments().forEach(c -> this.visitComment(c, JFileBlockImpl.builder()
                .withType(JFileBlockType.COMMENT)
                .withName(UUID.randomUUID().toString())
                .build()));

        // Visit EOF
        if (n.getEnd().isPresent())
        {
            String endOfFileChunk =  this.toString(n).endsWith("\n") ? "\n" : "";
            this.visitEOF(endOfFileChunk, n.getEnd().get());
        }

        // Visit compilation unit children
        super.visit(n, arg);
    }

    @Override
    public void visit(PackageDeclaration n, Integer arg)
    {
        this.visitCommentAwareNode(n, JFileBlockImpl.builder()
                .withType(JFileBlockType.PACKAGE)
                .withName(n.getNameAsString())
                .withIndex(n.getBegin().get().line)
                .build());
    }

    @Override
    public void visit(ImportDeclaration n, Integer arg)
    {
        Function<ImportDeclaration, Set<JFileBlockAccessModifier>> toModifiers = i -> {
            return i.isStatic() ? new HashSet<>(Arrays.asList(JFileBlockAccessModifier.STATIC)) : new HashSet<>();
        };
        this.visitCommentAwareNode(n, JFileBlockImpl.builder()
                .withType(JFileBlockType.IMPORT)
                .withName(n.getNameAsString())
                .withIndex(n.getBegin().get().line)
                .withAccessModifiers(toModifiers.apply(n))
                .build());
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Integer arg)
    {
        this.visitJavadocAwareNode(n, JFileBlockImpl.builder()
                .withType(n.isInterface() ? JFileBlockType.INTERFACE : JFileBlockType.CLASS)
                .withName(n.getNameAsString())
                .withIndex(n.getBegin().get().line)
                .withAccessModifiers(n.getModifiers().stream().map(m -> toJFileBlockModifier(m))
                        .collect(Collectors.toSet()))
                .build());
    }

    @Override
    public void visit(EnumDeclaration n, Integer arg)
    {
        this.visitJavadocAwareNode(n, JFileBlockImpl.builder()
                .withType(JFileBlockType.ENUM)
                .withName(n.getNameAsString())
                .withIndex(n.getBegin().get().line)
                .withAccessModifiers(n.getModifiers().stream().map(m -> toJFileBlockModifier(m))
                        .collect(Collectors.toSet()))
                .build());
    }

    @Override
    public String toString()
    {
//        SortedMap<Integer, String> chunksByLine = this
//                .mapToLineNumber(this.mapMissingPositions(this.reduceBySamePositionLine(this.codeChunkByPosition)));
        //return String.join("\n", chunksByLine.values());
        return String.join("\n", this.collectToJFileBlock(this.fileBlockPositions,
                this.mapMissingPositions(this.reduceBySamePositionLine(this.codeChunkByPosition)))
                .stream().sorted().map(fb -> fb.getContent()).collect(Collectors.toList()));
    }

    public Set<JFileBlock> getJFileBlocks()
    {
        return this.collectToJFileBlock(this.fileBlockPositions,
                this.mapMissingPositions(this.reduceBySamePositionLine(this.codeChunkByPosition)));
    }

    private <N extends Node> void visitJavadocAwareNode(TypeDeclaration<? extends TypeDeclaration<?>> n,
            JFileBlock fileBlock)
    {
        // Visit javadoc comment
        if (n.getJavadocComment().isPresent())
        {
            this.visitComment(n.getJavadocComment().get(), fileBlock);
        }

        // Visit node and its comments
        this.visitCommentAwareNode(n, fileBlock);
    }

    private <N extends Node> void visitCommentAwareNode(N n, JFileBlock fileBlock)
    {
        // Visit comment
        if (n.getComment().isPresent())
        {
            this.visitComment(n.getComment().get(), fileBlock);
        }

        // Visit orphan comments
        n.getOrphanComments().forEach(c -> this.visitComment(c, fileBlock));

        // Visit node
        this.visitNode(n, fileBlock);
    }

    private void visitComment(Comment comment, JFileBlock fileBlock)
    {
        // Record chunk
        Position position = this.putFileChunk(comment);

        // Record position
        this.fileBlockPositions.computeIfAbsent(fileBlock, fb -> new TreeSet<>()).add(position);
    }

    private <N extends Node> void visitNode(N n, JFileBlock fileBlock)
    {
        // Record chunk
        Position position = this.putFileChunk(n);

        // Record position
        this.fileBlockPositions.computeIfAbsent(fileBlock, fb -> new TreeSet<>()).add(position);
    }

    private void visitEOF(String eof, Position p)
    {
        // Record chunk
        Position position = this.putFileChunk(p, eof);

        // Record block
        JFileBlock fileBlock = JFileBlockImpl.builder().withType(JFileBlockType.EOF).withName("EOF")
                .build();
        this.fileBlockPositions.computeIfAbsent(fileBlock, fb -> new TreeSet<>()).add(position);
    }

    private <N extends Node> Position putFileChunk(N n)
    {
        return this.putFileChunk(n.getBegin().get(), this.toString(n));
    }

    private Position putFileChunk(Comment n)
    {
        return this.putFileChunk(n.getBegin().get(), this.toString(n));
    }

    private Position putFileChunk(Position p, String chunk)
    {
        this.codeChunkByPosition.put(p, chunk);
        return Position.pos(p.line, 1);
    }

    private String toString(Comment lineComment)
    {
        String comment = lineComment.toString();
        if (comment.endsWith("\n"))
        {
            comment = comment.substring(0, comment.length() - 1);
        }
        return comment;
    }

    private <N extends Node> String toString(N n)
    {
        return this.preservingPrinter.print(n);
    }

    private JFileBlockAccessModifier toJFileBlockModifier(Modifier m)
    {
        switch (m)
        {
            case PUBLIC:
                return JFileBlockAccessModifier.PUBLIC;
            case DEFAULT:
                return JFileBlockAccessModifier.PACKAGE;
            case PROTECTED:
                return JFileBlockAccessModifier.PROTECTED;
            case PRIVATE:
                return JFileBlockAccessModifier.PRIVATE;
            case STATIC:
                return JFileBlockAccessModifier.STATIC;
            case FINAL:
                return JFileBlockAccessModifier.FINAL;
            case ABSTRACT:
                return JFileBlockAccessModifier.ABSTRACT;
            default:
                throw new IllegalStateException();
        }
    }

    private SortedSet<JFileBlock> collectToJFileBlock(Map<JFileBlock, SortedSet<Position>> blockPositions,
            SortedMap<Position, String> positionedChunks)
    {
        SortedSet<JFileBlock> fileBlocks = new TreeSet<>();
        SortedSet<Position> processedPositions = new TreeSet<>();
        for (Map.Entry<JFileBlock, SortedSet<Position>> blockPositionsEntry : blockPositions.entrySet())
        {
            JFileBlock fileBlock = blockPositionsEntry.getKey();
            SortedSet<Position> positions = blockPositionsEntry.getValue();
            String blockContent = String.join("\n",
                    positions.stream().map(p -> positionedChunks.get(p)).collect(Collectors.toList()));
            fileBlocks.add(JFileBlockImpl.builder().withType(fileBlock.getType()).withName(fileBlock.getName())
                    .withAccessModifiers(fileBlock.getAccessModifiers()).withContent(blockContent)
                    .withIndex(positions.first().line).build());
            processedPositions.addAll(positions);
        }

        //  find unprocessed positions (file block blanks)
        for (Position position : positionedChunks.keySet())
        {
            if (!processedPositions.contains(position))
            {
                fileBlocks.add(JFileBlockImpl.builder().withType(JFileBlockType.BLANK)
                        .withName(UUID.randomUUID().toString()).withContent(positionedChunks.get(position))
                        .withIndex(position.line).build());
            }
        }

        return fileBlocks;
    }

    private SortedMap<Position, String> mapMissingPositions(SortedMap<Position, String> input)
    {
        SortedMap<Position, String> result = new TreeMap<>();
        int previousChunkEndLine = 0;
        int chunkLineGap = 0;
        Position currentChunkPosition = null;
        String currentChunk = null;
        for (Map.Entry<Position, String> inputEntry : input.entrySet())
        {
            currentChunkPosition = inputEntry.getKey();
            currentChunk = inputEntry.getValue();
            chunkLineGap = currentChunkPosition.line - previousChunkEndLine - 1;
            if (chunkLineGap > 0)
            {
                result.put(Position.pos(previousChunkEndLine + 1, 1),
                        new String(new char[chunkLineGap - 1]).replace("\0", "\n"));
            }

            result.put(currentChunkPosition, currentChunk);
            previousChunkEndLine = currentChunkPosition.line + currentChunk.split("\n").length - 1;
        }

        return result;
    }

    private SortedMap<Position, String> reduceBySamePositionLine(SortedMap<Position, String> input)
    {
        SortedMap<Position, String> result = new TreeMap<>();
        Position resultPosition = null;
        String[] overlappingResultPositionLines = null;
        String overlappingResultPositionFirstLine = null;
        int overlappingResultPositionFirstLineLength = -1;
        String lineOverlapSpace = null;
        for (Position inputPosition : input.keySet())
        {
            resultPosition = Position.pos(inputPosition.line, 1);
            if (result.containsKey(resultPosition))
            {
                overlappingResultPositionLines = result.get(resultPosition).split("\n");
                overlappingResultPositionFirstLine = overlappingResultPositionLines[0];
                overlappingResultPositionFirstLineLength = overlappingResultPositionLines[0].length();
                lineOverlapSpace = this.repeatString(" ",
                        inputPosition.column - 1 - overlappingResultPositionFirstLineLength);
                overlappingResultPositionLines[0] = new StringBuilder()
                        .insert(0, overlappingResultPositionFirstLine)
                        .insert(overlappingResultPositionFirstLineLength, lineOverlapSpace)
                        .insert(inputPosition.column - 1, input.get(inputPosition)).toString();
                result.put(resultPosition, String.join("", overlappingResultPositionLines));
            }
            else
            {
                result.put(resultPosition, input.get(inputPosition));
            }
        }
        return result;
    }

    private String repeatString(String token, int repeat)
    {
        return new String(new char[repeat]).replace("\0", token);
    }
}
