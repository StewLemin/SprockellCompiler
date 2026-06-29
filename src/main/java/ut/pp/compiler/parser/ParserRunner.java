package ut.pp.compiler.parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ut.pp.ast.ProgramNode;
import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;

public class ParserRunner {
    public static ProgramNode parse(String input){
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
}
