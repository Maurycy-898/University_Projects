%{
#include <stdio.h>

int yylex();
int yywrap();
%}



%%
[/]{3}.* ECHO; // exception - dont delete "///" type comments 
[/]{2}[!].* ECHO; // exception - dont delete "//!" type comments 

[/][*]{2}[^*]*[*]+([^*/][^*]*[*]+)*[/] ECHO; // exception - dont delete "/**" type comments
[/][*][!][^*]*[*]+([^*/][^*]*[*]+)*[/] ECHO; // exception - dont delete "/*!" type comments

["]([^"\n]|[\\\"])*["] ECHO; // exception - dont delete comments in Strings(quotes)

[/][\\\n]*[/](.|\\\n)* ; // delete "//" type comments 
[/][\\\n]*[*][^*]*[*]+([^*/][^*]*[*]+)*[\\\n]*[/] ; // delete "/*" type comments

 /* using " [\\\n]* " to deal with escape sequences in comments */
%%


int yywrap() { return 1; }
int main() { return yylex(); }
