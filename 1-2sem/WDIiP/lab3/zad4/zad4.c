#include <stdio.h>
#include <stdlib.h>
#include <math.h>

struct agent
{
    int x;
    int y;
};

struct agent newagent(int x, int y)
{
    struct agent a;
    a.x=x;
    a.y=y;
    return a;
}

void north(struct agent *a)
{
    (*a).y = (*a).y + 1;
}

void south(struct agent *a)
{
    (*a).y = (*a).y - 1;
}

void east(struct agent *a)
{
    (*a).x = (*a).x + 1;
}

void west(struct agent *a)
{
    (*a).x = (*a).x - 1;
}

double distance(struct agent a1, struct agent a2)
{
    double distance = sqrt((pow((a1.x-a2.x),2)+pow((a1.y-a2.y),2)));
    return distance;
}

int main(void)
{
    struct agent Bob = newagent(0,0);
    struct agent Alice = newagent(0,0);
    
    int stala = 1;
    while(stala > 0)
    {
        printf("Wybierz agenta:\n-Wybierz 1->Bob\n-Wybierz 2->Alice\n-Wybierz 3 aby zakończyć\n");
        int n = 0;
        scanf("%d", &n);

        if(n==1)
        {
            printf("\nBob: hi sir I am waiting for the instructions!!!\n\nWybierz:\n-1 aby iść na północ\n-2 aby iść na południe\n-3 aby iść na wschód\n-4 aby iść na zachód\n-5 aby sprawdzić pozycję\n-0 aby zakończyć\n");
    
            int stala1 = 1;
            while(stala1>0)
            {
                int n1=0;
                scanf("%d",&n1);
                if(n1==1)
                {   north(&Bob);
                    printf("Bob: I am moving north!\n");
                }
                else if(n1==2)
                {   
                    south(&Bob);
                    printf("Bob: I am moving south!\n");        
                }
                else if(n1==3)
                {   
                    east(&Bob);
                    printf("Bob: I am moving east!\n");        
                }
                else if(n1==4)
                {   
                    west(&Bob);
                    printf("Bob: I am moving west!\n");        
                }
                else if(n1==5)
                {   
                    printf("Bob: my current position is (x=%d,y=%d)\n",Bob.x,Bob.y);        
                }
                else if(n1==0)
                {   
                    stala1=0;
                    printf("Bob: ok, bye sergeant!!!\n\n");        
                }
                else
                {
                    printf("Wybrałeś zły numer!!!\n\n");
                }
                
            
            }
        }
        else if(n==2)
        {
            printf("\nAlice: hi sir I am waiting for your instructions!!!\n\nWybierz:\n-1 aby iść na północ\n-2 aby iść na południe\n-3 aby iść na wschód\n-4 aby iść na zachód\n-5 aby sprawdzić pozycję\n-0 aby zakończyć\n");
    
            int stala1 = 1;
            while(stala1>0)
            {
                int n1=0;
                scanf("%d",&n1);
                if(n1==1)
                {   north(&Alice);
                    printf("Alice: I am moving north!\n");
                }
                else if(n1==2)
                {   
                    south(&Alice);
                    printf("Alice: I am moving south!\n");        
                }
                else if(n1==3)
                {   
                    east(&Alice);
                    printf("Alice: I am moving east!\n");        
                }
                else if(n1==4)
                {   
                    west(&Alice);
                    printf("Alice: I am moving west!\n");        
                }
                else if(n1==5)
                {   
                    printf("Alice: my current position is (x=%d,y=%d)\n",Alice.x,Alice.y);        
                }
                else if(n1==0)
                {   
                    stala1=0;
                    printf("Alice: ok, bye sergeant!!!\n\n");        
                }
                else
                {
                    printf("Wybrałeś zły numer!!!\n\n");
                }
                
            
            }
        }
        else if(n==3)
        {
            stala = 0;
            printf("Alice and Bob: ok, mission completed, bye sergeant!!!\n\n");
        }
        else
        {
            printf("Wybrałeś zły numer!!!\n");
        }
        
    }
    
    printf (" Distance = %f\n", distance(Bob, Alice));
    printf(" Bob (x=%d ,y=%d)\n", Bob.x, Bob.y);
    printf(" Alice (x=%d ,y=%d)\n", Alice.x, Alice.y);

}