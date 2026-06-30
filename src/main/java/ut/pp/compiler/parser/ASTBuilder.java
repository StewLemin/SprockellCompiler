package ut.pp.compiler.parser;


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
        return visit(ctx.declaration());
    }

    @Override
    public ASTNode visitAssignmentStatement(MyLangParser.AssignmentStatementContext ctx) {
        return visit(ctx.assignment());
    }

    @Override
    public ASTNode visitPrintStatement(MyLangParser.PrintStatementContext ctx) {
        return visit(ctx.print());
    }

    @Override
    public ASTNode visitIfStatement(MyLangParser.IfStatementContext ctx) {
        return visit(ctx.ifElse());
    }

    @Override
    public ASTNode visitWhileStatement(MyLangParser.WhileStatementContext ctx) {
        return visit(ctx.while_());
    }

    @Override
    public ASTNode visitBlockStatement(MyLangParser.BlockStatementContext ctx) {
        return visit(ctx.block());
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
            return new VariableNode(name);
        }

        ExprNode index = (ExprNode) visit(ctx.expr());
        return new ArrayNode(name, index);
    }

    @Override
    public ASTNode visitType(MyLangParser.TypeContext ctx) {
        String baseType = ctx.getChild(0).getText();

        TypeKind kind;
        if (baseType.equals("int")) {
            kind = TypeKind.INT;
        } else if (baseType.equals("bool")) {
            kind = TypeKind.BOOL;
        } else {
            throw new IllegalArgumentException("Unknown type: " + baseType);
        }

        if(ctx.INT() != null) {
            return new TypeNode(kind, Integer.parseInt(ctx.INT().getText()));
        }

        return new TypeNode(kind);
    }


    @Override
    public ASTNode visitIfElse(MyLangParser.IfElseContext ctx) {
        ExprNode condition = (ExprNode) visit(ctx.expr());
        BlockNode thenBlock = (BlockNode) visit(ctx.block(0));

        if(ctx.block().size() == 1) {
            return new IfNode(condition, thenBlock);
        }

        BlockNode elseBlock = (BlockNode) visit(ctx.block(1));
        return new IfNode(condition, thenBlock, elseBlock);
    }

    @Override
    public ASTNode visitWhile(MyLangParser.WhileContext ctx) {
        ExprNode condition = (ExprNode) visit(ctx.expr());
        BlockNode body = (BlockNode) visit(ctx.block());

        return new WhileNode(condition, body);
    }

    @Override
    public ASTNode visitBlock(MyLangParser.BlockContext ctx) {
        List<StatementNode> statements = new ArrayList<>();

        for(MyLangParser.StatementContext statementContext : ctx.statement()) {
            statements.add((StatementNode) visit(statementContext));
        }

        return new BlockNode(statements);
    }

    @Override
    public ASTNode visitAddSubExpr(MyLangParser.AddSubExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitMulExpr(MyLangParser.MulExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitOrExpr(MyLangParser.OrExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitAndExpr(MyLangParser.AndExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitEqualityExpr(MyLangParser.EqualityExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitCompareExpr(MyLangParser.CompareExprContext ctx) {
        ExprNode left = (ExprNode) visit(ctx.expr().get(0));
        String operator = ctx.getChild(1).getText();
        ExprNode right = (ExprNode) visit(ctx.expr().get(1));

        return new DoubleExprNode(left, operator, right);
    }

    @Override
    public ASTNode visitArrayLiteralExpr(MyLangParser.ArrayLiteralExprContext ctx) {
        List<ExprNode> elements = new ArrayList<>();

        for (MyLangParser.ExprContext exprContext : ctx.expr()) {
            elements.add((ExprNode) visit(exprContext));
        }

        return new ArrayLiteralNode(elements);
    }

    @Override
    public ASTNode visitVarExpr(MyLangParser.VarExprContext ctx) {
        return visit(ctx.var());
    }

    @Override
    public ASTNode visitNotExpr(MyLangParser.NotExprContext ctx) {
        return new SingleExprNode(ctx.getChild(0).getText(), (ExprNode) visit(ctx.expr()));
    }

    @Override
    public ASTNode visitIntExpr(MyLangParser.IntExprContext ctx) {
        return new IntNode(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public ASTNode visitBoolExpr(MyLangParser.BoolExprContext ctx) {
        return new BoolNode(ctx.BOOL().getText().equals("TRUE"));
    }

    @Override
    public ASTNode visitParenExpr(MyLangParser.ParenExprContext ctx) {
        return visit(ctx.expr());
    }
}

