package ut.pp.ast.expr;

import ut.pp.ast.ASTNode;

public class BoolNode extends ASTNode{
    public final boolean value;

    BoolNode(boolean value){
        this.value = value;
    }


}
