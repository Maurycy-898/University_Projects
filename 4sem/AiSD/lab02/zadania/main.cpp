#include <iostream>
#include <cstring>
#include <string>
#include <iomanip>

using namespace std;

void print_array(int size, int arr[]);
void swap(int *x, int *y);
void insertion_sort(int size, int arr[]);
void merge_sort(int arr[], int start, int end);
void merge(int arr[], int start, int middle, int end);
void quick_sort(int arr[], int start, int end);
void partition_QS(int arr[], int start, int end, int *pivot);
void dual_pivot_QS(int arr[], int start, int end);
void partition_DP_QS(int arr[], int start, int end, int *right_pivot, int *left_pivot);
void hybrid_sort(int arr[], int start, int end);
void insertion_HS(int arr[], int start, int end);

int ARRAY_SIZE = 0;
int COMP_COUNT = 0;
int SWAP_COUNT = 0;


int main(int argc, char **argv) {
    if (argc > 1) {
        string line;
        getline(cin, line);
        int n = stoi(line);
        ARRAY_SIZE = n;
        int array[n];
        
        int i = 0;
        while( i < n && getline(cin, line)) {
            array[i] = stoi(line);
            i++;
        }
        cout << "Received size: " << n << endl;
        cout << "Received array:" << endl;
        print_array(n, array);
        cout << endl;
         
        if (strcmp(argv[1], "insertion") == 0) {
            cout << "Insertion sort steps:" << endl;
            insertion_sort(n, array);
            cout << "\n\nAray after insertion sort:" << endl;
            print_array(n, array);
        } 
        else if (strcmp(argv[1], "merge") == 0) {
            cout << "Merge sort steps:" << endl;
            merge_sort(array, 0, n - 1);
            cout << "\n\nAray after merge sort:" << endl;
            print_array(n, array);
        } 
        else if (strcmp(argv[1], "quick") == 0) {
            cout << "Quick sort steps:" << endl;
            quick_sort(array, 0, n - 1);
            cout << "\n\nAray after quick sort:" << endl;
            print_array(n, array);
        } 
        else if (strcmp(argv[1], "dual_pivot") == 0) {
            cout << "Dual pivot quicksort steps:" << endl;
            dual_pivot_QS(array, 0, n - 1);
            cout << "\n\nAray after dual pivot quick sort:" << endl;
            print_array(n, array);
        } 
        else if (strcmp(argv[1], "hybrid") == 0) {
            cout << "Hybrid algorithm sort steps:" << endl;
            hybrid_sort(array, 0, n - 1);
            cout << "\n\nAray after hybrid algorithm sort:" << endl;
            print_array(n, array);
        } 
        else {
            cout << "Pass type of algorithm you want to use as an argument" << endl;
            cout << "Available types: insertion, merge, quick, dual_pivot, hybrid" << endl;
            cout << "Usage: \"./$this_program $algorithm_type\" ";
            cout << " ,or: \"./$data_generator $array_lenght | ./$this_program $algorithm_type" << endl;
            return 0;
        }

        cout << "\n\nStats for array of size " << n << " and " << argv[1] << " sort algorithm:" << endl;
        cout << "Number of array key comparisons: " << COMP_COUNT << endl;
        cout << "Number of array key swaps: " << SWAP_COUNT << endl;
    }
    else {
        cout << "Pass type of algorithm you want to use as an argument" << endl;
        cout << "Available types: insertion, merge, quick, dual_pivot, hybrid" << endl;
        cout << "Usage: \"./$this_program $algorithm_type\" ";
        cout << " ,or: \"./$data_generator $array_lenght | ./$this_program $algorithm_type" << endl;
        return 0;
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

        print_array(size, arr);

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
        
        print_array(ARRAY_SIZE, arr);

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

    print_array(ARRAY_SIZE, arr);
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
    int pivot_left = arr[start]; 
    int pivot_right = arr[end];

    int i = start + 1;
    int j = end - 1;
    int k = start + 1;

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

    print_array(ARRAY_SIZE, arr);
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
            partition_QS(arr, start, end, &pivot);

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
        while (arr[j - 1] > key && j > start) {
            arr[j] = arr[j - 1];
            j--;

            COMP_COUNT += 2; 
            SWAP_COUNT ++;
        }
        arr[j] = key;
        
        print_array(ARRAY_SIZE, arr);
        
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

/**
 * @brief prints given array content (when size < 50)
 * 
 */
void print_array(int size, int arr[]) {
    if (size < 50) {
        for(int i = 0; i < size; i++) {  
            cout << setw(2) << arr[i] << ", ";
        }
        cout << "\n" << endl;
    }
}
