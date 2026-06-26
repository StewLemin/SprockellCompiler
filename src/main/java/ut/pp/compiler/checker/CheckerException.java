package ut.pp.compiler.checker;

import java.util.List;

public class CheckerException extends RuntimeException{
    private final List<String> errors;

    public CheckerException(List<String> errors) {
        super(String.join(System.lineSeparator(), errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
