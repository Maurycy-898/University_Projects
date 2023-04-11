#ifndef TASK1_H
#define TASK1_H

#include <stdbool.h>

typedef struct __sollution__ { 
    int x, y;
    bool is_correct;
} sollution;

sollution new_sollution(int, int, bool);

int factorial(int);
int factorial_recur(int);

int gcd(int, int);
int gcd_recur(int, int);

sollution equation(int, int, int);
sollution equation_recur(int, int, int);

#endif