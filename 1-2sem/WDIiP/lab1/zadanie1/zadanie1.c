#include <stdio.h>
#include <string.h>

int main(void)
{
char *tekst ="ABRAKADABRA";
int dlugosc = strlen(tekst); //ilość znaków w tekście
int spacje = 1;  //ilosc spacji na poczatku wierszy zaczynając od drugiego

for (int i = dlugosc; i>0; i--) //główna pętla
{
    for(int j = 0; j<i; j++)
    {
        printf("%c ", tekst[j]);  // wypisywanie napisu
    }
    printf("\n"); 
    
    for (int k=0;k<spacje;k++) //wcięcia na początku wierszy
    {
        printf(" ");
    }
        spacje++;
}
return 0;
    
}