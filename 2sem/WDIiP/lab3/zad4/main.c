#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "biblio.h"

struct agent
{
    int x;
    int y;
};

int main(void)
{
    struct agent Bob = newagent(0,0);
    struct agent Alice = newagent(3,4);
    
    north(&Bob);
    north(&Alice);
    north(&Bob);
    south(&Alice);
    west(&Alice);
    north(&Bob);
    east(&Bob);
    south(&Alice);
    
    printf (" odległość = %f\n", distance(Bob, Alice));
    printf(" Bob x = %d , y = %d\n", Bob.x, Bob.y);
    printf(" Alice x = %d , y = %d\n", Alice.x, Alice.y);

}