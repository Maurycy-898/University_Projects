#include <stdio.h>
#include <math.h>
#include <assert.h>
#include "zad2.h"

int main(void)
{
    printf("Podaj współczynniki a<b, b, eps:\n");
    double a,b,eps;
    scanf("%lf",&a); scanf("%lf", &b); scanf("%lf", &eps);
    assert(a<b);
    printf("Miejsce zerowe funkcji między punktami %lf , %lf z dokł do '%lf' to:\n'%lf'\n",
    a, b, eps, rozwiazanie(a,b,eps)); 

    return 0;
}