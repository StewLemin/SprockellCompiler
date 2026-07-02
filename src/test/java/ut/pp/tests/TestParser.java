package ut.pp.tests;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import ut.pp.ast.ExprNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.concurrency.*;
import ut.pp.ast.expr.ArrayLiteralNode;
import ut.pp.ast.expr.BoolNode;
import ut.pp.ast.expr.DoubleExprNode;
import ut.pp.ast.expr.SingleExprNode;
import ut.pp.ast.statement.*;
import ut.pp.compiler.parser.ASTBuilder;
import ut.pp.compiler.parser.ParseException;
import ut.pp.compiler.parser.ParserRunner;
import ut.pp.parser.MyLangLexer;
import ut.pp.parser.MyLangParser;
import ut.pp.ast.ProgramNode;

import static org.junit.jupiter.api.Assertions.*;

//ADDED BY TERZA INITIALY TO BE CHANGED!!!!!!!!!!!!1
import ut.pp.ast.type.TypeKind;
import ut.pp.ast.variable.ArrayNode;

import java.nio.file.Path;


public class TestParser {
    private ProgramNode buildAst(String input){
        return ParserRunner.parseString(input);
    }



    @Test
    public void parseSimpleAddition(){
        ProgramNode root = buildAst("int x = 1 + 2;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        DoubleExprNode add =(DoubleExprNode) decl.value;
        assertEquals("+",add.operator);
    }

    @Test
    public void multBindsTightherThanAdd(){
        ProgramNode root = buildAst("x = 2 + 3 * 4;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode add = (DoubleExprNode) stmt.value;
        DoubleExprNode mult = (DoubleExprNode) add.expr2;
        assertEquals("+",add.operator);
        assertEquals("*",mult.operator);
    }

    @Test
    public void paransOverrideBindingRules(){
        ProgramNode root = buildAst("x = (2 + 3) * 4;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode mult = (DoubleExprNode) stmt.value;
        assertEquals("*", mult.operator);
        DoubleExprNode add = (DoubleExprNode) mult.expr1;
        assertEquals("+", add.operator);
    }

    @Test
    public void leftAssociativeSameOrderOperations(){
        ProgramNode root = buildAst("x = 10 - 3 - 2;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode first = (DoubleExprNode) stmt.value;
        assertEquals("-", first.operator);
        DoubleExprNode second = (DoubleExprNode) first.expr1;
        assertEquals("-", second.operator);
    }

    @Test
    public void comparisonAfterArithmetic(){
        ProgramNode root = buildAst("x = 1 + 2 > 3;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode cmp = (DoubleExprNode) stmt.value;
        assertEquals(">", cmp.operator);
        DoubleExprNode add = (DoubleExprNode) cmp.expr1;
        assertEquals("+", add.operator);
    }


    @Test
    public void andBindsTighterThanOr(){
        ProgramNode root = buildAst("b = TRUE && FALSE || TRUE;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode or = (DoubleExprNode) stmt.value;
        assertEquals("||", or.operator);
        assertEquals("&&", ((DoubleExprNode) or.expr1).operator);
    }


    @Test
    public void eqBindsTighterThanAnd(){
        ProgramNode root = buildAst("b = 1 == 2 && TRUE;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode and = (DoubleExprNode) stmt.value;
        assertEquals("&&", and.operator);
        assertEquals("==", ((DoubleExprNode) and.expr1).operator);
    }


    @Test
    public void compBindsTighterThanEq(){
        ProgramNode root = buildAst("b = 1 < 2 == TRUE;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode eq = (DoubleExprNode) stmt.value;
        assertEquals("==", eq.operator);
        assertEquals("<", ((DoubleExprNode) eq.expr1).operator);
    }

    @Test
    public void notBindsBeforeAllOthers(){
        ProgramNode root = buildAst("b = !TRUE && FALSE;");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        DoubleExprNode and = (DoubleExprNode) stmt.value;
        assertEquals("&&", and.operator);
        SingleExprNode not = (SingleExprNode) and.expr1;
        assertEquals("!", not.operator);
    }

    @Test
    public void notAppliesToParans(){
        ProgramNode root = buildAst("b = !(TRUE || FALSE);");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        SingleExprNode booleanArithm = (SingleExprNode) stmt.value;
        assertInstanceOf(DoubleExprNode.class,booleanArithm.expression);
    }

    @Test
    public void boolUsedAsIfCondition(){
        ProgramNode root = buildAst("if (TRUE && FALSE) { print 1; }");
        assertInstanceOf(IfNode.class, root.statements.get(0));
    }

    @Test
    public void rejectsOnSyntaxError(){
        assertThrows(ParseException.class, () -> buildAst("int x = 2"));
    }

    @Test
    public void rejectsBraceLessIfOrWhileBlock(){
        assertThrows(ParseException.class, () -> buildAst("if (x) print 1"));
    }

    @Test
    public void rejectsMissingCondition(){
        assertThrows(ParseException.class, () -> buildAst("if () { print 1; }"));
    }

    @Test
    public void elseAttachesToSamBlockIf(){
        ProgramNode root = buildAst("if (a) { if (b) { print 1; } else { print 2; } }");
        IfNode outer = (IfNode) root.statements.get(0);
        assertNull(outer.elseBlock);
        IfNode inner = (IfNode) outer.thenBlock.statements.get(0);
        assertNotNull(inner.elseBlock);
    }

    @Test
    public void emptyBlockParses(){
        ProgramNode root = buildAst("while (TRUE) {}");
        WhileNode node = (WhileNode) root.statements.get(0);
        assertEquals(0, node.block.statements.size());
    }

    @Test
    public void arrayLiteralParses(){
        ProgramNode root = buildAst("x = [1, 2, 3];");
        AssignmentNode stmt = (AssignmentNode) root.statements.get(0);
        assertInstanceOf(ArrayLiteralNode.class, stmt.value);
    }

    @Test
    public void arrayIndexingParses(){
        ProgramNode root = buildAst("x = a[2];");
    }

    @Test
    public void arrayDeclarationParses(){
        ProgramNode root = buildAst("int[5] a;");
        assertInstanceOf(DeclarationNode.class, root.statements.get(0));
    }

    @Test
    public void forkParsesForkNode(){
        ProgramNode root = buildAst("fork { print 21; }");
        assertInstanceOf(ForkNode.class, root.statements.get(0));
    }

    @Test
    public void forkParsesMultipleStatementsInBlock(){
        ProgramNode root = buildAst("fork { print 16; int x = 23; }");
        ForkNode fork = (ForkNode) root.statements.get(0);
        assertEquals(2, fork.body.statements.size());
    }

    @Test
    public void joinParsesToJoinNode(){
        ProgramNode root = buildAst("join;");
        assertInstanceOf(JoinNode.class, root.statements.get(0));
    }

    @Test
    public void multipleForksThenJoin(){
        ProgramNode root = buildAst("fork { print 273; } fork { x = 2 * 999; } join;");
        assertInstanceOf(ForkNode.class, root.statements.get(0));
        assertInstanceOf(ForkNode.class, root.statements.get(1));
        assertInstanceOf(JoinNode.class, root.statements.get(2));
    }

    @Test
    public void nestedForkNodesParse(){
        ProgramNode root = buildAst("fork { fork { print 61; } }");
        ForkNode outer = (ForkNode) root.statements.get(0);
        assertInstanceOf(ForkNode.class, outer.body.statements.get(0));
    }

    @Test
    public void lockDeclParsesToLockNode(){
        ProgramNode root = buildAst("lock bankLock;");
        assertInstanceOf(LockNode.class, root.statements.get(0));
    }

    @Test
    public void acquireLockParsesToLockOpAQ(){
        ProgramNode root = buildAst("acquire(auraLock);");
        LockOpNode op = (LockOpNode) root.statements.get(0);
        assertEquals(LockOp.ACQUIRE, op.operation);
    }

    @Test
    public void releaseParsesToLockOpREL(){
        ProgramNode root = buildAst("release(auraLock);");
        LockOpNode op = (LockOpNode) root.statements.get(0);
        assertEquals(LockOp.RELEASE, op.operation);
    }

    @Test
    public void sharedDeclarationHasSharedFlag(){
        ProgramNode root = buildAst("shared int balance = 0;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        assertTrue(decl.isShared);
    }

    @Test
    public void nonSharedDeclarationNoSharedFlag(){
        ProgramNode root = buildAst("int x = 0;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        assertFalse(decl.isShared);
    }

    @Test
    public void commentsAreIgnored(){
        ProgramNode root = buildAst("""
            int x = 1; 
            // this is a comment;
            print x;
            """);
        assertEquals(2, root.statements.size());
        assertInstanceOf(DeclarationNode.class, root.statements.get(0));
        assertInstanceOf(PrintNode.class, root.statements.get(1));
    }

    @Test
    public void enumDeclarationParses(){
        ProgramNode root = buildAst("enum Color { RED, GREEN, BLUE };");
        EnumNode enumNode = (EnumNode) root.statements.get(0);
        assertEquals("Color", enumNode.name);
        assertEquals(3, enumNode.enumValues.size());
        assertEquals("RED", enumNode.enumValues.get(0));
        assertEquals("GREEN", enumNode.enumValues.get(1));
        assertEquals("BLUE", enumNode.enumValues.get(2));
    }

    @Test
    public void enumVariableDeclarationParsesAsEnumType(){
        ProgramNode root = buildAst("Color c = GREEN;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        assertEquals(TypeKind.ENUM, decl.type.kind);
        assertEquals("Color", decl.type.typeName);
    }

    @Test
    public void divisionParses(){
        ProgramNode root = buildAst("int x = 10 / 2;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        DoubleExprNode div = (DoubleExprNode) decl.value;
        assertEquals("/", div.operator);
    }

    @Test
    public void notEqualsParses(){
        ProgramNode root = buildAst("bool b = 1 != 2;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        DoubleExprNode neq = (DoubleExprNode) decl.value;
        assertEquals("!=", neq.operator);
    }

    @Test
    public void gteAndLteParse(){
        ProgramNode root = buildAst("bool a = 1 <= 2; bool b = 3 >= 2;");
        DeclarationNode first = (DeclarationNode) root.statements.get(0);
        DeclarationNode second = (DeclarationNode) root.statements.get(1);
        assertEquals("<=", ((DoubleExprNode) first.value).operator);
        assertEquals(">=", ((DoubleExprNode) second.value).operator);
    }

    @Test
    public void orBooleanOpParses(){
        ProgramNode root =  buildAst("bool f = TRUE || FALSE;");
        DeclarationNode decl = (DeclarationNode) root.statements.get(0);
        DoubleExprNode or = (DoubleExprNode) decl.value;
        assertEquals("||",or.operator);
    }

    @Test
    public void arrayElementAssignmentParses(){
        ProgramNode root = buildAst("a[1] = 7;");
        AssignmentNode assignment = (AssignmentNode) root.statements.get(0);
        assertInstanceOf(ArrayNode.class, assignment.target);
    }

    @Test
    public void emptyArrayLiteralParses(){
        ProgramNode root = buildAst("x = [];");
        AssignmentNode assignment = (AssignmentNode) root.statements.get(0);
        assertInstanceOf(ArrayLiteralNode.class, assignment.value);
    }

    @Test
    public void rejectsBadEnumSyntax(){
        assertThrows(ParseException.class, () -> buildAst("enum Color { RED, };"));
    }

    @Test
    public void rejectsBadLockSyntax(){
        assertThrows(ParseException.class, () -> buildAst("acquire bankLock;"));
    }

    @Test
    public void rejectsBadForkSyntax(){
        assertThrows(ParseException.class, () -> buildAst("fork print 1;"));
    }

    @Test
    public void rejectsBadArrayDeclaration(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile( Path.of("testFiles/syntax-error/test_bad_array_declaration.mylang")));
    }

    @Test
    public void rejectsBadEnumSyntaxFile(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_bad_enum_syntax.mylang")));
    }

    @Test
    public void rejectsForkWithoutBlock(){
         assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_fork_without_block.mylang")));
    }

    @Test
    public void rejectsIfWithoutBlock(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_if_without_block.mylang")));
    }

    @Test
    public void rejectsMissingExpressionSubExpr(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_missing_expression_subexpr.mylang")));
    }

    @Test
    public void rejectsMissingSemicolog(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_missing_semicolon.mylang")));
    }

    @Test
    public void rejectsWrongLockOperationSyntax(){
        assertThrows(ParseException.class,()->ParserRunner.parseFile(Path.of("testFiles/syntax-error/test_wrong_lock_operation_syntax.mylang")));
    }



}
