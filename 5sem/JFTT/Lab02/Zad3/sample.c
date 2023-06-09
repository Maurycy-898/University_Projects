#include <stdio.h>

/** \brief Java style Doc String - Foo function */
int foo();

int bar(); /**< Bar function */

/// .NET Style Doc String
int g_global_var = 1;
/* com/
/* Hello
/* World
// */
int baz();
// */

/*! Global variable
 *  ... */
volatile int g_global;

//! Main
int main(int argc, const char* argv)
{
    printf("/* foo bar");
    //*/ bar();

    // \
    /*
    baz();
    /*/
    foo();
    //*/
/\
/*
    baz();
/*/
    foo();
//*/

    int k = \
25;

    return 1;
}