#include <stdio.h>
#include "task6_lib.h"


sollution new_sollution(int x, int y, bool is_correct) {
    sollution s = { x, y, is_correct };
    return s;
}


int factorial(int n) {
    int f = 1;
    for (int i = 1; i <= n; i++) {
        f = f * i;
    }
    return f;
}


int factorial_recur(int n) {
    if (n == 0)  return 1;  
    else  return (n * factorial(n - 1)); 
}


int gcd(int a, int b) {
    int tmp;
    while(b != 0) {
       tmp = a; a = b;
       b = tmp % b;
    }
    return a;
}


int gcd_recur(int a, int b) {
    if (b == 0) return a;
    return gcd(b, a % b);
}


sollution equation(int a, int b, int c) {
    int quotient, tmp = 0, x = 1, x1 = 0, y = 0, y1 = 1;

    while (b != 0) {
        quotient = a / b;
        tmp = x; x = x1;
        x1 = tmp - quotient * x1;
        tmp = y; y = y1;
        y1 = tmp - quotient * y1;
        tmp = a; a = b;
        b = tmp % b;
    }
    if (c % a != 0) return new_sollution(-1, -1, false);
    return new_sollution((x * (c / a)), (y * (c / a)), true);
} 


sollution equation_recur(int a, int b, int c) {
    if (b == 0) { 
        if (c % a != 0) return new_sollution(-1, -1, false);
        return new_sollution((c / a), 0, true);
    }
    sollution s = equation_recur(b, a % b, c); 
    return new_sollution(s.y, (s.x - (a / b) * s.y), s.is_correct);
}