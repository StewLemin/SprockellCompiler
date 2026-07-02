package ut.pp.compiler.codegen;

import java.util.*;

import java.util.concurrent.locks.Lock;
import ut.pp.ast.ExprNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.concurrency.*;
import ut.pp.ast.expr.*;
import ut.pp.ast.statement.*;
import ut.pp.ast.variable.ArrayNode;
import ut.pp.ast.variable.VariableNode;

public class CodeGenerator {
    private static final int DIVISION_BY_ZERO_ERROR = -99999999;

    private final Map<String, Integer> enumVal = new HashMap<>();
    private final Map<String, Integer> lockAddresses = new HashMap<>();

    private final List<SprilProgram> programs = new ArrayList<>();
    private SprilProgram code;//old code

    //list of 0 -> 1 -> 2 flags for each thread to check
    private final Stack<List<Integer>> unjoinedThreadsPerScope = new Stack<>();
    private final MemoryManager memory;

    public CodeGenerator() {
        this.memory = new MemoryManager();
        SprilProgram main = new SprilProgram();
        programs.add(main);
        this.code = main;
        unjoinedThreadsPerScope.push(new ArrayList<>());
    }

    public List<List<String>> generate(ProgramNode program) {
        generateProgram(program);
        List<List<String>> result = new ArrayList<>();
        for(SprilProgram p : programs){
            result.add(p.getInstruction());
        }
        return result;
    }

    public String generateHaskell(ProgramNode program) {
        return new HaskellOutput().emit(generate(program));
    }

