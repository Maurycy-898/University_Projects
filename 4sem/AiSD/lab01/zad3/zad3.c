#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct Node {
    int data;
    struct Node *previous;
    struct Node *next;
} Node;

// functions to insert element
void insert_as_entry(Node **entry_point, int data);
void insert_before_entry(Node **entry_point, int data);
void insert_after_entry(Node **entry_point, int data);
// functions to remove element
void remove_from_entry(Node **entry_point);
void remove_before_entry(Node **entry_point);
void remove_after_entry(Node **entry_point);
void remove_by_data(Node **entry_point, int data);
// funtions to find/display element
void display_from_entry(Node *entry_point);
Node* get_element(Node **entry_point, int data);
// calculates list size
int list_size(Node *entry_point);
// merge lists
Node* merge_lists(Node **entry_point_1, Node **entry_point_2);
// exercises
void measure_element_acces_time();
void test_merge_function();


int main(void) {
    srand(time(NULL));

    test_merge_function();
    measure_element_acces_time();

    return 0;
}

void measure_element_acces_time() {
    Node *entry_point = NULL;
    int values[1000];
    clock_t t;

    int r;
    for (int i = 0; i < 1000; i++) {
        r = rand() % 1000;
        values[i] = r;
    }

    for (int i = 0; i < 1000; i++) {
        insert_as_entry(&entry_point, values[i]);
    }
    
    // accesing 150th element
    t = clock();
    int counter = 0;
    while (counter < 1000000) {
        get_element(&entry_point, values[850]);
        counter ++;
    }
    t = clock() - t;
    double average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces 150th element is :  %.8f seconds\n", average_time);

    // accesing 450th element
    t = clock();
    counter = 0;
    while (counter < 1000000) {
        get_element(&entry_point, values[550]);
        counter ++;
    }
    t = clock() - t;
    average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces 450th element is :  %.8f seconds\n", average_time);

    // accesing 950th element
    t = clock();
    counter = 0;
    while (counter < 1000000) {
        get_element(&entry_point, values[100]);
        counter ++;
    }
    t = clock() - t;
    average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces 950th element is :  %.8f seconds\n", average_time);

    // average time accesing random element
    t = clock();
    counter = 0;
    while (counter < 1000000) {
        r = rand() % 1000;
        get_element(&entry_point, values[r]);
        counter ++;
    }
    t = clock() - t;
    average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces random element is : %.8f seconds\n", average_time);
}

void test_merge_function() {
    Node *entry_point_1 = NULL;
    Node *entry_point_2 = NULL;
    
    // adding random elements
    int r;
    for(int i = 0; i < 10; i++) {
        r = rand() % 100;
        insert_as_entry(&entry_point_1, r);
        
        r = rand() % 100;
        insert_as_entry(&entry_point_2, r);
    }
    printf("\n\nBEFORE MERGE :\n");
    printf("LIST 1 :\n");
    display_from_entry(entry_point_1);
    printf("\n");
    printf("LIST 2 :\n");
    display_from_entry(entry_point_2);
    printf("\n");
    printf("AFTER MERGE(LIST 1, LIST 2) :\n");
    display_from_entry(merge_lists(&entry_point_1, &entry_point_2));
    printf("\n\n");
}

void insert_as_entry(Node **entry_point, int data) {
    if (*entry_point == NULL) {
        *entry_point = (Node *)malloc(sizeof(Node));
        (*entry_point)->data = data;
        (*entry_point)->previous = *entry_point;
        (*entry_point)->next = *entry_point;
        return;
    } 
    else {
        Node *current = (Node *)malloc(sizeof(Node));
        current->next = *entry_point;
        current->previous = (*entry_point)->previous;
        current->data = data;
        (*entry_point)->previous->next = current;
        (*entry_point)->previous = current;
        *entry_point = current;
        return;
    }
}

void insert_before_entry(Node **entry_point, int data) {
    if (*entry_point == NULL) {
        *entry_point = (Node *)malloc(sizeof(Node));
        (*entry_point)->data = data;
        (*entry_point)->previous = *entry_point;
        (*entry_point)->next = *entry_point;
        return;
    } 
    else {
        Node *tmp = (*entry_point)->previous;
        Node *current = (Node *)malloc(sizeof(Node));
        current->previous = tmp;
        current->next = (*entry_point);
        current->data = data;
        (*entry_point)->previous = current;
        tmp->next = current;
        
        return;
    }
}

