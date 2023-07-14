#include <stdio.h>
#include <math.h>
#include <assert.h>

int phi(long int n)
{
    int wynik=0;

    for(int i = 1; i <= n; i++)
    {
        int i2=i;       // Algorytm Euklidesa - szukanie nwd
        int n2=n;
               
        while(i2>0)
        {   
            int reszta=n2%i2;
            n2=i2;
            i2=reszta;
        }
        if(n2==1)   // jeśli para wzgl pierwsza to wynik +1
        {
            wynik++;
        }
    }
    return wynik;
}

int main(void)
{
    printf("Podaj n:\n");
    long int n;
    scanf("%ld", &n);
    assert(n>0);

    printf("phi(n) = %d\n", phi(n));

    return 0;
}