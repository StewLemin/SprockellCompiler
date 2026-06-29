package ut.pp.compiler.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HaskellEmitter {

    public String emit(List<String> instructions) {
        StringBuilder result = new StringBuilder();

        result.append("module Main where\n\n");
        result.append("import Sprockell\n\n");

        result.append("prog :: [Instruction]\n");
        result.append("prog =\n");

        for (int i = 0; i < instructions.size(); i++) {
            if (i == 0) {
                result.append("  [ ");
            } else {
                result.append("  , ");
            }

            result.append(instructions.get(i));
            result.append("\n");
        }

        result.append("  ]\n\n");
        result.append("main = run [prog]\n");

        return result.toString();
    }

    public void writeToFile(List<String> instructions, Path outputPath) throws IOException {
        String haskellCode = emit(instructions);
        Files.writeString(outputPath, haskellCode);
    }
}