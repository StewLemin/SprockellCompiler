package ut.pp.ast.expr;

import ut.pp.ast.ExprNode;
import java.util.List;

public class ArrayLiteralNode extends ExprNode {
    public final List<ExprNode> elements;

    public ArrayLiteralNode(List<ExprNode> elements) {
        this.elements = elements;
    }
}