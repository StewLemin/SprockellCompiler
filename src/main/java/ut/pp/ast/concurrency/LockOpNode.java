package ut.pp.ast.concurrency;

import ut.pp.ast.StatementNode;

public class LockOpNode extends StatementNode {
    public final String identifier;
    public final LockOp operation;

    public LockOpNode(String name,LockOp op){
       this.identifier = name;
       this.operation = op;
    }
}

