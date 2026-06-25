package ut.pp.compiler;


import ut.pp.ast.ASTNode;
import ut.pp.ast.ExprNode;
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
            statements.add((StatementNode) visit(statementContext));
        }

        return new ProgramNode(statements);
    }

    @Override
    public ASTNode visitDeclarationStatement(MyLangParser.DeclarationStatementContext ctx) {
        return (StatementNode) visit(ctx.declaration());
    }

    @Override
    public ASTNode visitAssignmentStatement(MyLangParser.AssignmentStatementContext ctx) {
        return (StatementNode) visit(ctx.assignment());
    }

    @Override
    public ASTNode visitPrintStatement(MyLangParser.PrintStatementContext ctx) {
        return (StatementNode) visit(ctx.print());
    }

    @Override
    public ASTNode visitIfStatement(MyLangParser.IfStatementContext ctx) {
        return (StatementNode) visit(ctx.ifElse());
    }

    @Override
    public ASTNode visitWhileStatement(MyLangParser.WhileStatementContext ctx) {
        return (StatementNode) visit(ctx.while_());
    }

    @Override
    public ASTNode visitBlockStatement(MyLangParser.BlockStatementContext ctx) {
        return (StatementNode) visit(ctx.block());
    }

    @Override
    public ASTNode visitDeclaration(MyLangParser.DeclarationContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        String name = ctx.ID().getText();

        if (ctx.expr() == null) {
            return new DeclarationNode(type, name);
        }

        ExprNode initializer = (ExprNode) visit(ctx.expr());
        return new DeclarationNode(type, name, initializer);
    }

    @Override
    public ASTNode visitAssignment(MyLangParser.AssignmentContext ctx) {
        VarNode target = (VarNode) visit(ctx.var());
        ExprNode value = (ExprNode) visit(ctx.expr());

        return new AssignmentNode(target, value);
    }

    @Override
    public ASTNode visitPrint(MyLangParser.PrintContext ctx) {
        ExprNode expression = (ExprNode) visit(ctx.expr());
        return new PrintNode(expression);
    }

    @Override
    public ASTNode visitVar(MyLangParser.VarContext ctx) {
        String name = ctx.ID().getText();

        if(ctx.expr() == null) {
            return  new VariableNode(name);
        }

        ExprNode index = (ExprNode) visit(ctx.expr());
        return new ArrayNode(name, index);
    }

    //TODO
    @Override
    public ASTNode visitType(MyLangParser.TypeContext ctx) {
        return super.visitType(ctx);
    }


    @Override
    public ASTNode visitIfElse(MyLangParser.IfElseContext ctx) {
        ExprNode condition = (ExprNode) visit(ctx.expr());
        BlockNode thenBlock = (BlockNode) visit(ctx.block(0));

        if(ctx.block().size() == 1) {
            return new IfNode(condition, thenBlock);
        }

        BlockNode elseBlock = (BlockNode) visit((ctx.block(1)));
        return new IfNode(condition, thenBlock, elseBlock);
    }

    //TODO
    @Override
    public ASTNode visitWhile(MyLangParser.WhileContext ctx) {
        return super.visitWhile(ctx);
    }

    @Override
    public ASTNode visitBlock(MyLangParser.BlockContext ctx) {
        List<StatementNode> statements = new ArrayList<>();

        for(MyLangParser.StatementContext statementContext : ctx.statement()) {
            statements.add((StatementNode) visit(statementContext));
        }

        return new BlockNode(statements);
    }

    //TODO
    @Override
    public ASTNode visitMulExpr(MyLangParser.MulExprContext ctx) {
        return super.visitMulExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitAndExpr(MyLangParser.AndExprContext ctx) {
        return super.visitAndExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitBoolExpr(MyLangParser.BoolExprContext ctx) {
        return super.visitBoolExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitEqualityExpr(MyLangParser.EqualityExprContext ctx) {
        return super.visitEqualityExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitCompareExpr(MyLangParser.CompareExprContext ctx) {
        return super.visitCompareExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitArrayLiteralExpr(MyLangParser.ArrayLiteralExprContext ctx) {
        return super.visitArrayLiteralExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitVarExpr(MyLangParser.VarExprContext ctx) {
        return super.visitVarExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitNotExpr(MyLangParser.NotExprContext ctx) {
        return super.visitNotExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitIntExpr(MyLangParser.IntExprContext ctx) {
        return super.visitIntExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitParenExpr(MyLangParser.ParenExprContext ctx) {
        return super.visitParenExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitAddSubExpr(MyLangParser.AddSubExprContext ctx) {
        return super.visitAddSubExpr(ctx);
    }

    //TODO
    @Override
    public ASTNode visitOrExpr(MyLangParser.OrExprContext ctx) {
        return super.visitOrExpr(ctx);
    }
}

