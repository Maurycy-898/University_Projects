#include <stdio.h>
#include <math.h>

int main(void)
{
    //zbieranie danych od użytkownika
    printf("Program rozwiązujący równania kwadratowe w postaci: ax^2 + bx + c = 0.\n");
    float a = 0;
    float b = 0;
    float c = 0;
    
    printf("podaj współczynnik a:\n");
    scanf("%f", &a);
    printf("podaj współczynnik b:\n");
    scanf("%f", &b);
    printf("podaj współczynnik c:\n");
    scanf("%f", &c);
    
    if(a==0) // przypadek z a=0
    {
        printf("to nie jest równanie kwadratowe!!!\n");
        float k = (-1*c)/b;
        printf("rozwiązaniem  tego równania jest x=%f.\n", k);
        return 0;
    }
    // wyznaczenie delty
    float delta = 0;
    delta = b*b - 4*a*c;
    float pierwiastek = 0;
    pierwiastek = sqrt(delta);

    printf("delta wynosi %f \n \n", delta);
    

     if(delta > 0) // przypadek kiedy delta > 0
     {
         float x1 = (-b + pierwiastek)/(2*a);
         float x2 = (-b - pierwiastek)/(2*a);
        
        printf("równanie ma dwa rozwiązania:\n x1=%f \n x2=%f.\n", x1, x2);
        return 0;

     }
    else if(delta == 0) // przypadek kiedy delta = 0
    {
        float z = -b/(2*a);
        printf("równanie ma jedno rozwiązanie:\n x1=%f. \n ", z);
    }
    else // przypadek kiedy delta < o
    {
        printf("brak rozwiązań w zbiorze  liczb rzeczywistych. \n");
    }

    return 0;

}