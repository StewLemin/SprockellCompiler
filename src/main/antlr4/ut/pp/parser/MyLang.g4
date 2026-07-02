grammar MyLang;

program:
    statement* EOF
    ;

statement:
    declaration ';' # DeclarationStatement
    |assignment ';' # AssignmentStatement
    |print ';' # PrintStatement
    |ifElse # IfStatement
    |while # WhileStatement
    |block # BlockStatement
    |lockDecl ';' #LockDeclStatement
    |lockOp ';'#LockOpStatement
    |fork #ForkStatement
    |join ';' #JoinStatement
    |enumType ';' # EnumStatement
    ;


lockDecl:
    'lock' ID
    ;

lockOp:
    'acquire''('ID')'
    |'release''('ID')'
    ;

fork:
    'fork' block
    ;

join:
    'join'
    ;

declaration:
    (SHARED)? type ID ('=' expr)?
    ;

SHARED:
    'shared'
    ;

var:
    ID
    |ID '['expr']'
    ;

assignment:
    var '=' expr
    ;

// dont forget to make it modular
type:
    ('int' | 'bool' | ID) ('[' INT ']')?
    ;

BOOL:
    'TRUE'
    |'FALSE'
    ;

ID:
    [a-zA-Z]+[_0-9a-zA-Z']*
    ;

INT:
    '0'
    |[1-9][0-9]*
    ;

ifElse:
    'if' '(' expr ')' block ('else' block)?
    ;

while:
    'while' '(' expr ')' block
    ;

block:
    '{' statement* '}'
    ;

print:
    'print' expr
    ;

enumType:
    'enum' ID '{' ID (',' ID)* '}'
    ;

expr:
    '!' expr # NotExpr
    |expr ('*' | '/') expr # MulExpr
    |expr ('+' | '-')  expr # AddSubExpr
    |expr ('<' | '<=' | '>=' | '>') expr # CompareExpr
    |expr ('==' | '!=') expr # EqualityExpr
    |expr '&&' expr # AndExpr
    |expr '||' expr # OrExpr
    |'(' expr ')' # ParenExpr
    |'[' (expr(','expr)*)? ']' # ArrayLiteralExpr
    |INT # IntExpr
    |BOOL # BoolExpr
    |var # VarExpr
    ;


COMMENT : '//' (~('\n'))* -> skip;
WS : [ \n\t\r]+ -> skip;
