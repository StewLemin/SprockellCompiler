package ut.pp.compiler.codegen;

public class ThreadData {
    public final int id;
    public final int startAddress;
    public final int doneAddress;
    public final SprilProgram program;

    public ThreadData(int id,int startFlag, int doneFlag, SprilProgram program){
        this.id = id;
        this.startAddress = startFlag;
        this.doneAddress = doneFlag;
        this.program = program;

    }
}
