package ut.pp.ast.expr;

import ut.pp.ast.ExprNode;

public class SingleExprNode extends ExprNode {
    public final String operator;
    public final ExprNode expression;

    public SingleExprNode(String op, ExprNode expr) {
        this.operator = op;
        this.expression = expr;
    }
}
