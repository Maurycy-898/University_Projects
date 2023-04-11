%{
#include <stdio.h>

int yylex();
int yywrap();

int words = 0;
int lines = 0;
%}


%%
^[ \t]*\n* ; /* delete tabs whitespaces and new lines at the beggining of the line */
[ \t]+$   ; /* delete tabs and whitespaces at the end of the line */
[\n]+  { printf("\n"); words++; lines++; } /* add words and lines when new line */
[ \t]+ { printf(" "); words++; } /* add words when tab or spaces */
%%


int yywrap() {
    printf("\n\n >number of words: %d", words);
    printf("\n >number of lines: %d\n", lines);
    return 1;
}

int main() {
    return yylex();
}
