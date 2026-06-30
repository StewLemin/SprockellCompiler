package ut.pp.ast.concurrency;

import ut.pp.ast.StatementNode;

public class LockOpNode extends StatementNode {
    public final String name;
    public final LockOp operation;

    public LockOpNode(String name,LockOp op){
       this.name = name;
       this.operation = op;
    }
}

