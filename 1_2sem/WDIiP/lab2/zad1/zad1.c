#include <stdio.h>
#include <assert.h>

int main(void)
{
    int zlote, grosze;
    printf("Podaj kwotę.\nPodaj liczbę złotych:");
    scanf("%d", &zlote);
    assert(zlote > 0);
    printf("Podaj ilość groszy:");
    scanf("%d", &grosze);
    assert(grosze > 0);

    if(grosze >= 100)
    {
        zlote = zlote+(grosze/100);
        grosze = grosze % 100;
    }

    int l200=0, l100=0, l50=0, l20=0, l10=0, l5=0, l2=0, l1=0;
    int g50=0, g20=0, g10=0, g5=0, g2=0, g1=0;

    int i = zlote;
    int j = grosze;

    while(i>0)
    {
        if(i>=200)
        {
            i= i-200;
            l200++;
        }    
        else if(i>=100)
        {
            i=i-100;
            l100++;
        }    
        else if(i>=50)
        {
            i=i-50;
            l50++;
        }
        else if(i>=20)
        {
            i=i-20;
            l20++;
        }
        else if(i>=10)
        {
            i=i-10;
            l10++;
        }
        else if(i>=5)
        {
            i=i-5;
            l5++;
        }
        else if(i>=2)
        {
            i=i-2;
            l2++;
        }    
        else
        {
            i--;
            l1++;
        }
    }
    while(j>0)
    {
        if(j>=50)
        {
            j=j-50;
            g50++;
        }
        else if(j>=20)
        {
            j=j-20;
            g20++;
        }
        else if(j>=10)
        {
            j=j-10;
            g10++;
        }
        else  if(j>=5)
        {
            j=j-5;
            g5++;
        }
        else if(j>=2)
        {
            j=j-2;
            g2++;
        }
        else
        {
            j--;
            g1++;
        }
        

    }
    int monety = l5+l2+l1+g50+g20+g10+g5+g2+g1;
    int banknoty = l200+l100+l50+l20+l10;
    
    printf("\nPodana kwota składa się z:\n\n\n");
    printf("%d banknotów:\n\n%d x 200zł\n%d x 100zł\n%d x 50zł\n%d x 20zł\n%d x 10zł\n\n\n",banknoty,l200,l100,l50,l20,l10);
    printf("%d monet:\n\n%d x 5zł\n%d x 2zł\n%d x 1zł\n%d x 50gr\n%d x 20gr\n%d x 10gr\n%d x 5gr\n%d x 2gr\n%d x 1gr\n",monety,l5,l2,l1,g50,g20,g10,g5,g2,g1);

    return 0;
}
