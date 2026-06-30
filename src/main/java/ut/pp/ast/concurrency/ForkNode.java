package ut.pp.ast.concurrency;

import ut.pp.ast.StatementNode;
import ut.pp.ast.statement.BlockNode;

public class ForkNode extends StatementNode {
    public final BlockNode body;

    public ForkNode(BlockNode body){
        this.body = body;
    }
}
