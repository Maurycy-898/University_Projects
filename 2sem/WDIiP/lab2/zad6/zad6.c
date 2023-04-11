#include <stdio.h>
#include <math.h>
#include <assert.h>

int main(void)
{
    printf("Program znajdujący liczby doskonałe i pary liczb zaprzyjaźnionych do n\nPodaj n>0:");
    int n=0;
    scanf("%d", &n);
    assert(n > 0);

    //inicjowanie tablicy i wyzerowanie jej wartości
    int delta[n];
    for(int z=1; z<=n; z++)
    {
        delta[z]=0;
    }
    
    printf("\nLiczby idealne do %d:\n", n);
    // sprawdznie wartości funkcji delta dla każdej liczby od 1 do n i wpisywanie jej do tablicy
    for(int i = 1; i <= n; i++)
    {
        for(int j = 1; j <= i/2; j++)
        {
            if( i % j == 0)
            {
                delta[i]=delta[i]+j;
            }
        }
        // sprawdzanie i drukowanie liczb idealnych mniejszych od n
        if(delta[i]==i)
        {
            printf("%d,  ", i);
        }
    }


    printf("\n\nPary liczb zaprzyjaźnionych do %d:\n", n);
    //szukanie i drukowanie par zaprzyjaźnionych
    for(int i = 1; i < n; i++)
    {
        for(int j = i; j<n; j++)
        {
            if(delta[i] == j && delta[j] == i && i != j)
            {
                int p1 = fmin(i,j);
                int p2 = fmax(i,j);
                
                printf("(%d,%d); ", p1, p2);
            }    
        }

    }

    printf("\n\n");

    return 0;

}
