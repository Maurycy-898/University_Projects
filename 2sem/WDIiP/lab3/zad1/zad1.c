#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool palindrom(char napis[])
{
    int dlugosc = strlen(napis);
    char napis2[dlugosc];

    for(int i=0; i < dlugosc ; i++)
    {
        napis2[dlugosc - i-1] = napis[i];
    }

    for(int i=0; i<dlugosc; i++)
    {
        if(napis[i] == napis2[i])
            return true;
        else
            return false;
            break;
    }    
}

int main(void)
{
    printf("Podaj słowo:\n");
    char *slowo;
    scanf("%s", slowo);

    if(palindrom(slowo))
        printf("to jest palindrom\n");

    else
        printf("to nie jest palindrom\n");
    
    return 0;
    
}