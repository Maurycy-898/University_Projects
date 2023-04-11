#include <stdio.h>
int main()
{
float x;
float y;
float s;
float i;
float r;

printf("Podaj wartość x: .\n");
scanf("%f", &x);

printf(".\n");

printf("podaj wartość y: .\n");
scanf("%f", &y);

printf(".\n");

s=x+y;
i=x/y;
r=x-y;

printf("Suma %f i %f wynosi %f .\n .\n",x,y,s);

printf("Różnica %f i %f wynosi %f .\n .\n",x,y,r);

printf("Iloraz %f i %f wynosi %f .\n",x,y,i);

return 0;
}
