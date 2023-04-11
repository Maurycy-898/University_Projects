#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "biblio.h"

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