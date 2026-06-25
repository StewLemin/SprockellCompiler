package ut.pp.ast.expr;

import ut.pp.ast.ASTNode;
import ut.pp.ast.ExprNode;

public class BoolNode extends ExprNode {
    public final boolean value;

    public BoolNode(boolean value){
        this.value = value;
    }


}
