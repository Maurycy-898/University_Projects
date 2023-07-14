#include <stdio.h>
#include <math.h>
#include <stdbool.h>
#include <float.h>
#include <assert.h>

int main(void)
{
    printf("Program liczący pierwiastek 1000 stopnia z liczby n!\nPodaj n > 0:\n");
    int n=0;
    scanf("%d", &n); //przyjmowanie i sprawdzanie danych od użytkownika
    assert(n > 0);

    const double maxDouble = DBL_MAX; //definiowanie zmiennych
    double silnia = 1;
    double pierwiastek = 1;
    bool wytyczna = false;

    int x = 1;
    while(x<=n)
    {
        if (maxDouble/x > silnia) //wykonuje operacje mnożenia z silni dopóki liczba nie przekracza
        {                         //maksymalnej wartości przechowywanej przez zmienną  
            silnia = silnia * x;
            wytyczna = false;
            x++;
        }
        else    //wyciąga pierwiastek gdy zmienna przechowuje maksymalną możliwą wartość
        {
            silnia = (pow(silnia,0.001));
            pierwiastek = pierwiastek * silnia;
            silnia = 1;
            wytyczna=true;
        }
    }
    
    if(wytyczna == false) //wykonuje jeśli pętla nie zakończyła się wyjęciem pierwiastka z liczby
    {
        silnia = (pow(silnia,0.001));
        pierwiastek = pierwiastek * silnia;
    }

    printf("wynik to: %lf\n", pierwiastek); //drukowanie

    return 0;

}
