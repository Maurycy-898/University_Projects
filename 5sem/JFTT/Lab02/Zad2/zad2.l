%{
#include <stdio.h>
int yylex();
int yywrap();
%}


%%
["]([^"\n]|\\\n)*["] ECHO; // exception - allow to use "#" in Strings
['][^'\n]*['] ECHO; // exception - allow to use "#" in Strings 

[=][ \t]*["]{3}(["]{0,2}[^"]+)*["]{3} ECHO; // exception - allow triple quotes when assigninig value
[=][ \t]*[']{3}([']{0,2}[^']+)*[']{3} ECHO; // exception - allow triple quotes when assigninig value

"print("[ \t\n]*["]{3}(["]{0,2}[^"]+)*["]{3}[ \t\n]*[)] ECHO; // exception - allow triple quotes when printing valu
"print("[ \t\n]*[']{3}([']{0,2}[^']+)*[']{3}[ \t\n]*[)] ECHO; // exception - allow triple quotes when printing value

[#](.|\\\n)*  ; // to delete short comments 

["]{3}(["]{0,2}[^"]+)*["]{3} ; // to delete long comments
[']{3}([']{0,2}[^']+)*[']{3} ; // to delete long comments
%%


int yywrap() { return 1; }
int main() { return yylex(); }
