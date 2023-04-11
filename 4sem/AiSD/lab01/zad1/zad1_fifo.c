#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// maximum limit of the fifo queue
#define LIMIT 1000 

// global variables
int LIFO_QUEUE[LIMIT];
int front;
int rear; 

// functions
void insert_element(int element); 
void remove_element();

int main(void) {
    printf("FIFO QUEUE\n");
    // front and rear = -1 implies that queue is empty
    front = -1;
    rear = -1; 

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
    if (rear == -1) {
        rear = 0;
        front = 0;
        LIFO_QUEUE[rear] = element;
        printf("added element : %d\n", element);
    }
    else if (rear == LIMIT - 1) {
        printf("queue is full\n");
        return;
    }
    else {
        rear++;
        LIFO_QUEUE[rear] = element;
        printf("added element : %d\n", element);
    }
}

void remove_element() {
    if (front == - 1) {
        printf("queue is empty \n");
    }
    else if (front == rear) {
        printf("removed element : %d\n", LIFO_QUEUE[front]);
        front = -1;
        rear = -1;
    }
    else {
        printf("removed element : %d\n", LIFO_QUEUE[front]);
        front++;
    }
}
