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
        if (!isDeclaredInCurrScope(name)){
            Symbol variable = new Symbol(name,type,initialized);
            scopesList.get(scopesList.size() - 1).put(name,variable);
        }
    }

    public boolean isDeclaredInCurrScope(String name) {
        return  scopesList.get(scopesList.size() - 1).containsKey(name);
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
        Symbol s = lookup(name);
        if (s!=null){
            s.initialize();
        }
    }
}
