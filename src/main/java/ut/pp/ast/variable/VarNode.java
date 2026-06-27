package ut.pp.ast.variable;

import ut.pp.ast.ExprNode;

public abstract class VarNode extends ExprNode {
    public final String name;

    protected VarNode(String name){
        this.name = name;
    }
}
