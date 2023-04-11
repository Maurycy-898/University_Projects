#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "biblio.h"

struct agent
{
    int x;
    int y;
};

double distance(struct agent a1, struct agent a2)
{
    double distance = sqrt((pow((a1.x-a2.x),2)+pow((a1.y-a2.y),2)));
    return distance;
}