    private void generateProgram(ProgramNode program) {
        findEnumVal(program.statements);
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
        } else if (statement instanceof LockNode lock) {
            generateLock(lock);
        } else if (statement instanceof LockOpNode lockOp) {
            generateLockOp(lockOp);
        } else if (statement instanceof ForkNode fork) {
            generateFork(fork);
        } else if (statement instanceof JoinNode join) {
            generateJoin(join);
        } else if (statement instanceof EnumNode){
            // No spril code needed for enums
        }else {
            throw new CodeGeneratorException("Unsupported statement: " + statement.getClass().getSimpleName());
        }
    }

    private void generateWaitUntilFinished(int threadStateAddress) {
        int loopStart = code.size();
        //read state value
        code.emit(Spril.readInstr(Spril.dirAddr(threadStateAddress)));
        code.emit(Spril.receive(Spril.REG_A));

        //check if state value == 2
        code.emit(Spril.load(Spril.immValue(2),Spril.REG_B));
        code.emit(Spril.compute("Equal",Spril.REG_A,Spril.REG_B,Spril.REG_B));

        int branchDone = code.emit(Spril.branch(Spril.REG_B, Spril.abs(-1)));

        code.emit(Spril.jump(Spril.abs(loopStart)));

        int after = code.size();
        code.patch(Spril.branch(Spril.REG_B, Spril.abs(after)), branchDone);
    }



    private void generateJoin(JoinNode join) {
        List<Integer> unjoinedThreads = unjoinedThreadsPerScope.peek();
        for (int threadState : unjoinedThreads) {
            generateWaitUntilFinished(threadState);
        }

        unjoinedThreads.clear();
    }

    private void generateThreadWaitForForkStart(int threadStateAddres){
        int waitInstruction = code.size();
        //read current state value
        code.emit(Spril.readInstr(Spril.dirAddr(threadStateAddres)));
        code.emit(Spril.receive(Spril.REG_A));

        //check is current state value is 1 and store result in REB_B
        code.emit(Spril.load(Spril.immValue(1),Spril.REG_B));
        code.emit(Spril.compute("Equal",Spril.REG_A,Spril.REG_B,Spril.REG_B));

        int branchInstruction = code.emit(Spril.branch(Spril.REG_B,Spril.abs(-1)));


        code.emit(Spril.jump(Spril.abs(waitInstruction)));

        int bodyStart = code.size();
        code.patch(Spril.branch(Spril.REG_B,Spril.abs(bodyStart)), branchInstruction);


    }



    private void generateFork(ForkNode fork) {
        int threadStateAddress = memory.allocateSharedAddress();

        SprilProgram parentThread = this.code;
        SprilProgram currentThread = new SprilProgram();


        unjoinedThreadsPerScope.peek().add(threadStateAddress);
        programs.add(currentThread);
        this.code = currentThread;

        unjoinedThreadsPerScope.push(new ArrayList<>());

        generateThreadWaitForForkStart(threadStateAddress);

        generateBlock(fork.body);

        unjoinedThreadsPerScope.pop();

        code.emit(Spril.load(Spril.immValue(2), Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(threadStateAddress)));
        code.emit(Spril.endProg());

        this.code = parentThread;

        code.emit(Spril.load(Spril.immValue(1), Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(threadStateAddress)));
    }


    private void generateLock(LockNode lock) {
        int address = memory.allocateSharedAddress();
        lockAddresses.put(lock.identifier, address);

        //0 means lock is unlocked
        code.emit(Spril.load(Spril.immValue(0), Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(address)));
    }

    private void generateLockOp(LockOpNode lockOp) {
        if (!lockAddresses.containsKey(lockOp.identifier)) {
            throw new CodeGeneratorException("Unknown lock: " + lockOp.identifier);
        }

        if (lockOp.operation == LockOp.ACQUIRE) {
            generateAcquire(lockOp.identifier);
        } else if (lockOp.operation == LockOp.RELEASE){
            generateRelease(lockOp.identifier);
        } else {
            throw new CodeGeneratorException("Unknown lock operation: " + lockOp.operation);
        }
    }

    private void generateAcquire(String lock) {
        int address = lockAddresses.get(lock);

        int loopStart = code.size();
        //test and set the lock
        code.emit(Spril.testAndSet(Spril.dirAddr(address)));
        code.emit(Spril.receive(Spril.REG_A));
        //temporary branch
        int branch = code.emit(Spril.branch(Spril.REG_A, Spril.abs(-1)));
        //if regA == 0, we retry
        code.emit(Spril.jump(Spril.abs(loopStart)));
        //we patch to known address now
        int acquiredAddress = code.size();
        code.patch(Spril.branch(Spril.REG_A, Spril.abs(acquiredAddress)), branch);
    }

    private void generateRelease(String lock) {
        int address = lockAddresses.get(lock);

        //as before 0 means that its unlocked
        code.emit(Spril.load(Spril.immValue(0), Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(address)));
    }

    private void generateDeclaration(DeclarationNode declaration) {
        MemoryLocation location = memory.declare(declaration.identifier, declaration.type,declaration.isShared);

        if (declaration.value == null) {
            generateDefaultValue(location);
            return;
        }

        if (location.isArray()) {
            generateArrayValueInto(location, declaration.value);
        } else {
            generateStore(location, declaration.value);
        }
    }

    private void generateDefaultValue(MemoryLocation location) {
        code.emit(Spril.load(Spril.immValue(0), Spril.REG_A));

        for (int i = 0; i < location.getCellCount(); i++) {
            if (location.isShared()) {
                code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(location.addressOfElement(i))));
            } else {
                code.emit(Spril.store(Spril.REG_A, Spril.dirAddr(location.addressOfElement(i))));
            }
        }
    }

    private void generateStore(MemoryLocation location, ExprNode value) {
        generateExpr(value);
        code.emit(Spril.pop(Spril.REG_A));

        if (location.isShared()) {
            code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(location.getFirstAddress())));
        } else {
            code.emit(Spril.store(Spril.REG_A, Spril.dirAddr(location.getFirstAddress())));
        }

    }

    private void generateArrayValueInto(MemoryLocation location, ExprNode value) {
        if (!location.isArray()) {
            throw new CodeGeneratorException("Target '" + location.getName() + "' is not an array.");
        }

        if (value instanceof ArrayLiteralNode arrayLiteral) {
            generateArrayLiteralIntoMemory(arrayLiteral, location);
            return;
        }

        if (value instanceof VariableNode variable) {
            MemoryLocation source = memory.search(variable.name);
            copyArray(source, location);
            return;
        }

        throw new CodeGeneratorException("Array assignment requires an array literal or another array variable.");
    }

    private void copyArray(MemoryLocation source, MemoryLocation target) {
        if (!source.isArray() || !target.isArray()) {
            throw new CodeGeneratorException("Both source and target must be arrays.");
        }

        if (source.getCellCount() != target.getCellCount()) {
            throw new CodeGeneratorException("Cannot copy arrays of different lengths.");
        }

        for (int i = 0; i < target.getCellCount(); i++) {

            if (source.isShared()) {
                code.emit(Spril.readInstr(Spril.dirAddr(source.addressOfElement(i))));
                code.emit(Spril.receive(Spril.REG_A));
            } else {
                code.emit(Spril.load(Spril.dirAddr(source.addressOfElement(i)), Spril.REG_A));
            }


            if (target.isShared()) {
                code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(target.addressOfElement(i))));
            } else {
                code.emit(Spril.store(Spril.REG_A, Spril.dirAddr(target.addressOfElement(i))));
            }
        }

    }

    private void generateArrayLiteralIntoMemory(ArrayLiteralNode arrayLiteral, MemoryLocation location) {
        if (arrayLiteral.elements.size() != location.getCellCount()) {
            throw new CodeGeneratorException(
                    "Array literal length " + arrayLiteral.elements.size()
                            + " does not match array length " + location.getCellCount() + "."
            );
        }

        for (int i = 0; i < arrayLiteral.elements.size(); i++) {
            generateExpr(arrayLiteral.elements.get(i));
            code.emit(Spril.pop(Spril.REG_A));

            if (location.isShared()) {
                code.emit(Spril.writeInstr(Spril.REG_A, Spril.dirAddr(location.addressOfElement(i))));
            } else {
                code.emit(Spril.store(Spril.REG_A, Spril.dirAddr(location.addressOfElement(i))));
            }
        }

    }

    private void generateAssignment(AssignmentNode assignment) {
        if (assignment.target instanceof VariableNode var) {
            generateVariableAssignment(var, assignment.value);
        } else if (assignment.target instanceof ArrayNode array) {
            generateArrayElementAssignment(array, assignment.value);
        } else {
            throw new CodeGeneratorException("Unsupported assignment target: "
                                                     + assignment.target.getClass().getSimpleName());
        }
    }

    private void generateArrayElementAssignment(ArrayNode array, ExprNode value) {
        MemoryLocation location = memory.search(array.name);
        if(!location.isArray()) {
            throw new CodeGeneratorException("Variable '" + array.name + "' is not an array.");
        }

        //calculate the address z§first
        generateExpr(array.index);
        code.emit(Spril.pop(Spril.REG_A));
        code.emit(Spril.load(Spril.immValue(location.getFirstAddress()), Spril.REG_B));
        //regB will contain the memory address we want to write into
        code.emit(Spril.compute("Add", Spril.REG_B, Spril.REG_A, Spril.REG_B));
        code.emit(Spril.push(Spril.REG_B));
        generateExpr(value);
        code.emit(Spril.pop(Spril.REG_A)); //value
        code.emit(Spril.pop(Spril.REG_B)); //address

        if (location.isShared()) {
            code.emit(Spril.writeInstr(Spril.REG_A, Spril.indAddr(Spril.REG_B)));
        } else {
            code.emit(Spril.store(Spril.REG_A, Spril.indAddr(Spril.REG_B)));
        }

    }

    private void generateVariableAssignment(VariableNode variable, ExprNode value) {
        MemoryLocation location = memory.search(variable.name);

        if (location.isArray()) {
            generateArrayValueInto(location, value);
        } else {
            generateStore(location, value);
        }
    }

    private void generatePrint(PrintNode print) {
        generateExpr(print.expression);
        code.emit(Spril.pop(Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.NUMBER_IO));
    }

    private void generateIf(IfNode ifNode) {
        generateExpr(ifNode.condition);
        code.emit(Spril.pop(Spril.REG_A));

        //temporary address for the then block using Abs -1
        int branchToThenIndex = code.emit(Spril.branch(Spril.REG_A, Spril.abs(-1)));
        if (ifNode.elseBlock != null) {
            generateBlock(ifNode.elseBlock);
        }

        int jumpToEndIndex = code.emit(Spril.jump(Spril.abs(-1)));
        int thenStart = code.size();
        //we patch the previous branch so we can jump
        code.patch(Spril.branch(Spril.REG_A, Spril.abs(thenStart)), branchToThenIndex);
        generateBlock(ifNode.thenBlock);

        //create instruction to jump into
        int end = code.size();
        code.patch(Spril.jump(Spril.abs(end)), jumpToEndIndex);
    }

    private void generateWhile(WhileNode whileNode) {
        //loop condition to jump back
        int conditionStart = code.size();
        generateExpr(whileNode.expression);
        code.emit(Spril.pop(Spril.REG_A));

        //temporary addressees to patch later
        int branchToBodyIndex = code.emit(Spril.branch(Spril.REG_A, Spril.abs(-1)));
        int jumpToEndIndex = code.emit(Spril.jump(Spril.abs(-1)));
        //loop body
        int bodyStart = code.size();
        code.patch(Spril.branch(Spril.REG_A, Spril.abs(bodyStart)), branchToBodyIndex);
        generateBlock(whileNode.block);
        code.emit(Spril.jump(Spril.abs(conditionStart)));
        //instruction after the while
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
            code.emit(Spril.load(Spril.immValue(intNode.value), Spril.REG_A));
            code.emit(Spril.push(Spril.REG_A));
        } else if (expr instanceof BoolNode boolNode) {
            code.emit(Spril.load(Spril.immValue(boolNode.value ? 1 : 0), Spril.REG_A));
            code.emit(Spril.push(Spril.REG_A));
        } else if (expr instanceof VariableNode var) {
            generateVariableRead(var);
        } else if (expr instanceof ArrayNode array) {
            generateArrayRead(array);
        } else if (expr instanceof DoubleExprNode doubleExpr) {
            generateDoubleExpr(doubleExpr);
        } else if (expr instanceof SingleExprNode singleExprNode) {
            generateSingleExpr(singleExprNode);
        } else if (expr instanceof ArrayLiteralNode arrayLiteral) {
            throw new CodeGeneratorException("Array literal cannot be used as a scalar expression");
        } else {
            throw new CodeGeneratorException("Unsupported expression: "
                                                     + expr.getClass().getSimpleName());
        }
    }

    private void generateVariableRead(VariableNode var) {
        if (enumVal.containsKey(var.name)) {
            int number= enumVal.get(var.name);
            code.emit(Spril.load(Spril.immValue(number), Spril.REG_A));
            code.emit(Spril.push(Spril.REG_A));
            return;
        }




        MemoryLocation location = memory.search(var.name);
        if (location.isArray()) {
            throw new CodeGeneratorException("Array variable '" + var.name
                                                     + "' cannot be used as a scalar expression.");
        }

        if (location.isShared()) {
            code.emit(Spril.readInstr(Spril.dirAddr(location.getFirstAddress())));
            code.emit(Spril.receive(Spril.REG_A));
        } else {
            code.emit(Spril.load(Spril.dirAddr(location.getFirstAddress()), Spril.REG_A));
        }

        code.emit(Spril.push(Spril.REG_A));
    }

    private void generateArrayRead(ArrayNode array) {
        MemoryLocation location = memory.search(array.name);
        if (!location.isArray()) {
            throw new CodeGeneratorException("Variable '" + array.name + "'is not an array.");
        }

        generateExpr(array.index);
        code.emit(Spril.pop(Spril.REG_A));
        code.emit(Spril.load(Spril.immValue(location.getFirstAddress()), Spril.REG_B));
        code.emit(Spril.compute("Add", Spril.REG_B, Spril.REG_A, Spril.REG_B));

        if (location.isShared()) {
            code.emit(Spril.readInstr(Spril.indAddr(Spril.REG_B)));
            code.emit(Spril.receive(Spril.REG_A));
        } else {
            code.emit(Spril.load(Spril.indAddr(Spril.REG_B), Spril.REG_A));
        }

        code.emit(Spril.push(Spril.REG_A));
    }

    private void generateDoubleExpr(DoubleExprNode doubleExpr) {
        generateExpr(doubleExpr.expr1);
        generateExpr(doubleExpr.expr2);

        code.emit(Spril.pop(Spril.REG_B)); // right operand
        code.emit(Spril.pop(Spril.REG_A)); // left operand
        switch (doubleExpr.operator) {
            case "+" ->
                    code.emit(Spril.compute("Add", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "-" ->
                    code.emit(Spril.compute("Sub", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "*" ->
                    code.emit(Spril.compute("Mul", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "==" ->
                    code.emit(Spril.compute("Equal", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "!=" ->
                    code.emit(Spril.compute("NEq", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "<" ->
                    code.emit(Spril.compute("Lt", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "<=" ->
                    code.emit(Spril.compute("LtE", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case ">" ->
                    code.emit(Spril.compute("Gt", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case ">=" ->
                    code.emit(Spril.compute("GtE", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "&&" ->
                    code.emit(Spril.compute("And", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "||" ->
                    code.emit(Spril.compute("Or", Spril.REG_A, Spril.REG_B, Spril.REG_A));
            case "/" ->
                    generateDivision();
            default ->
                    throw new CodeGeneratorException("Unsupported double operator: " + doubleExpr.operator);
        }

        code.emit(Spril.push(Spril.REG_A));
    }

    private void generateDivision() {
        //registers meaning
        // REG_A = numerator
        // REG_B = denominator
        // REG_C = quotient
        // REG_D = negative flag
        // REG_E = temporary

        //check div by 0
        //here we store in regC(temporary) the value denominator == zero
        code.emit(Spril.compute("Equal", Spril.REG_B, Spril.ZERO, Spril.REG_C));
        //and based on that we decide if we jump to errorStart or afterError
        //creating the necessary jump addressees
        int branchError = code.emit(Spril.branch(Spril.REG_C, Spril.abs(-1)));
        int jumpAfterError = code.emit(Spril.jump(Spril.abs(-1)));

        //this showcases the run time error block that we are gonna send
        int errorStart = code.size();
        code.patch(Spril.branch(Spril.REG_C, Spril.abs(errorStart)), branchError);
        //in immValue we are gonna send a fixed value that will represent our type of error
        code.emit(Spril.load(Spril.immValue(DIVISION_BY_ZERO_ERROR), Spril.REG_A));
        code.emit(Spril.writeInstr(Spril.REG_A, Spril.NUMBER_IO));
        //this stops the program
        code.emit(Spril.endProg());

        int afterError = code.size();
        code.patch(Spril.jump(Spril.abs(afterError)), jumpAfterError);

        //we check if both numbers are negative
        //regD contains 0 signaling negative = false that is gonna get flipped to 1 or stay 0
        code.emit(Spril.load(Spril.immValue(0), Spril.REG_D));
        //first if numerator < 0
        code.emit(Spril.compute("Lt", Spril.REG_A, Spril.ZERO, Spril.REG_E));
        //temporary addresses before patching
        int branchNumNegative = code.emit(Spril.branch(Spril.REG_E, Spril.abs(-1)));
        int jumpAfterNumNegative = code.emit(Spril.jump(Spril.abs(-1)));
        //create the block
        int negativeNumStart = code.size();
        code.patch(Spril.branch(Spril.REG_E, Spril.abs(negativeNumStart)), branchNumNegative);
        code.emit(Spril.compute("Sub", Spril.ZERO, Spril.REG_A, Spril.REG_A));
        //update negative flag
        code.emit(Spril.compute("Equal", Spril.REG_D, Spril.ZERO, Spril.REG_D));

        int afterNumNegative = code.size();
        code.patch(Spril.jump(Spril.abs(afterNumNegative)), jumpAfterNumNegative);

        //now if denominator < 0
        code.emit(Spril.compute("Lt", Spril.REG_B, Spril.ZERO, Spril.REG_E));
        //create temporary addresses
        int branchDenomNegative = code.emit(Spril.branch(Spril.REG_E, Spril.abs(-1)));
        int jumpAfterDenomNegative = code.emit(Spril.jump(Spril.abs(-1)));
        //create block
        int negativeDenomStart = code.size();
        code.patch(Spril.branch(Spril.REG_E, Spril.abs(negativeDenomStart)), branchDenomNegative);
        code.emit(Spril.compute("Sub", Spril.ZERO, Spril.REG_B, Spril.REG_B));
        //u[date flag
        code.emit(Spril.compute("Equal", Spril.REG_D, Spril.ZERO, Spril.REG_D));

        int afterDenomNegative = code.size();
        code.patch(Spril.jump(Spril.abs(afterDenomNegative)), jumpAfterDenomNegative);

        //create quotient = 0
        code.emit(Spril.load(Spril.immValue(0), Spril.REG_C));

        //now while loop that subtracts
        int loopStart = code.size();
        //num >= denom
        code.emit(Spril.compute("GtE", Spril.REG_A, Spril.REG_B, Spril.REG_E));
        //create temporary addresses for the cond
        int branchLoopBlock = code.emit(Spril.branch(Spril.REG_E, Spril.abs(-1)));
        int jumpAfterLoop = code.emit(Spril.jump(Spril.abs(-1)));
        //create block
        int blockStart = code.size();
        code.patch(Spril.branch(Spril.REG_E, Spril.abs(blockStart)), branchLoopBlock);
        //subtract from num
        code.emit(Spril.compute("Sub", Spril.REG_A, Spril.REG_B, Spril.REG_A));
        //update quotient
        code.emit(Spril.load(Spril.immValue(1), Spril.REG_E));
        code.emit(Spril.compute("Add", Spril.REG_C, Spril.REG_E, Spril.REG_C));
        //jump back to the loop condition
        code.emit(Spril.jump(Spril.abs(loopStart)));
        // after loop block
        int afterBlock = code.size();
        code.patch(Spril.jump(Spril.abs(afterBlock)), jumpAfterLoop);

        //copy quotient into result - REG_A
        code.emit(Spril.compute("Add", Spril.REG_C, Spril.ZERO, Spril.REG_A));

        //if negative flag is true we make the res negative
        int branchMakeNegative = code.emit(Spril.branch(Spril.REG_D, Spril.abs(-1)));
        int jumpAfterMakeNegative = code.emit(Spril.jump(Spril.abs(-1)));

        // create negative block
        int makeNegativeStart = code.size();
        code.patch(Spril.branch(Spril.REG_D, Spril.abs(makeNegativeStart)), branchMakeNegative);
        // make it negative
        code.emit(Spril.compute("Sub", Spril.ZERO, Spril.REG_A, Spril.REG_A));
        // after make negative block
        int afterMakeNegative = code.size();
        code.patch(Spril.jump(Spril.abs(afterMakeNegative)), jumpAfterMakeNegative);
    }

    private void generateSingleExpr(SingleExprNode singleExprNode) {
        if (!singleExprNode.operator.equals("!")) {
            throw new CodeGeneratorException("Unsupported single operator: "
                                                     + singleExprNode.operator);
        }

        generateExpr(singleExprNode.expression);
        code.emit(Spril.pop(Spril.REG_A));
        code.emit(Spril.compute("Equal", Spril.REG_A, Spril.ZERO, Spril.REG_A));
        code.emit(Spril.push(Spril.REG_A));
    }

    public void findEnumVal (List<StatementNode> statements){
        for (StatementNode statement : statements){
            if(statement instanceof EnumNode e){
                for(int i=0; i<e.enumValues.size();i++){
                    String name = e.enumValues.get(i);
                    enumVal.put(name,i);


                }
            }
            if(statement instanceof BlockNode b){ findEnumVal(b.statements);}
        }
    }
}

