#include <stdio.h>
#include <stdbool.h>

int find1(int x, int n, int t[n])
{
    for(int i=0; i<n; i++)
    {
        if(t[i]==x)
        {
            return i;
        }
    }
}

int main(void)
{
    int z = 5;
    int tab[5]={1,4,6,3,2};
    int k = 4;
    int wynik = find1(k, 5, &tab[5]);

    printf("%d\n", wynik);

    return 0;  
}