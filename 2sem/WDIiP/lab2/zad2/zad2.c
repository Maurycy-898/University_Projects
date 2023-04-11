#include <stdio.h>
#include <assert.h>

int main(void)
{
    int n=0;
    printf("Podaj n > 0:\n");
    scanf("%d", &n);
    assert(n > 0);

    double x=0;
    double suma=0;

    printf("Wprowadz n liczb rzeczywistych:\n");

    for(int i=0; i<n; i++)
    {
        scanf("%lf", &x);
        suma = suma + x;
    }
    
    double srednia = suma/n;

    printf("\nśrednia podanych liczb wynosi:\n%lf\n", srednia);

    return 0;
}
