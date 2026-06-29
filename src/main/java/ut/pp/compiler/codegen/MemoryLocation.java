package ut.pp.compiler.codegen;

import ut.pp.ast.type.TypeNode;
import java.util.Objects;



public class    MemoryLocation {

    private final String name;
    private final TypeNode type;
    private final int firstAdress;
    private final int cellCount;

    public MemoryLocation(String name, TypeNode type, int firstAdress){

//        if (name == null) {
//            throw new CodeGeneratorException("Variable name cannot be null.");
//        }
//        if (type == null) {
//            throw new CodeGeneratorException("Variable type cannot be null.");
//        }

        this.name = name;
        this.type = type;

        this.firstAdress = firstAdress;


        if(type.isArray()){
//            if (type.arrayLength == null || type.arrayLength <= 0) {
//                throw new CodeGeneratorException("Invalid array size for variable '" + name + "'.");
//            }
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

    public int getFirstAdress() {
        return firstAdress;
    }


    public boolean isArray(){
        return type.isArray();
    }



    public int adressOfElement(int i){
        if (!isArray()) {
//            if (i != 0) {
//                throw new CodeGeneratorException("Variable '" + name + "' is not an array.");
//            }
            return firstAdress;
    }

//        if (i < 0 || i >= cellCount) {
//            throw new CodeGeneratorException(
//                    "Array index " + i + " out of bounds for '" + name + "'."
//            );
//        }

        return firstAdress + i;
    }

    @Override
    public String toString() {
        return name + " -> address " + firstAdress + ", size " + cellCount;
    }


}
