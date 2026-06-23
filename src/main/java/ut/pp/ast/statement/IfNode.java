package ut.pp.ast.statement;

import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;

public class IfNode extends StatementNode {
    public final ExprNode condition;
    public final BlockNode thenBlock;
    public final BlockNode elseBlock;

    public IfNode(ExprNode cond, BlockNode thenBlock, BlockNode elseBlock) {
        this.condition = cond;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}
