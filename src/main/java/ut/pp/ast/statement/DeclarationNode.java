package ut.pp.ast.statement;
import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.type.TypeNode;

public class DeclarationNode extends StatementNode {
    public final TypeNode type;
    public final String identifier;
    public final ExprNode value;

    public DeclarationNode(TypeNode type,String identifier){
        this.identifier = identifier;
        this.type = type;
        this.value = null;
    }

    public DeclarationNode(TypeNode type, String identifier, ExprNode expression){
        this.type = type;
        this.identifier = identifier;
        this.value = expression;
    }
}
