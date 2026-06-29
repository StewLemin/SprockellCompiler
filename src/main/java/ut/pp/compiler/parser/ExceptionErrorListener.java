package ut.pp.compiler.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ExceptionErrorListener extends BaseErrorListener {
   public static final ExceptionErrorListener listener = new ExceptionErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
        throw new ParseException("syntax error at line " + line + " and col " + charPositionInLine + " - " + msg);
    }

}
