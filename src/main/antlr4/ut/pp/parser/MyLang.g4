grammar MyLang;

hellos : HELLO* EOF; 

HELLO : 'Hello' ;

COMMENT : '//' (~('\n'))* -> skip;
WS : [ \n\t\r]+ -> skip;
