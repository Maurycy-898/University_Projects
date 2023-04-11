#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include "palindrom.h"

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