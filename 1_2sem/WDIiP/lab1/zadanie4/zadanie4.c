#include <stdio.h>
#include <string.h>

int main(void)
{
    //ustalanie zmiennych
    char *gwiazdka ="*";
    char *spacja =" ";
    int n =0;
    printf("Podaj n>0.\n");
    scanf("%d", &n);
    int x = n;
    int y = 1;

    if(n<0) //wykluczenie liczb niespełniających warunku
    {
        printf("podana liczba nie spełnia warunku!\n");
    }

    else
    {
        for(int i=0; i<n; i++) //główna pętla powtarzana n razy
        {
            for(int j=0; j<x-1; j++) //spacje
            {
                printf("%s", spacja);
            }
            x--;

            for( int k=0; k<y; k++) //gwiazdki
            {
                printf("%s", gwiazdka);
            }
            y= y+2;

            printf("\n");
        }

        
    
    }
    
    return 0;
}