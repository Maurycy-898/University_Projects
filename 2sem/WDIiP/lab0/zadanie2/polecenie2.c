#include <stdio.h>

int main()

{
 float c;
 float f;

 printf("Podaj temperaturę w stopniach Celcjusza .\n");

 scanf("%f", &c);

 f=1.8*c+32;

 printf("Temperatura w stopniach Farenheita wynosi %f F .\n", f);

 return 0;
}
