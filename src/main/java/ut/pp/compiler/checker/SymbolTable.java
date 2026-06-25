package ut.pp.compiler.checker;

import ut.pp.ast.type.TypeNode;

public class SymbolTable {
    public void enterScope() {
    }

    public void exitScope() {
    }

    public void declare(String name, TypeNode type, boolean initialized) {
    }

    public boolean isDeclaredInCurrScope(String name) {
        return false;
    }

    public Symbol lookup(String name) {
        return null;
    }

    public void markIntiliazed() {
    }
}
