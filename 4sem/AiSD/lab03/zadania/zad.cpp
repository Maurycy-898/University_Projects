#include <iostream>
#include <stdlib.h>
#include <cstring>
#include <string>
#include <iomanip>
#include <time.h>
#include <random>

using namespace std;

// global random generator
random_device rd;
mt19937 mt_gen(rd());
int random(int start, int end);

// random selection algorithm
int random_select(int arr[], int start, int end, int position_stat);
int basic_partition(int arr[], int start, int end);

// selection algorithm
int select(int arr[], int start, int end, int position_stat, int sub_arr_size);
int find_median_of_median(int arr[], int start, int end);
int insertion_sort_find_median(int arr[], int start, int end);
int partition(int arr[], int start, int end, int pivot_val);

//binary search algorithm (works for sorted arrays)
int binary_search(int arr[], int start, int end, int key_val);

// quick sort algorithms using selection algorithm ideas
void new_quick_sort(int arr[], int start, int end, int sub_arr_size = 5);
void new_dual_pivot_QS(int arr[], int start, int end, int sub_arr_size = 5);
void partition_DPQS(int arr[], int start, int end,int *left_pivot, int *right_pivot, int pivot_val);

// helpful functions
void print_array(int size, int arr[]);
void print_array(int arr, int start, int end);
void swap(int *x, int *y);



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char **argv) {
   if (argc > 1) {
        string line;
        getline(cin, line);
        int n = stoi(line);
        int array[n];
        
        int i = 0;
        while( i < n && getline(cin, line)) {
            array[i] = stoi(line);
            i++;
        }
        cout << "\nReceived size: " << n << endl;
        cout << "\nReceived array:" << endl;
        print_array(n, array);
    

        if (strcmp(argv[1], "random_select") == 0) {
            int position = random(0, n - 1);
            int result = random_select(array, 0, n - 1, position);
            cout << "\nAray after random select: " << endl;
            print_array(n, array);
            new_quick_sort(array, 0, n - 1);
            cout << "\n Sorted aray: " << endl;
            print_array(n, array);
            cout << "\nRandom select result for random position: " << position << " =  " << result << endl;
            cout << "Expected result : array[" << position << "] = " << array[position] << "\n" << endl;
        } 
        else if (strcmp(argv[1], "select") == 0) {
            int position = random(0, n - 1);
            int result = select(array, 0, n - 1, position, 5);
            cout << "\nAray after select: " << endl;
            print_array(n, array);
            new_quick_sort(array, 0, n - 1);
            cout << "\n Sorted aray: " << endl;
            print_array(n, array);
            cout << "\nSelect result for random position: " << position << " =  " << result << endl;
            cout << "Expected result : array[" << position << "] = " << array[position] << "\n" << endl;
        }
        else if (strcmp(argv[1], "binary_search") == 0) {
            int position = random(0, n - 1);
            int searched_val = array[position];
            new_quick_sort(array, 0, n - 1);
            cout << "\n Sorted aray: " << endl;
            print_array(n, array);
            cout << "Searching for value : " << searched_val << endl;
            int result = binary_search(array, 0, n - 1, searched_val);
            cout << "\nBinary search returned index = " << result << endl;
            cout << "Check result : array[" << result << "] = " << array[result] << "\n" << endl;
        } 
        else if (strcmp(argv[1], "new_quick_sort") == 0) {
            cout << "Array before new quick sort:" << endl;
            print_array(n, array);
            new_quick_sort(array, 0, n - 1);
            cout << "\nAray after new quick sort:" << endl;
            print_array(n, array);
        } 
        else if (strcmp(argv[1], "new_dual_pivot") == 0) {
            cout << "Array before new dual pivot quicksort: " << endl;
            print_array(n, array);
            new_dual_pivot_QS(array, 0, n - 1);
            cout << "\nAray after new dual pivot quick sort:" << endl;
            print_array(n, array);
        } 
        else {
            cout << "\nPass type of algorithm you want to use as an argument" << endl;
            cout << "Available types: random_select, select, binary_search, new_quick_sort, new_dual_pivot" << endl;
            cout << "Usage: \"./$this_program $algorithm_type\" ";
            cout << " ,or: \"./$data_generator $array_lenght | ./$this_program $algorithm_type\n" << endl;

            return 0;
        }

    }
    else {
        cout << "\nPass type of algorithm you want to use as an argument" << endl;
        cout << "Available types: random_select, select, binary_search, new_quick_sort, new_dual_pivot" << endl;
        cout << "Usage: \"./$this_program $algorithm_type\" ";
        cout << " ,or: \"./$data_generator $array_lenght | ./$this_program $algorithm_type\n" << endl;

        return 0;
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/**
 * @brief returns element that would be on given position in the sorted array
 * given position stat should be between [0...(n - 1)] where n is the array size
 * In other worlds returns @position_stat smallest element (starting from zero)
 */
int random_select(int arr[], int start, int end, int position_stat) {
    if (start == end) return arr[start];

    int pivot = basic_partition(arr, start, end);

    int pivot_position_stat = pivot - start;
    
    if (pivot_position_stat > position_stat) return random_select(arr, start, pivot - 1, position_stat);

    else if (pivot_position_stat < position_stat) return random_select(arr, pivot + 1, end, position_stat - pivot_position_stat - 1);

    else return arr[pivot];
}


/**
 * @brief basic partition, takes array last element value as pivot
 * 
 */
int basic_partition(int arr[], int start, int end) {
    int new_pivot = start;
    int pivot_value = arr[end];

    for (int j = start; j < end; j++) {
        if (arr[j] <= pivot_value) {
            if (new_pivot != j) {
                swap(&arr[new_pivot], &arr[j]);
            }
            new_pivot++;
        } 
    }
    swap(&arr[new_pivot], &arr[end]);

    return new_pivot;
}


/**
 * @brief returns element that would be on given position in the sorted array
 * given position stat should be between [0...(n - 1)] where n is the array size
 * In other worlds returns @position_stat smallest element (starting from zero)
 * 
 */
int select(int arr[], int start, int end, int position_stat, int sub_arr_size = 5) {
    if (start == end) return arr[start];
    
    int arr_size = end - start + 1;
    int median_arr_size = (arr_size + sub_arr_size - 1) / sub_arr_size;
    int median_arr[median_arr_size];

    int temp_start = start;
    int temp_end = start + sub_arr_size - 1;
    for (int i = 0; i < arr_size / sub_arr_size; i++) {
        median_arr[i] = insertion_sort_find_median(arr, temp_start, temp_end);
            
        temp_start += sub_arr_size;
        temp_end += sub_arr_size;
    }

    // when last part is shorter than sub_arr size
    if (arr_size % sub_arr_size != 0) { 
        median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
    }

    // find median of medians using same method
    int median_of_median = select(median_arr, 0, (median_arr_size - 1), (median_arr_size - 1) / 2, sub_arr_size);

    int pivot = partition(arr, start, end, median_of_median);

    int pivot_position_stat = pivot - start;

    if (pivot_position_stat > position_stat) return select(arr, start, pivot - 1, position_stat, sub_arr_size);

    else if (pivot_position_stat < position_stat) return select(arr, pivot + 1, end, position_stat - pivot_position_stat - 1, sub_arr_size);

    else return arr[pivot];
}


/**
 * @brief Sorts given sub-array or array using insertion sort.
 * Returns median from sorted fragment.
 * 
 */
int insertion_sort_find_median(int arr[], int start, int end) {
    for (int i = start + 1; i <= end; i++) {
        int key = arr[i];
        int j = i;
        while (arr[j - 1] > key && j > start) {
            arr[j] = arr[j - 1];
            j--;
        }
        arr[j] = key;
    }

    return arr[start + (end - start) / 2];
}


/**
 * @brief partition, takes passed value as pivot. 
 * First searches array for given pivot and swaps it with last element
 * then same as @basic_partition
 * 
 */
int partition(int arr[], int start, int end, int pivot_val) {
    for (int i = start; i <= end ; i++) {
        if (arr[i] == pivot_val) {
            swap(&arr[i], &arr[end]);
            break;
        }
    }

    int new_pivot = start;
    int pivot_value = arr[end];

    for (int j = start; j < end; j++) {
        if (arr[j] <= pivot_value) {
            if (new_pivot != j) {
                swap(&arr[new_pivot], &arr[j]);
            }
            new_pivot++;
        } 
    }
    swap(&arr[new_pivot], &arr[end]);

    return new_pivot;
}


/**
 * @brief Binary search for sorted arrays
 * Founds array index for given key value if exists
 * Otherwise returns -1
 *
 */
int binary_search(int arr[], int start, int end, int key_val) {
    if (end > 0) {
        int middle = start + (end - start) / 2;

        if (arr[middle] > key_val) return binary_search(arr, start, middle - 1, key_val);

        else if (arr[middle] < key_val) return binary_search(arr,middle + 1, end, key_val);

        else return middle;
    }

    return -1;
}


/**
 * @brief implementation of quick sort algorithm
 * sub arr size = 5 by default
 * pivot is choosen like in selection algorithm
 * 
 */
void new_quick_sort(int arr[], int start, int end, int sub_arr_size) {
    if (start < end) {
        int arr_size = end - start + 1;
        int median_arr_size = (arr_size + sub_arr_size - 1) / sub_arr_size;
        int median_arr[median_arr_size];

        int temp_start = start;
        int temp_end = start + sub_arr_size - 1;
        for (int i = 0; i < arr_size / sub_arr_size; i++) {
            median_arr[i] = insertion_sort_find_median(arr, temp_start, temp_end);
            
            temp_start += sub_arr_size;
            temp_end += sub_arr_size;
        }

        // when last part is shorter than sub_arr size
        if (arr_size % sub_arr_size != 0) { 
            median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
        }

        // find median of medians using same method
        int median_of_median = select(median_arr, 0, median_arr_size - 1, median_arr_size / 2, sub_arr_size);

        // estimate pivot using founded value
        int pivot = partition(arr, start, end, median_of_median);

        new_quick_sort(arr, start, pivot - 1, sub_arr_size);
        new_quick_sort(arr, pivot + 1, end, sub_arr_size);
    }
}


/**
 * @brief implementation of dual pivot quick sort algorithm
 * sub arr size is = 5 by default
 * one pivot is choosen like in selection algorithm the second is random
 * 
 */
void new_dual_pivot_QS(int arr[], int start, int end, int sub_arr_size) {
    if (start < end) {
        int arr_size = end - start + 1;
        int median_arr_size = (arr_size + sub_arr_size - 1) / sub_arr_size;
        int median_arr[median_arr_size];

        int temp_start = start;
        int temp_end = start + sub_arr_size - 1;
        for (int i = 0; i < arr_size / sub_arr_size; i++) {
            median_arr[i] = insertion_sort_find_median(arr, temp_start, temp_end);
            
            temp_start += sub_arr_size;
            temp_end += sub_arr_size;
        }

        // when last part is shorter than sub_arr size
        if (arr_size % sub_arr_size != 0) { 
            median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
        }

        // find median of medians using same method
        int median_of_median = select(median_arr, 0, median_arr_size - 1, median_arr_size / 2, sub_arr_size);
        
        int left_pivot, right_pivot;
        partition_DPQS(arr, start, end, &left_pivot, &right_pivot, median_of_median);

        new_dual_pivot_QS(arr, start, left_pivot - 1, sub_arr_size);
        new_dual_pivot_QS(arr, left_pivot + 1, right_pivot - 1, sub_arr_size);
        new_dual_pivot_QS(arr, right_pivot + 1, end, sub_arr_size);
    }
}


/**
 * @brief partition for dual pivot quick sort
 * changes right pivot, returns left pivot
 * 
 */
void partition_DPQS(int arr[], int start, int end, int *left_pivot, int *right_pivot, int pivot_val) {
    for (int i = start; i <= end ; i++) {
        if (arr[i] == pivot_val) {
            swap(&arr[i], &arr[end]);
            break;
        }
    }
    
    if (arr[start] > arr[end]) {
        swap(&arr[start], &arr[end]);
    }

    int left_pivot_val = arr[start]; 
    int right_pivot_val = arr[end];

    int new_left_pivot = start + 1;
    int new_right_pivot = end - 1;

    for(int i = start + 1; i <= new_right_pivot; i++) {
        if (arr[i] < left_pivot_val) {
            if (i != new_left_pivot) {
                swap(&arr[i], &arr[new_left_pivot]);
            }
            new_left_pivot++;
        }
        else if (arr[i] >= right_pivot_val) {
            while (arr[new_right_pivot] > right_pivot_val && i < new_right_pivot) {
                new_right_pivot--;
            }
           
            swap(&arr[i], &arr[new_right_pivot]);
            new_right_pivot--;

            if (arr[i] < left_pivot_val) {
                swap(&arr[i], &arr[new_left_pivot]);
                new_left_pivot++;
            }
        }
    }
    new_left_pivot--;
    new_right_pivot++;
 
    swap(&arr[start], &arr[new_left_pivot]);
    swap(&arr[end], &arr[new_right_pivot]);
 
    *right_pivot = new_right_pivot;
    *left_pivot = new_left_pivot;
}


/**
 * @brief Swaps values of two given integers 
 * 
 */
void swap(int *x, int *y) {
    int tmp = *x;
    *x = *y;
    *y = tmp;
}


/**
 * @brief generates random number in given range
 * and uses uniform distribution to do so
 * 
 */
int random(int start, int end) {
    uniform_int_distribution<> rand_val(start, end);
    return rand_val(mt_gen);
}


/**
 * @brief prints given array when its size is  < 50
 * 
 */
void print_array(int size, int arr[]) {
    if (size <= 50) {
        for(int i = 0; i < size; i++) {  
            cout << setw(2) << arr[i] << ", ";
        }
        cout << "\n\n";
    }
}


/**
 * @brief prints given fragment of the given array
 * 
 */
void print_array(int arr[], int start, int end) {
    if (end - start <= 50) {
        for (int i = start; i <= end; i++){
            cout << setw(2) << arr[i] << ", ";
        }
        cout << "\n\n";
    }
}