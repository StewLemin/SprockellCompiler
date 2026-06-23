package ut.pp.ast.statement;

import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;

public class PrintNode extends StatementNode {
    public final ExprNode expression;

    public PrintNode(ExprNode expression) {
        this.expression = expression;
    }
}
