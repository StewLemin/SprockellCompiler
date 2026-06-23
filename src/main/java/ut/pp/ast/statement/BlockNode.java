package ut.pp.ast.statement;

import java.util.List;
import ut.pp.ast.StatementNode;

public class BlockNode extends StatementNode {
    public final List<StatementNode> statements;

    public BlockNode(List<StatementNode> statements) {
        this.statements = statements;
    }
}
