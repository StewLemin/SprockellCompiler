package ut.pp.compiler.checker;

import java.util.Objects;
import ut.pp.ast.type.TypeKind;
import ut.pp.ast.type.TypeNode;

public final class CheckerUtils {
    private CheckerUtils() {
    }

    public static boolean sameType(TypeNode first, TypeNode second) {
        if (first == null || second == null) {
            return false;
        }

        if (first.kind != second.kind) {
            return false;
        }

        if (first.isArray() != second.isArray()) {
            return false;
        }

        if (first.isArray()) {
            return Objects.equals(first.arrayLength, second.arrayLength);
        }

        return true;
    }

    public static boolean isInt(TypeNode type) {
        return type != null && !type.isArray() && type.kind == TypeKind.INT;
    }

    public static boolean isBool(TypeNode type) {
        return type != null && !type.isArray() && type.kind == TypeKind.BOOL;
    }

    public static TypeNode intType() {
        return new TypeNode(TypeKind.INT);
    }

    public static TypeNode boolType() {
        return new TypeNode(TypeKind.BOOL);
    }

    public static TypeNode elementType(TypeNode arrayType) {
        if (arrayType == null || !arrayType.isArray()) {
            return null;
        }

        return new TypeNode(arrayType.kind);
    }

    public static String toString(TypeNode type) {
        if (type == null) {
            return "<unknown>";
        }

        String base;
        if (type.kind == TypeKind.INT) {
            base = "int";
        } else if (type.kind == TypeKind.BOOL) {
            base = "bool";
        } else {
            base = "<unknown>";
        }

        if (type.isArray()) {
            return base + "[" + type.arrayLength + "]";
        }

        return base;
    }
}
