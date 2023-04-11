#include <stdio.h>
#include <math.h>
#include <assert.h>
#include "biblio.h"

int main(void)
{
    printf("Podaj n:\n");
    long int n;
    scanf("%ld", &n);
    assert(n>0);

    printf("phi(n) = %d\n", phi(n));

    return 0;
}