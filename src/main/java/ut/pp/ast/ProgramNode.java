package ut.pp.ast;

import java.util.List;

public class ProgramNode extends ASTNode  {
    public final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {
        this.statements = statements;
    }
}
