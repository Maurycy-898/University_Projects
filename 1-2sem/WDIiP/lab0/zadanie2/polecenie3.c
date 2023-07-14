#include <stdio.h>
int main()
{
int x1;
int x2;
int x3;
int x4;
int x5;
int x6;


printf("Wprowadz ilosc jedynek \n");
scanf("%d", &x1);

printf("wprowadz ilosc dwójek \n");
scanf("%d", &x2);

printf("wprowadz ilosc trójek: \n");
scanf("%d", &x3);

printf("wprowadz ilosc czworek: \n");
scanf("%d", &x4);

printf("wprowadz ilosc piatek: \n");
scanf("%d", &x5);

printf("wprowadz ilosc szostek: \n");
scanf("%d", &x6);

float z= 1*x1 + 2*x2 + 3*x3 + 4*x4 + 5*x5 +6*x6;

float y= x1+x2+x3+x4+x5+x6;

float s= z/y;

printf("twoja średnia to: %f \n", s);

return 0;
}
