package ut.pp.ast.concurrency;

import ut.pp.ast.StatementNode;

public class LockNode extends StatementNode {
    public final String identifier;

    public LockNode(String name){
        this.identifier = name;
    }

}
