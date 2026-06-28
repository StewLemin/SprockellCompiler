package ut.pp.compiler.codegen;

import ut.pp.ast.ExprNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.expr.*;
import ut.pp.ast.statement.*;
import ut.pp.ast.variable.ArrayNode;
import ut.pp.ast.variable.VariableNode;

public class CodeGenerator {
    private SprilProgram code;
    private MemoryManager memory;

    public CodeGenerator() {
        this.code = new SprilProgram();
        this.memory = new MemoryManager();
    }

    private void generateProgram(ProgramNode program) {
        for (StatementNode statement : program.statements) {
            generateStatement(statement);
        }

        //code.emit(Spril.end());
    }

    private void generateStatement(StatementNode statement) {
        if (statement instanceof DeclarationNode declaration) {
            generateDeclaration(declaration);
        } else if (statement instanceof AssignmentNode assignment) {
            generateAssignment(assignment);
        } else if (statement instanceof PrintNode print) {
            generatePrint(print);
        } else if (statement instanceof IfNode ifNode) {
            generateIf(ifNode);
        } else if (statement instanceof WhileNode whileNode) {
            generateWhile(whileNode);
        } else if (statement instanceof BlockNode block) {
            generateBlock(block);
        } else {
            //TODO throw exception
        }
    }

    private void generateDeclaration(DeclarationNode declaration) {
    }

    private void generateAssignment(AssignmentNode assignment) {
        if (assignment.target instanceof VariableNode variable) {
            generateVariableAssignment(variable, assignment.value);
        } else if (assignment.target instanceof ArrayNode array) {
            generateArrayElementAssignment(array, assignment.value);
        } else {
           //throw exception
        }
    }

    private void generateArrayElementAssignment(ArrayNode array, ExprNode value) {
    }

    private void generateVariableAssignment(VariableNode variable, ExprNode value) {
    }

    private void generatePrint(PrintNode print) {
    }

    private void generateIf(IfNode ifNode) {
    }

    private void generateWhile(WhileNode whileNode) {
    }

    private void generateBlock(BlockNode block) {
        //memory.enterScope();

        for (StatementNode statement : block.statements) {
            generateStatement(statement);
        }

        //memory.exitScope();
    }


    //generate for expr
    private void generateExpr(ExprNode expr) {
        if (expr instanceof IntNode intNode) {
            //use code to emit it
        } else if (expr instanceof BoolNode boolNode) {
            //same as before
        } else if (expr instanceof VariableNode var) {
            generateVariableRead(var); // better name?
        } else if (expr instanceof ArrayNode array) {
            generateArrayRead(array); //better name maybe for the method
        } else if (expr instanceof DoubleExprNode doubleExpr) {
            generateDoubleExpr(doubleExpr);
        } else if (expr instanceof SingleExprNode singleExprNode) {
            generateSingleExpr(singleExprNode);
        } else if (expr instanceof ArrayLiteralNode arrayLiteral) {
            //throw an error and handle this case in generateArray since its not scalar>
        } else {
            //TODO throw exception
        }
    }

    private void generateVariableRead(VariableNode var) {
    }

    private void generateArrayRead(ArrayNode array) {
    }


    private void generateDoubleExpr(DoubleExprNode doubleExpr) {
    }

    private void generateSingleExpr(SingleExprNode singleExprNode) {
    }


}

