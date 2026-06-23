package ut.pp.ast.expr;

import ut.pp.ast.ExprNode;

public class IntNode extends ExprNode {
    public final int value;

    public IntNode(int value) {
        this.value = value;
    }
}
