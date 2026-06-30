package ut.pp.ast.type;

import ut.pp.ast.ASTNode;

public class TypeNode extends ASTNode {
    public final TypeKind kind;
    public final Integer arrayLength; // we define null as not an array
    public final String typeName;

    public TypeNode(TypeKind kind) {
        this.kind = kind;
        this.arrayLength = null;
        this.typeName = null;
    }

    public TypeNode(TypeKind kind, String name){
        this.kind = kind;
        this.typeName = name;
        this.arrayLength = null;
    }



    public TypeNode(TypeKind kind, Integer arrayLength) {
        this.kind = kind;
        this.arrayLength = arrayLength;
        this.typeName = null;
    }

    public TypeNode(TypeKind kind, String name,Integer arrayLength){
        this.arrayLength = arrayLength;
        this.typeName = name;
        this.kind = kind;
    }





    public boolean isArray() {
        return  arrayLength != null;
    }

    public boolean isEnum(){
        return kind == TypeKind.ENUM;
    }
}
