#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "biblio.h"

struct agent
{
    int x;
    int y;
};

void south(struct agent *a)
{
    (*a).y = (*a).y-1;
}