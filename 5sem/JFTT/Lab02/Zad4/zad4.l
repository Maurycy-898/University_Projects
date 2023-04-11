%{
#include <iostream>
#include <cmath>
#include <stack>

int yylex();
std::string msg;
std::stack<int> nums;
int num1, num2;
%}


%x ERROR
%%
^[ \t]*\n+ { printf("\n"); };

[^0-9\+\-\*\/\%\^\n ] {
    ECHO;
    if (!(strcmp(yytext, ":q") == 0)) {
        msg = "error: wrong symbol : \""+ (std::string) yytext + "\".";
        BEGIN(ERROR);
    } else REJECT;
}

[-]?[0-9]+ {
    ECHO;
    nums.push(std::stoi(yytext)); 
}

[\+\-\*\/\%\^] { 
    ECHO;
    if (nums.size() >= 2) {
        num2 = nums.top(); nums.pop();
        num1 = nums.top(); nums.pop();
        REJECT;
    } else {
        msg = "error: not enough args.\n";
        BEGIN(ERROR);
    }
}

[\/\%] {
    if (num2 == 0) {
        msg = "error: dividing by zero.";
        BEGIN(ERROR);
    } else REJECT;
}

[\^] {
    if (num1 == 0 && num2 == 0) { 
        msg = "error: zero to power of zero.";
        BEGIN(ERROR);
    } else REJECT;
}

[\+] nums.push(num1 + num2);
[\-] nums.push(num1 - num2);
[\*] nums.push(num1 * num2);
[\/] nums.push(num1 / num2);
[\%] nums.push(num1 % num2);
[\^] nums.push((int) pow(num1, num2));

\n {
    ECHO;
    if (nums.size() == 1) {
        std::cout << "= " << nums.top() << "\n\n";
        nums = *(new std::stack<int>);
    } else {
        std::cout << "error: not enough operators.\n\n";
        nums = *(new std::stack<int>);
    } 
}

<ERROR>\n {
    ECHO;
    std::cout << msg << std::endl; 
    nums = *(new std::stack<int>);
    BEGIN(0);
}

":q" { return 0; }
%%


int yywrap() { return 1; }
int main() { return yylex(); }
