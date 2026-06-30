package ut.pp.compiler.codegen;

import ut.pp.ast.ExprNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.expr.*;
import ut.pp.ast.statement.*;
import ut.pp.ast.type.TypeNode;
import ut.pp.ast.variable.ArrayNode;
import ut.pp.ast.variable.VariableNode;

public class CodeGenerator {
    private static final String ZERO = Spril.ZERO;
    private static final String R1 = Spril.REG_A;
    private static final String R2 = Spril.REG_B;
    private static final String R3 = Spril.REG_C;
    private static final String R4 = Spril.REG_D;
    private static final String NUMBER_IO = Spril.NUMBER_IO;

    private SprilProgram code;
    private MemoryManager memory;

    //TODO we should add this?? to reset the instance so we make sure that a program wont use old code/memory from previous programs
    //TODO -- especially seen during testing as we would probably test multi instances but ig we can just do taht then???
//    public CodeGenerator() {
//        reset();
//    }
//
//    public List<String> generate(ProgramNode program) {
//        reset();
//        generateProgram(program);
//        return code.getInstruction();
//    }
//
//    private void reset() {
//        this.code = new SprilProgram();
//        this.memory = new MemoryManager();
//    }

    public CodeGenerator() {
        this.code = new SprilProgram();
        this.memory = new MemoryManager();
    }

    public String generateHaskell(ProgramNode program) {
        //return new HaskellEmitter().emit(generate(program));
        return null;
    }

    private void generateProgram(ProgramNode program) {
        for (StatementNode statement : program.statements) {
            generateStatement(statement);
        }

        code.emit(Spril.endProg());
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
        MemoryLocation location = memory.declare(declaration.identifier, declaration.type);

        if (declaration.value == null) {
            return;
        }

        if (location.isArray()) {
            generateArrayValueInto(location, declaration.value);
        } else {
            generateScalarStore(location, declaration.value);
        }
    }

    private void generateScalarStore(MemoryLocation location, ExprNode value) {
    }

    private void generateArrayValueInto(MemoryLocation location, ExprNode value) {
    }

    private void generateAssignment(AssignmentNode assignment) {
        if (assignment.target instanceof VariableNode var) {
            generateVariableAssignment(var, assignment.value);
        } else if (assignment.target instanceof ArrayNode array) {
            generateArrayElementAssignment(array, assignment.value);
        } else {
            //throw exception
        }
    }

    private void generateArrayElementAssignment(ArrayNode array, ExprNode value) {
        MemoryLocation location = memory.search(array.name);
        if(!location.isArray()) {
            //throw exception
        }

        //calculate the address ofc first
        generateExpr(array.index);
        code.emit(Spril.pop(R1));
        code.emit(Spril.load(Spril.immValue(location.getFirstAdress()), R2));
        code.emit(Spril.compute("Add", R2, R1, R2));

        code.emit(Spril.push(R2));

        generateExpr(value);
        code.emit(Spril.pop(R1)); //value
        code.emit(Spril.pop(R2)); //address
        code.emit(Spril.store(R1, Spril.indAddr(R2)));
    }

    private void generateVariableAssignment(VariableNode variable, ExprNode value) {
        MemoryLocation location = memory.search(variable.name);

        if (location.isArray()) {
            generateArrayValueInto(location, value);
        } else {
            generateScalarStore(location, value);
        }
    }

    private void generatePrint(PrintNode print) {

    }

    private void generateIf(IfNode ifNode) {
        generateExpr(ifNode.condition);
        code.emit(Spril.pop(R1));

        int branchToThenIndex = code.emit(Spril.branch(R1, Spril.abs(-1)));
        if (ifNode.elseBlock != null) {
            generateBlock(ifNode.elseBlock);
        }

        int jumpToEndIndex = code.emit(Spril.jump(Spril.abs(-1)));
        int thenStart = code.size();
        code.patch(Spril.branch(R1, Spril.abs(thenStart)), branchToThenIndex);
        generateBlock(ifNode.thenBlock);

        int end = code.size();
        code.patch(Spril.jump(Spril.abs(end)), jumpToEndIndex);
    }

    private void generateWhile(WhileNode whileNode) {
        int conditionStart = code.size();
        generateExpr(whileNode.expression);
        code.emit(Spril.pop(R1));

        int branchToBodyIndex = code.emit(Spril.branch(R1, Spril.abs(-1)));
        int jumpToEndIndex = code.emit(Spril.jump(Spril.abs(-1)));

        int bodyStart = code.size();
        code.patch(Spril.branch(R1, Spril.abs(bodyStart)), branchToBodyIndex);
        generateBlock(whileNode.block);
        code.emit(Spril.jump(Spril.abs(conditionStart)));
        int end = code.size();
        code.patch(Spril.jump(Spril.abs(end)), jumpToEndIndex);
    }

    private void generateBlock(BlockNode block) {
        memory.newScope();

        for (StatementNode statement : block.statements) {
            generateStatement(statement);
        }

        memory.leaveScope();
    }


    //generate for expr
    private void generateExpr(ExprNode expr) {
        if (expr instanceof IntNode intNode) {
            code.emit(Spril.load(Spril.immValue(intNode.value), R1));
            code.emit(Spril.push(R1));
        } else if (expr instanceof BoolNode boolNode) {
            code.emit(Spril.load(Spril.immValue(boolNode.value ? 1 : 0), R1));
            code.emit(Spril.push(R1));
        } else if (expr instanceof VariableNode var) {
            generateVariableRead(var);
        } else if (expr instanceof ArrayNode array) {
            generateArrayRead(array);
        } else if (expr instanceof DoubleExprNode doubleExpr) {
            generateDoubleExpr(doubleExpr);
        } else if (expr instanceof SingleExprNode singleExprNode) {
            generateSingleExpr(singleExprNode);
        } else if (expr instanceof ArrayLiteralNode arrayLiteral) {
            //throw an error and handle this case in generateArray since its not scalar>
        } else {
            throw new CodeGeneratorException("Unsupported expression: " + expr.getClass().getSimpleName());
        }
    }

    private void generateVariableRead(VariableNode var) {
        MemoryLocation location = memory.search(var.name);
        if (location.isArray()) {
            //throw excpeiton
        }

        code.emit(Spril.load(Spril.dirAddr(location.getFirstAdress()), R1));
        code.emit(Spril.push(R1));
    }

    private void generateArrayRead(ArrayNode array) {
        MemoryLocation location = memory.search(array.name);
        if (!location.isArray()) {
            throw new CodeGeneratorException("Variable '" + array.name + "' is not an array.");
        }

        generateExpr(array.index);
        code.emit(Spril.pop(R1));
        code.emit(Spril.load(Spril.immValue(location.getFirstAdress()), R2));
        code.emit(Spril.compute("Add", R2, R1, R2));
        code.emit(Spril.load(Spril.indAddr(R2), R1));
        code.emit(Spril.push(R1));
    }

    private void generateDoubleExpr(DoubleExprNode doubleExpr) {
    }

    private void generateSingleExpr(SingleExprNode singleExprNode) {
        if (!singleExprNode.operator.equals("!")) {
            throw new CodeGeneratorException("Unsupported unary operator: " + singleExprNode.operator);
        }

        generateExpr(singleExprNode.expression);
        code.emit(Spril.pop(R1));
        code.emit(Spril.compute("Equal", R1, ZERO, R1));
        code.emit(Spril.push(R1));
    }
}

