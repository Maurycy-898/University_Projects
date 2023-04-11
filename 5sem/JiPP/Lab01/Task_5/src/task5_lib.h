#ifndef TASK5_H
#define TASK5_H

#include <stdbool.h>

typedef struct __sollution__ { 
    int X;
    int Y;
    bool IsCorrect;
} sollution;

sollution new_sollution(int, int, bool);

int factorial(int);
int factorial_recur(int);

int gcd(int, int);
int gcd_recur(int, int);

sollution equation(int, int, int);
sollution equation_recur(int, int, int);

#endif