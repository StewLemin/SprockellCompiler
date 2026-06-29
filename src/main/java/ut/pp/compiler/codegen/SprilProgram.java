package ut.pp.compiler.codegen;

import java.util.ArrayList;
import java.util.List;

public class SprilProgram {
    public List<String> instructions = new ArrayList<>();

    public SprilProgram(){
        this.instructions = new ArrayList<>();
    }

    public int emit(String instruction){
        instructions.add(instruction);
        return instructions.size() - 1;
    }

    public int size(){
        return instructions.size();
    }

    public void patch(String instruction, int index){
        instructions.set(index,instruction);
    }

    public List<String> getInstruction(){
        return instructions;
    }


}