void insert_after_entry(Node **entry_point, int data) {
    if (*entry_point == NULL) {
        *entry_point = (Node *)malloc(sizeof(Node));
        (*entry_point)->data = data;
        (*entry_point)->previous = *entry_point;
        (*entry_point)->next = *entry_point;
        return;
    } 
    else {
        Node *current = (Node *)malloc(sizeof(Node));
        Node *tmp = (*entry_point)->next;
        (*entry_point)->next = current;
        tmp->previous = current;
        current->next = tmp;
        current->previous = (*entry_point);
        current->data = data;
        return;
    }
}

void remove_from_entry(Node **entry_point) {
    if (*entry_point == NULL) {
        printf("list is empty");
        return;
    }
    else if ((*entry_point)->next == NULL) {
        *entry_point = NULL;
        return;
    }
    else {
        Node *tmp = (*entry_point)->next;
        tmp->previous = (*entry_point)->previous;
        free(*entry_point);
        *entry_point = tmp;
        return;
    }
}

void remove_before_entry(Node **entry_point) {
    if (*entry_point == NULL) {
        printf("list is empty");
        return;
    }
    else if ((*entry_point)->next == NULL) {
        *entry_point = NULL;
        return;
    }
    else {
        Node *tmp = (*entry_point)->next->next;
        free((*entry_point)->next);
        (*entry_point)->next = tmp;
        tmp->previous = *entry_point;
    }
}

void remove_after_entry(Node **entry_point) {
    if (*entry_point == NULL) {
        printf("list is empty");
        return;
    }
    else if ((*entry_point)->next == NULL) {
        free(*entry_point);
        *entry_point = NULL;
        return;
    }
    else {
        Node *tmp = (*entry_point)->previous->previous;
        free((*entry_point)->previous);
        (*entry_point)->previous = tmp;
        tmp->next = *entry_point;
    }
}

void remove_by_data(Node **entry_point, int data) {
    if ((*entry_point)->data == data) {
        remove_from_entry(entry_point);
        return;
    }
    if ((*entry_point)->next->data == data) {
        remove_after_entry(entry_point);
        return;
    }
    if ((*entry_point)->previous->data == data) {
        remove_before_entry(entry_point);
        return;
    }

    Node* tmp = *entry_point;
    while (tmp->data != data && tmp->next != *entry_point ) {
        tmp = tmp->next;
    }

    if (tmp == (*entry_point)->previous) {
        printf("cant find and remove element with given data\n");
        return;
    }

    tmp->previous->next = tmp->next;
    tmp->next->previous = tmp->previous;
    free(tmp);

    return;
}

void display_from_entry(Node *entry_point) {
    if (entry_point == NULL) {
        printf("List is empty");
        return;
    }

    Node *tmp = entry_point;
    do {
        printf("%d, ", tmp->data);
        tmp = tmp->next;
    } while(tmp != entry_point);
    
    printf("\n");
}

Node* get_element(Node **entry_point, int data) {
    if (*entry_point == NULL) {
        printf("List is empty");
        return NULL;
    }

    Node* tmp = *entry_point;
    while (tmp->data != data && tmp->next != *entry_point ) {
        tmp = tmp->next;
    }

    if (tmp == (*entry_point)->previous && tmp->data != data) {
        printf("cant find element with given data\n");
        return NULL;
    }

    return tmp;
}

int list_size(Node *entry_point) {
    Node *current = entry_point;
    int counter = 0;
    do {
        printf("%d, ", current->data);
        current = current->next;
        counter ++;
    } while(current != entry_point);
    
    printf("\n");
    return counter;
}

Node* merge_lists(Node **entry_point_1, Node **entry_point_2) {
    if (*entry_point_1 == NULL && *entry_point_2 == NULL) {
        return NULL;
    }
    if (*entry_point_1 == NULL) {
        return *entry_point_2;
    }
    if (*entry_point_2 == NULL) {
        return *entry_point_2;
    }
    
    Node *tmp_1 = (*entry_point_1)->previous;
    Node *tmp_2 = (*entry_point_2)->previous;
    
    (*entry_point_1)->previous = tmp_2;
    tmp_2->next = *entry_point_1;

    (*entry_point_2)->previous = tmp_1;
    tmp_1->next = *entry_point_2;

    return *entry_point_1;
}