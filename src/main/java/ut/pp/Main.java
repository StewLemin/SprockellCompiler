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
                int[3] arr = [1,2,3];
                print arr[0];
                arr[0] = 21;
                print arr[0];
                print arr[2];
                """;
        //to run do
        //cd sprockell
        //stack build if never done before.
        //stack runghc ../output/Generated.hs to run the Generated.hs is built in Main()
        ProgramNode root = ParserRunner.parse(input);
        Checker checker = new Checker();
        checker.check(root);

        CodeGenerator generator = new CodeGenerator();
        List<String> instructions = generator.generate(root);

        System.out.println("Generated " + instructions.size() + " instructions:");

        HaskellOutput output = new HaskellOutput();
        Path outputPath = Path.of("output/Generated.hs");
        try {
            Files.createDirectories(outputPath.getParent());
            output.writeToFile(instructions, outputPath);
            System.out.println("Wrote to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
