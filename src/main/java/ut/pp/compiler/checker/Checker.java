package ut.pp.compiler.checker;


import java.util.ArrayList;
import java.util.List;
import ut.pp.ast.ExprNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.expr.*;
import ut.pp.ast.statement.*;
import ut.pp.ast.type.TypeKind;
import ut.pp.ast.type.TypeNode;
import ut.pp.ast.variable.ArrayNode;
import ut.pp.ast.variable.VarNode;
import ut.pp.ast.variable.VariableNode;


public class Checker {
    private final List<String> errors = new ArrayList<>();

    private final SymbolTable symbols = new SymbolTable();
    public void check(ProgramNode program) {
        symbols.enterScope();

        for(StatementNode statement : program.statements) {
            checkStatement(statement);
        }

        symbols.exitScope();

        if (!errors.isEmpty()) {
            //throw new TypeCheckException(errors);
        }
    }

    private void checkStatement(StatementNode statement) {
        if(statement instanceof DeclarationNode declaration) {
            checkDeclaration(declaration);
        } else if(statement instanceof AssignmentNode assignment) {
            checkAssignment(assignment);
        } else if(statement instanceof IfNode ifNode) {
            checkIfNode(ifNode);
        } else if(statement instanceof WhileNode whileNode) {
            checkWhileNode(whileNode);
        } else if(statement instanceof BlockNode block) {
            checkBlock(block);
        } else if(statement instanceof PrintNode print) {
            checkPrint(print);
        } else {
            //TODO: add personalized error for statement
        }
    }

    private TypeNode typeOfExpr(ExprNode expr) {
        if(expr instanceof IntNode) {
            return new TypeNode(TypeKind.INT);
        }
        if (expr instanceof BoolNode) {
            return new TypeNode(TypeKind.BOOL);
        }

        if (expr instanceof VariableNode var) {
            return typeOfVariable(var);
        }

        if (expr instanceof ArrayNode array) {
            return typeOfArray(array, true);
        }

        if (expr instanceof SingleExprNode singleExpr) {
            return typeOfSingleExpr(singleExpr);
        }

        if (expr instanceof DoubleExprNode doubleExprNode) {
            return typeOfDoubleExpr(doubleExprNode);
        }

        if (expr instanceof ArrayLiteralNode arrayLiteral) {
            return typeOfArrayLiteral(arrayLiteral);
        }

        //TODO: add personalized error for expr
        return null;
    }

    private TypeNode typeOfArrayLiteral(ArrayLiteralNode arrayLiteral) {
        return null;
    }

    private TypeNode typeOfDoubleExpr(DoubleExprNode doubleExprNode) {
        return null;
    }

    private TypeNode typeOfSingleExpr(SingleExprNode singleExpr) {
        return null;
    }

    private TypeNode typeOfArray(ArrayNode array, boolean b) {
        Symbol symbol = symbols.lookup(array.arrayName);
        if (symbol == null) {
            //error
            return null;
        }

        //TypeNode  arrayType = symbol.getType();
//        if (!arrayType.isArray()) {
//            //error
//            return null;
//        }

        return null;
    }

    private TypeNode typeOfVariable(VariableNode var) {
        Symbol symbol = symbols.lookup(var.name);
        if (symbol == null) {
            //error
            return null;
        }

        if (!symbol.isInitialized()) {
            //error
        }

        return null;
        //return symbol.getType();
    }

    private void checkAssignment(AssignmentNode assignment) {


    }

    private void checkDeclaration(DeclarationNode declaration) {
        if (symbols.isDeclaredInCurrScope(declaration.identifier)) {
            //TODO error
            return;
        }

        boolean initialized = declaration.value != null;
        if (declaration.value != null) {
            TypeNode valueType = typeOfExpr(declaration.value);

            if (!CheckerUtils.sameType(declaration.type, valueType)) {
                //TODO error
            }
        }

        symbols.declare(declaration.identifier, declaration.type, initialized);
    }

    private void checkIfNode(IfNode ifNode) {
        TypeNode condition = typeOfExpr(ifNode.condition);
        if (!CheckerUtils.isBool(condition)) {
            //TODO write error
        }

        checkBlock(ifNode.thenBlock);

        if (ifNode.elseBlock != null) {
            checkBlock(ifNode.elseBlock);
        }
    }

    private void checkWhileNode(WhileNode whileNode) {
        TypeNode condition = typeOfExpr(whileNode.expression);
        if (!CheckerUtils.isBool(condition)) {
            //TODO write error
        }

        checkBlock(whileNode.block);
    }

    private void checkBlock(BlockNode block) {
        symbols.enterScope();

        for(StatementNode statement : block.statements) {
            checkStatement(statement);
        }

        symbols.exitScope();
    }

    private void checkPrint(PrintNode print) {
        typeOfExpr(print.expression);
    }

    private void error(String message) {
        errors.add(message);
    }
}
