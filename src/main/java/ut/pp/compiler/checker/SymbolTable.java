package ut.pp.compiler.checker;


import ut.pp.ast.type.TypeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SymbolTable {

    private List<Map<String,Symbol>> scopesList = new ArrayList<>();
    private final Map <String, Map<String, Integer>> enums = new HashMap<>();


    public boolean declareEnum(String name, List <String> values) {
        if (enums.containsKey(name) || isEnumVal(name)) {
            return false;
        }

        Map<String,Integer> current = new HashMap<>();

        for(int i=0; i<values.size(); i++) {
            String val = values.get(i);
            if(enums.containsKey(val) || isEnumVal(val) || current.containsKey(val)){
                return false;
            }
            current.put(val,i);
        }

        enums.put(name,current);
        return true;
    }

    public boolean isEnumType(String name) {
        return enums.containsKey(name);
    }

    public boolean isEnumVal (String valName){
        return enumTypeOfVal(valName) != null;
    }

    public String enumTypeOfVal(String valName) {
        for (String enName : enums.keySet()) {
            Map<String, Integer> values = enums.get(enName);
            if (values.containsKey(valName)) {
                return enName;
            }
        }
        return null;
    }

    public Integer enValNumr(String valName) {
        String type = enumTypeOfVal(valName);
        if(type == null){ return null;}
        return enums.get(type).get(valName);
    }


    public void enterScope() {
        scopesList.add(new HashMap<String,Symbol>());
    }

    public void exitScope() {
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

    public void markInitiliazed(String name) {
        Symbol s = lookup(name);
        if (s!=null){
            s.initialize();
        }
    }

    public void declareLock(String name){
        if(!isDeclaredInCurrScope(name)) {
            Symbol lock = new Symbol(name, null, true, true);

        }
    }


}
