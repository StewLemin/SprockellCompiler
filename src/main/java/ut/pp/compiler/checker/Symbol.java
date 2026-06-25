package ut.pp.compiler.checker;

import ut.pp.ast.type.TypeNode;

public class Symbol {
    private final String name;
    private final TypeNode type;
    private boolean initialized = false;

    public Symbol(String name, TypeNode type, boolean initialized){
        this.name = name;
        this.type = type;
        this.initialized = initialized;
    }

    public void initialize(){
        initialized = true;
    }

    public boolean isInitialized(){
        return initialized;
    }

}
