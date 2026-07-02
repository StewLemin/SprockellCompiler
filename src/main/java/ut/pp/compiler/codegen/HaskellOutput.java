package ut.pp.compiler.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
//needs testing
public class HaskellOutput {

    public String emit(List<List<String>> processes) {
        int processNumber = processes.size();
        StringBuilder result = new StringBuilder();
        result.append("module Main where\n\n");
        result.append("import Sprockell\n\n");

        for (int i = 0; i < processNumber; i++){
            result.append("prog"+ i + ":: [Instruction]\n");
            result.append("prog" + i +"=\n");
            List<String> instructions = processes.get(i);
            for (int j = 0; j < instructions.size(); j++) {
                if (j == 0) {
                    result.append("  [ ");
                } else {
                    result.append("  , ");
                }

                result.append(instructions.get(j));
                result.append("\n");
            }

            result.append("  ]\n\n");
        }
        result.append("main = run[");
        for(int i = 0; i < processNumber - 1; i++){
           result.append("prog" + i + ",");
        }
        result.append("prog"+(processNumber - 1)+"]\n");
        return result.toString();
    }

    public void writeToFile(List<List<String>> processes, Path outputPath) throws IOException {
        String haskellCode = emit(processes);
        Files.writeString(outputPath, haskellCode);
    }
}