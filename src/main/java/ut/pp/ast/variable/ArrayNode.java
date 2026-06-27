package ut.pp.ast.variable;

import ut.pp.ast.ExprNode;

public class ArrayNode extends VarNode{
    public final ExprNode index;

    public ArrayNode(String arrayName, ExprNode index) {
        super(arrayName);
        this.index = index;
    }
}
