package ut.pp.tests;

import ut.pp.ast.ProgramNode;
import ut.pp.compiler.checker.Checker;
import ut.pp.compiler.checker.CheckerException;

import org.junit.jupiter.api.Test;
import ut.pp.compiler.parser.ParserRunner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestChecker {
    private void check(String input) {
        ProgramNode root = ParserRunner.parse(input);
        new Checker().check(root);
    }
    @Test
    public void acceptsExprIntDeclaration() {
        assertDoesNotThrow(() -> check("int x = 1 + 2;"));
    }

    @Test
    public void acceptsExprBoolDeclaration() {
        assertDoesNotThrow(() -> check("bool b = TRUE && FALSE;"));
    }

    @Test
    public void rejectsIntDeclaredWithBool() {
        assertThrows(CheckerException.class, () -> check("int x = FALSE;"));
    }

    @Test
    public void rejectsBoolDeclaredWithInt() {
        assertThrows(CheckerException.class, () -> check("bool b = 5;"));
    }

    @Test
    public void rejectsRedeclarationInSameScope() {
        assertThrows(CheckerException.class, () -> check("int x = 1; int x = 2;"));
    }

    @Test
    public void rejectsUndeclaredVariableUse() {
        assertThrows(CheckerException.class, () -> check("int x = y;"));
    }

    @Test
    public void rejectsAssignmentToUndeclared() {
        assertThrows(CheckerException.class, () -> check("x = 5;"));
    }

    @Test
    public void rejectsAssignmentTypeMismatch() {
        assertThrows(CheckerException.class, () -> check("int x = 1; x = TRUE;"));
    }

    @Test
    public void acceptsValidAssignment() {
        assertDoesNotThrow(() -> check("int x = 1; x = 2;"));
    }

    @Test
    public void rejectsArithmeticOnBool() {
        assertThrows(CheckerException.class, () -> check("int x = 1 + TRUE;"));
    }

    @Test
    public void rejectsComparisonOnBool() {
        assertThrows(CheckerException.class, () -> check("bool b = TRUE > FALSE;"));
    }

    @Test
    public void rejectsAndOnInt() {
        assertThrows(CheckerException.class, () -> check("bool b = 1 && 2;"));
    }

    @Test
    public void rejectsEqualityOnDiffTypes() {
        assertThrows(CheckerException.class, () -> check("bool b = 1 == TRUE;"));
    }

    @Test
    public void acceptsEqualityOnSameType() {
        assertDoesNotThrow(() -> check("bool b = 1 == 2;"));
    }

    @Test
    public void rejectsNotOnInt() {
        assertThrows(CheckerException.class, () -> check("bool b = !5;"));
    }

    @Test
    public void rejectsNonBoolIfCondition() {
        assertThrows(CheckerException.class, () -> check("if (5) { print 1; }"));
    }

    @Test
    public void rejectsNonBoolWhileCondition() {
        assertThrows(CheckerException.class, () -> check("while (5) { print 1; }"));
    }

    @Test
    public void acceptsBoolIfCondition() {
        assertDoesNotThrow(() -> check("int x = 1; if (x > 0) { print x; }"));
    }

    @Test
    public void acceptsRedeclarationInDifferentScopes() {
        assertDoesNotThrow(() -> check("int x = 1; if (TRUE) { int x = 2; }"));
    }

    @Test
    public void rejectsVariableOutsideItsScope() {
        assertThrows(CheckerException.class, () -> check("if (TRUE) { int y = 1; } print y;"));
    }

    @Test
    public void acceptsOuterVariableInInnerScope() {
        assertDoesNotThrow(() -> check("int x = 1; if (TRUE) { print x; }"));
    }

    @Test
    public void acceptsUseAfterInitialization() {
        assertDoesNotThrow(() -> check("int x; x = 5; print x;"));
    }

    @Test
    public void acceptsSameTypeArrayLiteral() {
        assertDoesNotThrow(() -> check("int[3] a = [1, 2, 3];"));
    }

    @Test
    public void rejectsMixedTypeArrayLiteral() {
        assertThrows(CheckerException.class, () -> check("int[2] a = [1, TRUE];"));
    }

    @Test
    public void rejectsNonIntArrayIndex() {
        assertThrows(CheckerException.class,
                () -> check("int[3] a = [1,2,3]; int x = a[TRUE];"));
    }

    @Test
    public void rejectsIndexingNonArray() {
        assertThrows(CheckerException.class, () -> check("int x = 1; int y = x[0];"));
    }

    @Test
    public void rejectsNestedArray(){
        assertThrows(CheckerException.class, () -> check("int[3] x; x = [[1,2],32];"));
    }

    @Test
    public void rejectsForkInsideConditionalInsideLoop(){
        assertThrows(CheckerException.class,() -> check("bool x = TRUE; while(x){ if(x) { fork { x = FALSE;}}}"));
    }

    @Test
    public void rejectsForkInsideLoop(){
        assertThrows(CheckerException.class, () -> check("bool x = TRUE; int balance = 0; while(x){fork{balance = balance + 100;}}"));
    }

    @Test
    public void acceptsNestedForks(){
        assertDoesNotThrow(() -> check("fork{fork{int x = 21;}}"));
    }

    @Test
    public void acceptsAcquiringDeclaredLock(){
        assertDoesNotThrow(() -> check("lock baustela; acquire(baustela);"));
    }

    @Test
    public void rejectsNestedForkInsideLoop(){
        assertThrows(CheckerException.class, () -> check("fork{ while(TRUE){fork{int x = 1;}}}"));
    }

    @Test
    public void rejectsOperationUndeclaredLock(){
        assertThrows(CheckerException.class,() -> check("int x = 1; release(baustela);"));
    }

    @Test
    public void rejectsLockOperationOnOtherType(){
        assertThrows(CheckerException.class,() -> check("int x = 1; acquire(x);"));
    }

    @Test
    public void rejectsRedeclaration(){
        assertThrows(CheckerException.class,() -> check("lock aura; lock aura;"));
    }

    @Test
    public void rejectLockWithSameIdentifierAsVar(){
        assertThrows(CheckerException.class,() -> check("int terzic; lock terzic;"));
    }

}
