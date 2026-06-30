package ut.pp.compiler.checker;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ut.pp.ast.ExprNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.concurrency.LockNode;
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
        errors.clear();
        symbols.enterScope();

        for(StatementNode statement : program.statements) {
            checkStatement(statement);
        }

        symbols.exitScope();

        if (!errors.isEmpty()) {
            throw new CheckerException(errors);
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
        } else if(statement instanceof LockNode lock){
            checkLockDecl(lock);
        }
        else {
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
            return typeOfArray(array);
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
        if (arrayLiteral.elements.isEmpty()) {
            error("Empty array literal has unknown type.");
            return null;
        }

        TypeNode firstType = typeOfExpr(arrayLiteral.elements.get(0));
        if (firstType == null) {
            return null;
        }
        if (firstType.isArray()) {
            error("Nested arrays are not supported.");
            return null;
        }

        for (int i = 1; i < arrayLiteral.elements.size(); i++) {
            TypeNode currentType = typeOfExpr(arrayLiteral.elements.get(i));

            if(currentType != null && !CheckerUtils.sameType(firstType, currentType)) {
                error("Array literal elements must all have the same type.");
                return null;
            }
        }

        return new TypeNode(firstType.kind, arrayLiteral.elements.size());
    }

    private TypeNode typeOfDoubleExpr(DoubleExprNode doubleExpr) {
        TypeNode left = typeOfExpr(doubleExpr.expr1);
        TypeNode right = typeOfExpr(doubleExpr.expr2);

        if (left == null || right == null) {
            return null;
        }

        switch (doubleExpr.operator) {
            case "+", "-", "*" -> {
                if (!CheckerUtils.isInt(left) || !CheckerUtils.isInt(right)) {
                    error("Operator " + doubleExpr.operator + " requires int operands.");
                    return null;
                }

                return CheckerUtils.intType();
            }
            case "<", "<=", ">=", ">" -> {
                if (!CheckerUtils.isInt(left) || !CheckerUtils.isInt(right)) {
                    error("Operator " + doubleExpr.operator + " requires int operands.");
                    return null;
                }

                return CheckerUtils.boolType();
            }
            case "==", "!=" -> {
                if (!CheckerUtils.sameType(left, right)) {
                    error("Operator " + doubleExpr.operator + " requires operands of the same type.");
                    return null;
                }

                return CheckerUtils.boolType();
            }
            case "&&", "||" -> {
                if(!CheckerUtils.isBool(left) || !CheckerUtils.isBool(right)) {
                    error("Operator " + doubleExpr.operator + " requires bool operands.");
                    return null;
                }

                return CheckerUtils.boolType();
            }
            default -> {
                error("Unknown binary operator '" + doubleExpr.operator + "'.");
                return null;
            }
        }
    }

    private TypeNode typeOfSingleExpr(SingleExprNode singleExpr) {
        TypeNode exprType = typeOfExpr(singleExpr.expression);
        if (exprType == null) {
            return null;
        }

        if (singleExpr.operator.equals("!")) {
            if (!CheckerUtils.isBool(exprType)) {
                error("Operator ! required a bool expression.");
                return null;
            }

            return CheckerUtils.boolType();
        }

        error("Unknown unary operator: '" + singleExpr.operator + "'.");
        return null;
    }

    private TypeNode typeOfArray(ArrayNode array) {
        Symbol symbol = symbols.lookup(array.name);
        if (symbol == null) {
            error("Array '" + array.name+ "' is not declared.");
            return null;
        }

        if (!symbol.isInitialized()) {
            error("Array '" + array.name + "' is used before initialization.");
        }

        TypeNode arrayType = symbol.getType();
        if (!arrayType.isArray()) {
            error("'" + array.name + "' is not an array.");
            return null;
        }

        TypeNode indexType = typeOfExpr(array.index);
        if (indexType == null) {
            return null;
        }
        if (!CheckerUtils.isInt(indexType)) {
            error("Array index of '" + array.name + "' must be int.");
            return null;
        }

        return CheckerUtils.elementType(arrayType);
    }

    private TypeNode typeOfVariable(VariableNode var) {
        Symbol symbol = symbols.lookup(var.name);
        if (symbol == null) {
            error("Variable '" + var.name + "' is not declared.");
            return null;
        }

        if (!symbol.isInitialized()) {
            error("Variable '" + var.name + "' is used before initialization.");
        }

        return symbol.getType();
    }

    private TypeNode typeOfAssignmentTarget(VarNode target) {
        if (target instanceof VariableNode var) {
            Symbol symbol = symbols.lookup(var.name);
            if (symbol == null) {
                error("Variable '" + var.name + "' is not declared.");
                return null;
            }

            return symbol.getType();
        }

        if (target instanceof ArrayNode array) {
            return typeOfArray(array);
        }

        error("Unknown assignment target.");
        return null;
    }

    private void checkAssignment(AssignmentNode assignment) {
        TypeNode targetType = typeOfAssignmentTarget(assignment.target);
        TypeNode valueType = typeOfExpr(assignment.value);

        if (targetType != null && valueType != null && !CheckerUtils.sameType(targetType, valueType)) {
            error("Cannot assign value of type " + CheckerUtils.toString(valueType)
                          + " to target of type " + CheckerUtils.toString(targetType) + ".");
            return;
        }

        if (assignment.target instanceof VariableNode var) {
            symbols.markInitiliazed(var.name);
        }
    }

    private void checkDeclaration(DeclarationNode declaration) {
        if (symbols.isDeclaredInCurrScope(declaration.identifier)) {
            error("Variable '" + declaration.identifier + "' is already declared in this scope.");
            return;
        }

        boolean initialized = false;
        if (declaration.value != null) {
            TypeNode valueType = typeOfExpr(declaration.value);

            if (valueType != null && !CheckerUtils.sameType(declaration.type, valueType)) {
                error("Cannot initialize variable '" + declaration.identifier
                              + "' of type " + CheckerUtils.toString(declaration.type)
                              + " with value of type " + CheckerUtils.toString(valueType) + ".");
            } else if (valueType != null) {
                initialized = true;
            }
        }

        symbols.declare(declaration.identifier, declaration.type, initialized);
    }

    private void checkIfNode(IfNode ifNode) {
        TypeNode condition = typeOfExpr(ifNode.condition);
        if (condition != null && !CheckerUtils.isBool(condition)) {
            error("If condition must be bool, but got " + CheckerUtils.toString(condition) + ".");
        }

        Set<String> ifSet = CheckerUtils.checkForInitialization(ifNode.thenBlock,symbols);
        checkBlock(ifNode.thenBlock);

        if (ifNode.elseBlock != null) {
            checkBlock(ifNode.elseBlock);
        }

        Set<String> elseSet = CheckerUtils.checkForInitialization(ifNode.elseBlock,symbols);
        Set<String> oneBranchOnly = new HashSet<>(ifSet);
        oneBranchOnly.addAll(elseSet);
        Set<String> both = new HashSet<>(ifSet);
        both.retainAll(elseSet);
        oneBranchOnly.removeAll(both);
        for(String v : oneBranchOnly) {
            error("Variable '" + v + "' might not be initialized: it is only assigned in one branch");
        }


    }




    private void checkWhileNode(WhileNode whileNode) {
        TypeNode condition = typeOfExpr(whileNode.expression);
        if (condition != null && !CheckerUtils.isBool(condition)) {
            error("While condition must be bool, but got " + CheckerUtils.toString(condition) + ".");
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
        //if code generator will not print arrays, add condition to reject it
        typeOfExpr(print.expression);
    }

   private void checkLockDecl(LockNode lock){
        SymbolTable symbols = new SymbolTable();
   }


    private void error(String message) {
        errors.add(message);
    }
}
