#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "biblio.h"

struct agent
{
    int x;
    int y;
};

void east(struct agent *a)
{
    (*a).x = (*a).x+1;
}