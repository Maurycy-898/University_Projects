#include <stdio.h>
#include <assert.h>

int main(void)
{
    printf("\nProgram rozwiązujący równanie : 1+1/2+1/3+...+1/n > x\nPodaj x > 0:\n");
    int x = 0;
    scanf("%d", &x);
    assert(x > 0);

    int n=0;
    double suma=0;

    while(suma <= x)
    {
        n++;
        assert(n > 0);
        suma = suma+(1.0/n);   
    }

    printf("Równanie prawdziwe dla n = %d i większych\n(dla n = %d suma wynosi: %lf)\n\n", n, n, suma);
    
    
    return 0;
}
