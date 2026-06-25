package ut.pp.compiler.checker;


import ut.pp.ast.type.TypeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ut.pp.ast.type.TypeNode;


public class SymbolTable {

    private List<Map<String,Symbol>> scopesList = new ArrayList<>();

    public void enterScope() {
        scopesList.add(new HashMap<String,Symbol>());
    }

    public void existScope() {
        scopesList.remove(scopesList.size() - 1);
    }

    public void declare(String name,TypeNode type, boolean initialized) {
        if (scopesList.get(scopesList.size() - 1).containsKey(name)){
            Symbol variable = scopesList.get(scopesList.size() - 1).get(name);
            if(!variable.isInitialized()){
                variable.initialize();
            }
        }
        else{
            Symbol variable = new Symbol(name,type,initialized);
            scopesList.get(scopesList.size() - 1).put(name,variable);
        }
    }

    public boolean isDeclaredInCurrScope(String name, TypeNode type) {
        for(int i = scopesList.size() - 1; i >= 0; i--){
            if(scopesList.get(i).containsKey(name)){
                return true;
            }
        }
        return false;
    }

    public Symbol lookup(String name) {
        for(int i = scopesList.size() - 1; i>= 0;i--){
            if(scopesList.get(i).containsKey(name)){
                return scopesList.get(i).get(name);
            }
        }
        return null;
    }

    public void markIntiliazed(String name) {
       lookup(name).initialize();
    }
}
