package ut.pp.compiler.codegen;

import ut.pp.ast.type.TypeNode;




public class    MemoryLocation {

    private final String name;
    private final TypeNode type;
    private final int firstAddress;
    private final int cellCount;
    private final boolean shared;

    public MemoryLocation(String name, TypeNode type, int firstAddress,boolean isShared){

        if (name == null) {
            throw new CodeGeneratorException("Variable name cannot be null.");
        }
        if (type == null) {
            throw new CodeGeneratorException("Variable type cannot be null.");
        }

        this.name = name;
        this.type = type;
        this.shared = isShared;
        this.firstAddress = firstAddress;

        if(type.isArray()){
            if (type.arrayLength == null || type.arrayLength <= 0) {
                throw new CodeGeneratorException("Invalid array size for variable '" + name + "'.");
            }
            this.cellCount = type.arrayLength;
        }else {
            this.cellCount = 1;
        }
    }

    public String getName() {
        return name;
    }

    public TypeNode getType(){
        return type;
    }

    public int getCellCount() {
        return cellCount;
    }

    public int getFirstAddress() {
        return firstAddress;
    }

    public boolean isArray(){
        return type.isArray();
    }

    public boolean isShared() {
        return shared;
    }

    public int addressOfElement(int i){
        if (!isArray()) {
            if (i != 0) {
                throw new CodeGeneratorException("Variable '" + name + "' is not an array.");
            }
            return firstAddress;
    }

        if (i < 0 || i >= cellCount) {
            throw new CodeGeneratorException(
                    "Array index " + i + " out of bounds for '" + name + "'."
            );
        }

        return firstAddress + i;
    }

    @Override
    public String toString() {
        return name + " -> address " + firstAddress + ", size " + cellCount;
    }
}
