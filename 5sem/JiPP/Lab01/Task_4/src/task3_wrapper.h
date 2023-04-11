#ifndef WRAPPER_H
#define WRAPPER_H

#include <stdbool.h>

typedef struct sollution {
    int X;
    int Y;
    bool IsCorrect;
} sollution;

extern int factorial(int);
extern int factorial_recur(int);

extern int gcd(int, int);
extern int gcd_recur(int, int);

extern sollution equation(int, int, int);
extern sollution equation_recur(int, int, int);

#endif