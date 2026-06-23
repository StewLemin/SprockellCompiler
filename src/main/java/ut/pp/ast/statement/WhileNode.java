package ut.pp.ast.statement;

import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;

public class WhileNode extends StatementNode {
    public ExprNode expression;
    public BlockNode block;

    public WhileNode(ExprNode expr, BlockNode block) {
        this.expression = expr;
        this.block = block;
    }
}
