package ut.pp.ast.expr;

import ut.pp.ast.ExprNode;

public class DoubleExprNode extends ExprNode {
    public final ExprNode expr1;
    public final String operator;
    public final ExprNode expr2;

    DoubleExprNode(ExprNode expr1,String Operator,ExprNode expr2){
        this.expr1 = expr1;
        this.operator = Operator;
        this.expr2 = expr2;
    }
}
