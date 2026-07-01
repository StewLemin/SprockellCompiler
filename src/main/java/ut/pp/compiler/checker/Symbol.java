package ut.pp.compiler.checker;

import ut.pp.ast.type.TypeNode;

public class Symbol {
    private final String name;
    private final TypeNode type;
    private boolean initialized = false;
    private final boolean isLock;

    public Symbol(String name, TypeNode type, boolean isInitialized, boolean isLock){
        this.name = name;
        this.type = type;
        this.initialized = isInitialized;
        this.isLock = isLock;
    }

    public void initialize(){
        initialized = true;
    }

    public boolean isInitialized(){
        return initialized;
    }

    public TypeNode getType(){
        return type;
    }

    public boolean isLock(){
        return isLock;
    }

}
