package ut.pp.compiler;


import ut.pp.ast.ASTNode;
import ut.pp.ast.ProgramNode;
import ut.pp.ast.StatementNode;
import ut.pp.ast.expr.*;
import ut.pp.ast.statement.*;
import ut.pp.ast.type.*;
import ut.pp.ast.variable.*;
import ut.pp.parser.MyLangBaseVisitor;
import ut.pp.parser.MyLangParser;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MyLangBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(MyLangParser.ProgramContext ctx) {
        List<StatementNode> statements = new ArrayList<>();

        for(MyLangParser.StatementContext statementContext : ctx.statement()) {
            statements.add(StatementNode) visit(statementContext);
        }

        return new ProgramNode(statements);
    }

    @Override
    public ASTNode visitDeclarationStatement(MyLangParser.DeclarationStatementContext ctx) {
        return super.visitDeclarationStatement(ctx);
    }

    @Override
    public ASTNode visitAssignmentStatement(MyLangParser.AssignmentStatementContext ctx) {
        return super.visitAssignmentStatement(ctx);
    }

    @Override
    public ASTNode visitPrintStatement(MyLangParser.PrintStatementContext ctx) {
        return super.visitPrintStatement(ctx);
    }

    @Override
    public ASTNode visitIfStatement(MyLangParser.IfStatementContext ctx) {
        return super.visitIfStatement(ctx);
    }

    @Override
    public ASTNode visitWhileStatement(MyLangParser.WhileStatementContext ctx) {
        return super.visitWhileStatement(ctx);
    }

    @Override
    public ASTNode visitBlockStatement(MyLangParser.BlockStatementContext ctx) {
        return super.visitBlockStatement(ctx);
    }

    @Override
    public ASTNode visitDeclaration(MyLangParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx);
    }

    @Override
    public ASTNode visitVar(MyLangParser.VarContext ctx) {
        return super.visitVar(ctx);
    }

    @Override
    public ASTNode visitAssignment(MyLangParser.AssignmentContext ctx) {
        return super.visitAssignment(ctx);
    }

    @Override
    public ASTNode visitType(MyLangParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public ASTNode visitIfElse(MyLangParser.IfElseContext ctx) {
        return super.visitIfElse(ctx);
    }

    @Override
    public ASTNode visitWhile(MyLangParser.WhileContext ctx) {
        return super.visitWhile(ctx);
    }

    @Override
    public ASTNode visitBlock(MyLangParser.BlockContext ctx) {
        return super.visitBlock(ctx);
    }

    @Override
    public ASTNode visitPrint(MyLangParser.PrintContext ctx) {
        return super.visitPrint(ctx);
    }

    @Override
    public ASTNode visitMulExpr(MyLangParser.MulExprContext ctx) {
        return super.visitMulExpr(ctx);
    }

    @Override
    public ASTNode visitAndExpr(MyLangParser.AndExprContext ctx) {
        return super.visitAndExpr(ctx);
    }

    @Override
    public ASTNode visitBoolExpr(MyLangParser.BoolExprContext ctx) {
        return super.visitBoolExpr(ctx);
    }

    @Override
    public ASTNode visitEqualityExpr(MyLangParser.EqualityExprContext ctx) {
        return super.visitEqualityExpr(ctx);
    }

    @Override
    public ASTNode visitCompareExpr(MyLangParser.CompareExprContext ctx) {
        return super.visitCompareExpr(ctx);
    }

    @Override
    public ASTNode visitArrayLiteralExpr(MyLangParser.ArrayLiteralExprContext ctx) {
        return super.visitArrayLiteralExpr(ctx);
    }

    @Override
    public ASTNode visitVarExpr(MyLangParser.VarExprContext ctx) {
        return super.visitVarExpr(ctx);
    }

    @Override
    public ASTNode visitNotExpr(MyLangParser.NotExprContext ctx) {
        return super.visitNotExpr(ctx);
    }

    @Override
    public ASTNode visitIntExpr(MyLangParser.IntExprContext ctx) {
        return super.visitIntExpr(ctx);
    }

    @Override
    public ASTNode visitParenExpr(MyLangParser.ParenExprContext ctx) {
        return super.visitParenExpr(ctx);
    }

    @Override
    public ASTNode visitAddSubExpr(MyLangParser.AddSubExprContext ctx) {
        return super.visitAddSubExpr(ctx);
    }

    @Override
    public ASTNode visitOrExpr(MyLangParser.OrExprContext ctx) {
        return super.visitOrExpr(ctx);
    }
}

