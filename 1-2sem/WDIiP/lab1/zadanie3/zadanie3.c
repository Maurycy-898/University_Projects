#include <stdio.h>
#include <string.h>

int main(void)
{
    //wyznaczanie zmiennych
    char *gwiazdka = "*";
    int n =0;
    printf("podaj zmienną 0 < n < 20.\n");
    scanf("%d", &n );

  if(n<0 || n>20) //odrzucanie liczb spoza zakresu
    {
        printf("podałeś liczbę spoza zakresu!\n");
    }
    else
    {
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<2*n; j++)
         {
            printf("%s", gwiazdka); //drukowanie rzędu gwiazdek
         }
            printf("\n");
        }

    }
    return 0;
}