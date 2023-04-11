#include <stdio.h>
#include <math.h>
#include <assert.h>
#include "zad2.h"

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