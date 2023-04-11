#include <stdio.h>
#include <math.h>
#include <assert.h>
#include <stdlib.h>
//version 2.0 (zapisuje do pliku)

int main(void)
{
    FILE *plik;
    
    printf("\nProgram drukujący kolejne wartości funkcji f (dla liczb od 1 do n):\n\nf = (ilepar)/(n^2)\n");
    printf("\nGdzie ilepar to ilość par liczb wzgl pierwszych należących do zbioru {1,..,n}^2\n\n");
    printf("\n\nPodaj dodatnią liczbę n:");
    int n;
    scanf("%d", &n);
    assert(n > 0);

    plik = fopen("wykres.csv","w");

    int ilepar=0;

    //Spr ilości par wzgl pierwszych dla zbiorów^2 od 1 do n 
    for(int n1 = 1; n1 <= n; n1++)
    {   
        //Sprawdzanie wszystkich możliwych par (p1,p2) ze zbioru n1^2
        for(int p1 = 1; p1 <= n1; p1++)
        {
            for(int p2 = 1; p2 <= n1; p2++)
            {
                //Algorytm Euklidesa - szukanie nwd
                int pom1=fmin(p1,p2);
                int pom2=fmax(p1,p2);
               
                while(pom1>0)
                {   
                    int reszta=pom2%pom1;
                    pom2=pom1;
                    pom1=reszta;
                }
                if(pom2==1) //jeśli para wzgl pierwsza(nwd = 1) to liczba par +1
                {
                    ilepar++;
                }
            }
        }
        // drukowanie szukanej liczby dla zbioru n1 (ilośc par wzgl pierwszych/(|n1|^2)
        double kwadrat =(pow(n1,2));
        double wynik = ilepar/kwadrat;
        ilepar = 0;
        
        fprintf(plik, "%lf;\n", wynik);
    }

    fclose(plik);

    return 0;
}
