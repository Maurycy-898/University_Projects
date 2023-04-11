#include <iostream>
#include <cstring>
#include <string>
#include <iomanip>
#include <stdlib.h>
#include <time.h>
#include <random>

using namespace std;

void swap(int *x, int *y);
void insertion_sort(int size, int arr[]); // insertion sort
void merge_sort(int arr[], int start, int end); // merge sort
void merge(int arr[], int start, int middle, int end);
void quick_sort(int arr[], int start, int end); // quick sort
void partition_QS(int arr[], int start, int end, int *pivot);
void dual_pivot_QS(int arr[], int start, int end); // dual pivot sort
void partition_DP_QS(int arr[], int start, int end, int *right_pivot, int *left_pivot);
void hybrid_sort(int arr[], int start, int end); // hybrid sort
void insertion_HS(int arr[], int start, int end);

int COMP_COUNT = 0;
int SWAP_COUNT = 0;


int main(int argc, char **argv) {
    if (argc > 2) {
        int n = strtol(argv[1], NULL, 10);
        int k = strtol(argv[2], NULL, 10);

        cout << "insertion cmp;insertion swp;merge cmp;merge swp;quick cmp;quick swp;dual pivot cmp;dual pivot swp;hybrid cmp;hybrid swp;" << endl;
        for (int i = 1; i < n; i++) {
            random_device rd;
            mt19937 mt_gen(rd());
            uniform_int_distribution<> distribution(0, (2*i - 1));

            int array[i];

            for(int p = 0; p < k; p++ ) {
                for(int j = 0; j < i; j++) {
                    array[j] = distribution(mt_gen);
                }
                insertion_sort(i, array);
            }
            COMP_COUNT /= k;
            SWAP_COUNT /= k;
            cout << COMP_COUNT << ";" << SWAP_COUNT;
            COMP_COUNT = 0;
            SWAP_COUNT = 0;

            for(int p = 0; p < k; p++) {
                for(int j = 0; j < i; j++) {
                    array[j] = distribution(mt_gen);
                }
                merge_sort(array, 0, i - 1);
            }
            COMP_COUNT /= k;
            SWAP_COUNT /= k;
            cout << ";" << COMP_COUNT << ";" << SWAP_COUNT;
            COMP_COUNT = 0;
            SWAP_COUNT = 0;

            for(int p = 0; p < k; p++ ) {
                for(int j = 0; j < i; j++) {
                    array[j] = distribution(mt_gen);
                }
                quick_sort(array, 0, i - 1);
            }
            COMP_COUNT /= k;
            SWAP_COUNT /= k;
            cout << ";" << COMP_COUNT << ";" << SWAP_COUNT;
            COMP_COUNT = 0;
            SWAP_COUNT = 0;

            for(int p = 0; p < k; p++ ) {
                for(int j = 0; j < i; j++) {
                    array[j] = distribution(mt_gen);
                }
                dual_pivot_QS(array, 0, i - 1);
            }
            COMP_COUNT /= k;
            SWAP_COUNT /= k;
            cout << ";" << COMP_COUNT << ";" << SWAP_COUNT;
            COMP_COUNT = 0;
            SWAP_COUNT = 0;

            for(int p = 0; p < k; p++ ) {
                for(int j = 0; j < i; j++) {
                    array[j] = distribution(mt_gen);
                }
                hybrid_sort(array, 0, i - 1);
            }
            COMP_COUNT /= k;
            SWAP_COUNT /= k;
            cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << endl;
            COMP_COUNT = 0;
            SWAP_COUNT = 0;
        }
    }
}


/**
 * @brief implementation of insertion sort algorithm
 * 
 */
void insertion_sort(int size, int arr[]) {
    for (int i = 1; i < size; i++) {
        int key = arr[i];
        int j = i;
        while (arr[j - 1] > key && j > 0) {
            arr[j] = arr[j-1];
            j--;

            COMP_COUNT += 2; 
            SWAP_COUNT ++;
        }
        arr[j] = key;

        COMP_COUNT ++;
        SWAP_COUNT ++;
    }
}


/**
 * @brief implementation of merge sort algorithm
 * 
 */
void merge_sort(int arr[], int start, int end) {
    if (start < end) {
        int middle = start + ((end - start) / 2);

        merge_sort(arr, start, middle);
        merge_sort(arr, middle + 1, end);

        merge(arr, start, middle, end);

        COMP_COUNT ++;
    }
}

