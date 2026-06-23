package ut.pp;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;

import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Programming Paradigms");

        String input = """
                // first program
                int x = 5;
                int y = x + 3;
                bool bigger = y > 5;

                if (bigger) {
                    print y;
                }

                while (x > 0) {
                    print x;
                    x = x - 1;
                }
                """;

        MyLangLexer myLangLexer = new MyLangLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(myLangLexer);
        MyLangParser parser = new MyLangParser(tokens);

        ParseTree tree = parser.program();

        System.out.println("Children: " + tree.getChildCount() + ", parsed text: " + tree.getText());
    }
}
