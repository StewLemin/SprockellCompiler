//// first program
//int x = 5;
//int y = x + 3;
//bool bigger = y > 5;
//
//if (bigger) {
//    print y;
//}
//
//while (x > 0) {
//    print x;
//    x = x - 1;
//}

grammar MihaitaLanguage;

program:
    statement* EOF
    ;

statement:
    declaration ';'
    |assignment ';'
    |print ';'
    |ifElse ';'
    |while ';'
    |block
    ;

declaration:
    type ID ('=' expr)?
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
    'int' ('[' INT ']')?
    |'bool' ('[' INT ']')?
   ;

ID:
    [a-zA-Z]+[_0-9a-zA-Z']*
    ;

BOOL:
    'TRUE'
    |'FALSE'
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

expr:
    INT
    |BOOL
    |ID
    |'!' expr
    |expr ('+' | '-')  expr
    |expr '*' expr
    |expr ('==' | '!=') expr
    |expr ('<' | '<=' | '>=' | '>') expr
    |'(' expr ')'
    |ID'[' expr ']'
    |expr '&&' expr
    |expr '||' expr
    |'[' (expr(','expr)*)? ']'//lets your language take input like x = [1,2,3,3,2,4,2,2,1,2]
    ;


COMMENT:
    '//' ~[\r\n]* -> skip
    ;

WHITESPACE:
    [ \r\n\t]+ -> skip
    ;

