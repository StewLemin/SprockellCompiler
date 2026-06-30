package ut.pp;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;

import ut.pp.compiler.checker.Checker;
import ut.pp.compiler.parser.ASTBuilder;
import ut.pp.compiler.parser.ParserRunner;
import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;
import ut.pp.ast.ProgramNode;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Programming Paradigms");

        String input = """
                // first program
                int x = 5;
                int y = x + 3;
                bool bigger = y > 5;
                int[3] arr;
                arr = [1,2,3];
                arr[0] = 4;
                
                if (bigger) {
                    print y;
                }

                while (x > 0) {
                    print x;
                    x = x - 1;
                }
                """;

        ProgramNode root = ParserRunner.parse(input);
        Checker checker = new Checker();
        checker.check(root);
    }
}
