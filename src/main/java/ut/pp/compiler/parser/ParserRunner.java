package ut.pp.compiler.parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ut.pp.ast.ProgramNode;
import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParserRunner {
    public static ProgramNode parseString(String input){
        MyLangLexer myLangLexer = new MyLangLexer(CharStreams.fromString(input));
        myLangLexer.removeErrorListeners();
        myLangLexer.addErrorListener(ExceptionErrorListener.listener);

        CommonTokenStream tokens = new CommonTokenStream(myLangLexer);
        MyLangParser parser = new MyLangParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ExceptionErrorListener.listener);

        ParseTree tree = parser.program();
        ASTBuilder builder = new ASTBuilder();
        return (ProgramNode) builder.visit(tree);
    }

    public static ProgramNode parseFile(Path path) throws IOException {
        String input = Files.readString(path);
        return parseString(input);
    }



}
