package ut.pp.ast.type;

import ut.pp.ast.ASTNode;

public class TypeNode extends ASTNode {
    public final TypeKind kind;
    public final Integer arrayLength; // we define null as not an array

    public TypeNode(TypeKind kind, Integer arrayLength) {
        this.kind = kind;
        this.arrayLength = arrayLength;
    }

    public boolean isArray() {
        return  arrayLength != null;
    }
}
