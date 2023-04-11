#include <stdio.h>
#include <string.h>

int main2()
{
    //ustalanie zmiennych
    char *gwiazdka ="*";
    char *spacja =" ";
    int n =0;
    printf("Podaj n>0.\n");
    scanf("%d", &n);
    int y = n;
    int k = n;

    if(n<0) //wykluczenie liczb niespełniających warunku
    {
        printf("podana liczba nie spełnia warunku!\n");
    }

    else
    {
        
        
            for(int i=n; i>0; i--) //główna pętla powtarzana n razy
            {
                for(int j=0; j<k-1; j++) //spacje
                {
                    printf("%s", spacja);
                }
                k=k-1;

                for( int l= y-1; l<n; l++) //gwiazdki
                {
                    printf("%s", gwiazdka);
                }
                y= y-4;
                printf("\n");
            }
            
            

        
    
    }
    
    return 0;
}

int main(void)
{
    printf("%d %d %d", main2, main2, main2);

    return 0;
}