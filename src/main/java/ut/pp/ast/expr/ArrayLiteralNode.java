package ut.pp.ast.expr;

import ut.pp.ast.ExprNode;
import ut.pp.ast.variable.ArrayNode;

public class ArrayLiteralNode extends ExprNode {
    public final ExprNode index;
    public final String arrayName;

    public ArrayLiteralNode(ExprNode index, String name) {
        this.index = index;
        this.arrayName = name;
    }
}
