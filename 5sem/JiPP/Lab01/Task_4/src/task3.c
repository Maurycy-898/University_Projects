#include <stdio.h>
#include "task3_wrapper.h"

int main (int argc, char **argv) {
    int n, num1 = 0, num2 = 0, num3 = 0;
    sollution s;

    bool loop = true;
    while(loop) {
        printf("\n\nChoose the operation: \n [0] factorial\n [1] factorial recur\n [2] gcd\n [3] gcd recur\n [4] equation\n [5] equation recur\n else - Quit\n\n");
        scanf("%d", &n);
        switch (n) {
            case 0:            
                printf("Enter the number: \n");
                scanf("%d", &num1);
                printf("The factorial of %d equals %d", num1, factorial(num1));
                break;
            case 1:
                printf("Enter the number: \n");
                scanf("%d", &num1);
                printf("The factorial of %d equals %d", num1, factorial_recur(num1));
                break;
            case 2:        
                printf("Enter two numbers: \n");
                scanf("%d %d", &num1, &num2);
                printf("The GCD(%d, %d) equals %d", num1, num2, gcd(num1, num2));
                break;
            case 3:
                printf("Enter two numbers: \n");
                scanf("%d %d", &num1, &num2);
                printf("The GCD(%d, %d) equals %d", num1, num2, gcd_recur(num1, num2));
                break;
            case 4:
                printf("Enter three numbers: \n");
                scanf("%d %d %d", &num1, &num2, &num3);
                s = equation(num1, num2, num3);
                if (s.IsCorrect) {
                    printf("Sollution of: %dx + %dy = %d", num1, num2, num3);
                    printf("\nis: x = %d, y = %d", s.X, s.Y);
                }
                else { printf("Sollution does not exist in Integers"); }
                break;
            case 5:
                printf("Enter three numbers: \n");
                scanf("%d %d %d", &num1, &num2, &num3);
                s = equation_recur(num1, num2, num3);
                if (s.IsCorrect) {
                    printf("Sollution of: %dx + %dy = %d", num1, num2, num3);
                    printf("\nis: x = %d, y = %d", s.X, s.Y);
                }
                else { printf("Sollution does not exist in Integers"); }
                break;
            default:
                loop = false;
                break;
        }
    }
    return 0;
}