void merge(int arr[], int start, int middle, int end) {
    int size_left = middle - start + 1;
    int size_right = end - middle;

    int left_arr[size_left], right_arr[size_right];

    for (int i = 0; i < size_left; i++) {
        left_arr[i] = arr[start + i];
        
        COMP_COUNT ++;
        SWAP_COUNT ++;
    }
    for (int j = 0; j < size_right; j++) {
        right_arr[j] = arr[middle + 1 + j];
        
        COMP_COUNT ++;
        SWAP_COUNT ++;
    }

    int i = 0;
    int j = 0;
    int k = start;

    while (i < size_left && j < size_right) {
        if (left_arr[i] <= right_arr[j]) {
            arr[k] = left_arr[i];
            i++;
        } 
        else {
            arr[k] = right_arr[j];
            j++;
        }
        k++;

        COMP_COUNT += 3;
        SWAP_COUNT ++;
    }

    while (i < size_left) {
        arr[k] = left_arr[i];
        i++;
        k++;

        COMP_COUNT ++;
        SWAP_COUNT ++;
    }

    while (j < size_right) {
        arr[k] = right_arr[j];
        j++;
        k++;

        COMP_COUNT ++;
        SWAP_COUNT ++;
    }
}


/**
 * @brief implementation of quick sort algorithm
 * 
 */
void quick_sort(int arr[], int start, int end) {
    if (start < end) {
        int pivot = 0; 
        partition_QS(arr, start, end, &pivot);

        quick_sort(arr, start, pivot - 1);
        quick_sort(arr, pivot + 1, end);

        COMP_COUNT++;
    }
}

void partition_QS(int arr[], int start, int end, int *pivot) {
    int pivot_value = arr[end];
    int i = start;

    for (int j = start; j < end; j++) {
        if (arr[j] <= pivot_value) {
            if (i != j) {
                swap(&arr[i], &arr[j]);
            }
            i++;

            COMP_COUNT ++;
        }

        COMP_COUNT += 2;
    }
    swap(&arr[i], &arr[end]);
  
    *pivot = i;
}


/**
 * @brief implementation of dual-pivot quick sort algorithm
 * 
 */
void dual_pivot_QS(int arr[], int start, int end) {
    if (start < end) {
        int left_pivot, right_pivot;
        partition_DP_QS(arr, start, end, &right_pivot, &left_pivot);
        dual_pivot_QS(arr, start, left_pivot - 1);
        dual_pivot_QS(arr, left_pivot + 1, right_pivot - 1);
        dual_pivot_QS(arr, right_pivot + 1, end);

        COMP_COUNT ++;
    }
}

void partition_DP_QS(int arr[], int start, int end, int *right_pivot, int *left_pivot) {
    if (arr[start] > arr[end]) {
        swap(&arr[start], &arr[end]);

        COMP_COUNT ++;
    }

    int i = start + 1;
    int j = end - 1;
    int k = start + 1;

    int pivot_left = arr[start]; 
    int pivot_right = arr[end];

    while (k <= j) {
        if (arr[k] < pivot_left) {
            if (k != i) {
                swap(&arr[k], &arr[i]);
            }
            i++;

            COMP_COUNT += 2;
        }
        else if (arr[k] >= pivot_right) {
            while (arr[j] > pivot_right && k < j) {
                j--;
                COMP_COUNT += 2;
            }
            swap(&arr[k], &arr[j]);
            j--;

            if (arr[k] < pivot_left) {
                swap(&arr[k], &arr[i]);
                i++;

                COMP_COUNT ++;
            }

            COMP_COUNT ++;
        }
        k++;
    }
    i--;
    j++;
 
    swap(&arr[start], &arr[i]);
    swap(&arr[end], &arr[j]);
 
    *left_pivot = i; 
    *right_pivot = j;
}


/**
 * @brief Implementation of hybrid (quick - insertion) sort algorithm
 * 
 */
void hybrid_sort(int arr[], int start, int end) {
    while (start < end) { 
        if (end - start < 8) {
            insertion_HS(arr, start, end);

            COMP_COUNT ++;
            break;
        }
        else {
            int pivot; 
            partition_QS(arr, start, end, &pivot) ;
            if ((pivot - start) < (end - pivot)) {
                hybrid_sort(arr, start, pivot - 1);
                start = pivot + 1;

                COMP_COUNT ++;
            }
            else {
                hybrid_sort(arr, pivot + 1, end);
                end = pivot - 1;
            }
        }

        COMP_COUNT ++;
    }

}

void insertion_HS(int arr[], int start, int end) {
    for (int i = start + 1; i <= end; i++) {
        int key = arr[i];
        int j = i;
        while (arr[j - 1] > arr[j] && j > start) {
            arr[j] = arr[j - 1];
            j--;

            COMP_COUNT += 2; 
            SWAP_COUNT ++;
        }
        arr[j] = key;

        COMP_COUNT ++;
        SWAP_COUNT ++;
    }

}


void swap(int *x, int *y) {
    int tmp = *x;
    *x = *y;
    *y = tmp;

    SWAP_COUNT += 2;
}