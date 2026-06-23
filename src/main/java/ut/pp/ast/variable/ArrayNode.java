package ut.pp.ast.variable;

import ut.pp.ast.ExprNode;

public class ArrayNode extends VarNode{
    public final String arrayName;
    public final ExprNode index;

    public ArrayNode(String arrayName, ExprNode index) {
        this.arrayName = arrayName;
        this.index = index;
    }
}
