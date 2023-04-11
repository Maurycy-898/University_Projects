#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include "palindrom.h"

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