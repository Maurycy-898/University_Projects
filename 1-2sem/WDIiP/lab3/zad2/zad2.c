#include <stdio.h>
#include <math.h>
#include <assert.h>

double f(double x)
{
    double wynik;
    wynik = cos(x/2);
    return wynik;
}

double rozwiazanie(double a, double b, double eps)
{
    assert(f(a)*f(b) < 0);
    double sr = (a+b)/2;
    
    int n = 1;
    while(n > 0)
    {
        if (f(sr-eps)*f(sr+eps)<0)
        {
            return sr;
            break; 
        }   
        else if (f(sr)*f(a)<0) 
        {
            b = sr;
            sr = (sr+a)/2;
        }    
        else if(f(sr)*f(b)<0)
        {
            a = sr;
            sr = (sr+b)/2;
        }    
    }
}

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
