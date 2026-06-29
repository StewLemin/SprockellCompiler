package ut.pp.compiler.codegen;
import ut.pp.ast.type.TypeNode;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
public class MemoryManager {
    private final Stack <Map <String,MemoryLocation>> scopes = new Stack<>();
    private int nextFreeAdress = 0;

    public void newScope(){
        scopes.push(new HashMap<>());

    }
    public MemoryManager(){
        newScope();

    }

    public void leaveScope(){
//        if(scopes.size() <= 1){
//            throw new CodeGeneratorException("Cannot exit global memory scope.");
//        }
        scopes.pop();
    }

    public MemoryLocation declare(String name, TypeNode type){
        Map<String, MemoryLocation> scope = scopes.peek();
//        if (scope == null) {
//            throw new CodeGeneratorException("No active memory scope.");
//        }

//        if(scope.containsKey(name)){
//            throw new CodeGeneratorException(
//                    "Variable '" + name + "' is already declared in this scope."
//            );
//        }

        MemoryLocation location = new MemoryLocation(name,type,nextFreeAdress);
        scope.put(name,location);
        nextFreeAdress += location.getCellCount();
        return location;

    }


    public MemoryLocation search (String name){
        for (int i=scopes.size()-1; i >= 0; i-- ){
            Map<String,MemoryLocation> scope = scopes.get(i);
            MemoryLocation location = scope.get(name);
            if(location != null){
                return location;
            }
        }
       // throw new CodeGeneratorException("Unknown variable: "+name);

    }

    public int getCellsUsed(){
        return nextFreeAdress;
    }

    public boolean isInCurrentScope(String name){
        Map<String,MemoryLocation> scope = scopes.peek();
        return scope.containsKey(name);
    }



}
