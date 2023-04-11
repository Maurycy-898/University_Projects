#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// maximum limit of the lifo queue
#define LIMIT 1000 

// global variables
int LIFO_QUEUE[LIMIT];
int top;

// functions
void insert_element(int element); 
void remove_element();

int main(void) {
    printf("LIFO QUEUE\n");
    // front and rear = -1 implies that queue is empty
    top = -1;

    srand(time(NULL));   
    int r;
    // adding 100 random elements
    for(int i = 0; i < 100; i++) {
        r = rand() % 100;
        insert_element(r);
    }
    printf("\nFINISHED INSERTING ELEMENTS\n\n");
    
    // "removing" elements
    for(int i = 0; i < 100; i++) {
        remove_element();
    }
    printf("\nFINISHED REMOVING ELEMENTS\n\n");

    return 0;
}


void insert_element(int element) {
    if (top == -1) {
        top = 0;
        LIFO_QUEUE[top] = element;
        printf("added element : %d\n", element);
    }
    else if (top == LIMIT - 1) {
        printf("queue is full\n");
        return;
    }
    else {
        top++;
        LIFO_QUEUE[top] = element;
        printf("added element : %d\n", element);
    }
}

void remove_element() {
    if (top == -1) {
        printf("queue is empty \n");
    }
    else {
        printf("removed element : %d\n", LIFO_QUEUE[top]);
        top--;
    }
}