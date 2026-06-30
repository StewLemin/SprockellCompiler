package ut.pp.ast.statement;

import ut.pp.ast.StatementNode;

import java.util.List;

public class EnumNode extends StatementNode {
    public final String name;
    public final List<String> enumValues;


    public EnumNode(String name, List<String> values){
        this.name = name;
        this.enumValues = values;
    }



}
