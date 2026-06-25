package ut.pp.compiler.checker;

import ut.pp.ast.ProgramNode;

import java.util.List;

import ut.pp.ast.StatementNode;


public class TypeChecker {

    private final ProgramNode root;

    public TypeChecker (ProgramNode root){
        this.root = root;
    }

    public List<String> check(){
        for(StatementNode s :root.statements){
            checkStmt(s);
        }
    }








}
