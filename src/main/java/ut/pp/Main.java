package ut.pp;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;

import ut.pp.compiler.checker.Checker;
import ut.pp.compiler.codegen.CodeGenerator;
import ut.pp.compiler.codegen.HaskellOutput;
import ut.pp.compiler.parser.ASTBuilder;
import ut.pp.compiler.parser.ParserRunner;
import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;
import ut.pp.ast.ProgramNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Programming Paradigms");

        String input = """
                int cnt = 0;
                
                                                                         while(TRUE){
                                                                             cnt = cnt + 1;
                                                                         }
                """;
        //to run do
        //cd sprockell
        //stack build if never done before.
        //stack runghc ../output/Generated.hs to run the Generated.hs is built in Main()
        ProgramNode root = ParserRunner.parseString(input);
        Checker checker = new Checker();
        checker.check(root);

        CodeGenerator generator = new CodeGenerator();
        List<List<String>> programs = generator.generate(root);


        HaskellOutput output = new HaskellOutput();
        Path outputPath = Path.of("output/Generated.hs");
        try {
            Files.createDirectories(outputPath.getParent());
            output.writeToFile(programs, outputPath);
            System.out.println("Wrote to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
