#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct Node {
    int data;
    struct Node *next;
} Node;

// functions to insert element
void insert_back(Node **head, int data);
void insert_front(Node **head, int data);
// functions to remove element
void remove_back(Node **head);
void remove_front(Node **head);
void remove_by_data(Node **head, int data);
void remove_by_index(Node **head, int index);
// funtions to find/display element
void display(Node *head);
Node* get_by_data(Node **head, int data);
Node* get_by_index(Node **head, int index);
// merge lists
Node* merge_lists(Node **head_1, Node **head_2);
// calculates list size
int list_size(Node *head);
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
    Node *head = NULL;
    int values[1000];
    clock_t t;

    int r;
    for (int i = 0; i < 1000; i++) {
        r = rand() % 1000;
        values[i] = r;
    }

    for (int i = 0; i < 1000; i++) {
        insert_front(&head, values[i]);
    }
    
    // accesing 150th element
    t = clock();
    int counter = 0;
    while (counter < 1000000) {
        get_by_index(&head, 150);
        counter ++;
    }
    t = clock() - t;
    double average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces 150th element is :  %.8f seconds\n", average_time);

    // accesing 450th element
    t = clock();
    counter = 0;
    while (counter < 1000000) {
        get_by_index(&head, 450);
        counter ++;
    }
    t = clock() - t;
    average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces 450th element is :  %.8f seconds\n", average_time);

    // accesing 950th element
    t = clock();
    counter = 0;
    while (counter < 1000000) {
        get_by_index(&head, 950);
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
        get_by_data(&head, values[r]);
        counter ++;
    }
    t = clock() - t;
    average_time = (((double)t)/CLOCKS_PER_SEC)/counter;
    printf("Average time to acces random element is : %.8f seconds\n", average_time);
}

void test_merge_function() {
    Node *head_1 = NULL;
    Node *head_2 = NULL;
    
    // adding random elements
    int r;
    for(int i = 0; i < 10; i++) {
        r = rand() % 100;
        insert_front(&head_1, r);
        
        r = rand() % 100;
        insert_front(&head_2, r);
    }
    printf("\n\nBEFORE MERGE :\n");
    printf("LIST 1 :\n");
    display(head_1);
    printf("\n");
    printf("LIST 2 :\n");
    display(head_2);
    printf("\n");
    printf("AFTER MERGE(LIST 1, LIST 2) :\n");
    display(merge_lists(&head_1, &head_2));
    printf("\n\n");
}

void insert_back(Node **head, int number) {
	if(*head == NULL) {
		*head = (Node *)malloc(sizeof(Node));
   		(*head)->data = number;
    	(*head)->next = NULL;
	}
    else {
		Node *current = *head;
	
	    while (current->next != NULL) {
	        current = current->next;
	    }
	
	    current->next = (Node *)malloc(sizeof(Node));
	    current->next->data = number;
	    current->next->next = NULL;	
	}
}

void insert_front(Node **head, int data) {
    Node *current = (Node *)malloc(sizeof(Node));
 
    current->data = data;
    current->next = (*head);
    *head = current;
}
 
void remove_back(Node **head) {
	if((*head)->next == NULL) {	
		*head = NULL;	
	} 
    else {
		Node *current = *head;
		while (current->next->next!= NULL) {
            current = current->next;
    	}
   		free(current->next);
   		current->next = NULL;
	}   
}

void remove_front(Node **head) {
    Node * tmp = NULL;
 
    if (*head != NULL) {
        tmp = (*head)->next;
        free(*head);
        *head = tmp;	
	}   
}
 
void remove_by_data(Node **head, int data) {
    Node* tmp = *head;
    Node* previous; 
   
    if ((*head)->data == data) { 				  
        remove_front(head);
        return;
    } 
  
    while (tmp != NULL && tmp->data != data) { 
        previous = tmp; 
        tmp = tmp->next; 
    } 
   
    if (tmp == NULL){
    	printf("element with such data doesn't exist\n");
    	return;
    } 
  
    previous->next = tmp->next; 
    free(tmp);
}
 
void remove_by_index(Node **head, int position) {
	if(position == 0) {
        remove_front(head);
    }
    else {
		Node *current = *head;
	    Node *tmp;
		
		int i = 0;
		while (current->next != NULL && i < position - 1) {
	        current = current->next;
			i++;    
		}
			
		tmp = current->next;
	    current->next = tmp->next;
	    free(tmp);	
	}
}

void display(Node *head) {
    if (head == NULL) {
        printf("List is empty");
        return;
    }
    
    Node *current = head;
    while (current != NULL) {
        printf("%d, ", current->data);
        current = current->next;
    }
    printf("\n");
}

Node* get_by_data(Node **head, int data) {
    Node *tmp = *head;

    int position = 0;
    while (tmp != NULL && tmp->data != data) {
        tmp = tmp->next;
        position++;
    }

    if (tmp == NULL) {
        printf("element with such data doesn't exist\n");
        return NULL;
    }
    else {
        //printf("element with value:%d (%d), has index:%d", data, tmp->data, position);
        return tmp;
    }
}

Node* get_by_index(Node **head, int index) {
    Node *tmp = *head;

    int position = 0;
    while (tmp != NULL && position < index) {
        tmp = tmp->next;
        position++;
    }

    if (tmp == NULL) {
        printf("given index is out of bounds\n");
        return NULL;
    }
    else {
        //printf("element with value:%d, has index:%d", tmp->data, position);
        return tmp;
    } 
}

Node* merge_lists(Node **head_1, Node **head_2) {
    if (*head_1 == NULL && *head_2 == NULL) {
        return NULL;
    }
    if (*head_1 == NULL) {
        return *head_2;
    }
    if (*head_2 == NULL) {
        return *head_1;
    }

    Node *tmp = *head_1;
    while (tmp->next != NULL) {
        tmp = tmp->next;
    }
    tmp->next = *head_2;

    return *head_1;
}

int list_size(Node *head) {
    Node *current = head;
    
    int counter = 0;
    while (current != NULL) {
        current = current->next;
        counter ++;
    }
    return counter; 
}

