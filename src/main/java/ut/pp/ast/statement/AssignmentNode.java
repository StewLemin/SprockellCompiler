package ut.pp.ast.statement;

import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.variable.VarNode;

public class AssignmentNode extends StatementNode {
    public final VarNode target;
    public final ExprNode value;

    public AssignmentNode(VarNode target, ExprNode value){
        this.target = target;
        this.value = value;
    }
}
