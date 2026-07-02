package ut.pp.compiler.codegen;
import ut.pp.ast.type.TypeNode;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
public class MemoryManager {
    private final Stack <Map <String,MemoryLocation>> scopes = new Stack<>();
    private final Map<String,MemoryLocation> sharedMemory = new HashMap<>();
    private int nextFreeAddress = 0;
    private int nextSharedAddress = 0;

    public void newScope(){
        scopes.push(new HashMap<>());

    }
    public MemoryManager(){
        newScope();
    }

    public void leaveScope(){
        if(scopes.size() <= 1){
            throw new CodeGeneratorException("Cannot exit global memory scope.");
        }
        scopes.pop();
    }

    public MemoryLocation declare(String name, TypeNode type, boolean isShared){
        if(isShared){
            MemoryLocation location = new MemoryLocation(name,type,nextSharedAddress,true);
            sharedMemory.put(name,location);
            nextSharedAddress += location.getCellCount();
            return location;
        }
        else{
            Map<String, MemoryLocation> scope = scopes.peek();
            if (scope == null) {
                throw new CodeGeneratorException("No active memory scope.");
            }
            MemoryLocation location = new MemoryLocation(name, type, nextFreeAddress, false);
            scope.put(name,location);
            nextFreeAddress += location.getCellCount();
            return location;
        }
    }

    public int allocateSharedAddress() {
        int address = nextSharedAddress;
        nextSharedAddress++;
        return address;
    }

    public MemoryLocation search (String name){
        for (int i=scopes.size()-1; i >= 0; i-- ){
            Map<String,MemoryLocation> scope = scopes.get(i);
            MemoryLocation location = scope.get(name);
            if(location != null){
                return location;
            }
        }
        MemoryLocation shared = sharedMemory.get(name);
        if(shared != null){
            return shared;
        }
        throw new CodeGeneratorException("Unknown variable: "+name);

    }

    public int getCellsUsed(){
        return nextFreeAddress;
    }

    public boolean isInCurrentScope(String name){
        Map<String,MemoryLocation> scope = scopes.peek();
        return scope.containsKey(name);
    }
}
