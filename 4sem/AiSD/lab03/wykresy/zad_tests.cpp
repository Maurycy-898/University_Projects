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
void new_quick_sort(int arr[], int start, int end, int sub_arr_size);
void new_dual_pivot_QS(int arr[], int start, int end, int sub_arr_size);
void partition_DPQS(int arr[], int start, int end,int *left_pivot, int *right_pivot, int pivot_val);

// old sort algorithms to compare
void quick_sort(int arr[], int start, int end);
void dual_pivot_QS(int arr[], int start, int end);
void partition_DP_QS(int arr[], int start, int end,int *left_pivot, int *right_pivot);

// for binary search tests (does not count swaps and comparisons)
void NC_dual_pivot_QS(int arr[], int start, int end);
void NC_partition_DP_QS(int arr[], int start, int end, int *right_pivot, int *left_pivot);

// helpful functions
void swap(int *x, int *y);

// testing functions
void test_selects();
void test_binary();
void test_sorts();


// GLOBAL VARS (COUNTERS)
int COMP_COUNT = 0;
int SWAP_COUNT = 0;



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char **argv) {
   if (argc > 1) {
        if (strcmp(argv[1], "test_selects") == 0) {
            test_selects();
        } 
        else if (strcmp(argv[1], "test_binary") == 0) {
            test_binary();
        }
        else if (strcmp(argv[1], "test_sorts") == 0) {
            test_sorts();
        } 
    }
    return 0;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// TESTS
void test_selects() {
    int n = 10000;
    int k = 100;

    long int TIME = 0L;
    clock_t tmp_time = 0;

    cout << "rand_select (cmp);rand_select (swap);rand_select (time);select_3 (cmp);select_3 (swap);select_3 (time);";
    cout << "select_5 (cmp);select_5 (swap);select_5 (time);select_7 (cmp);select_7 (swap);select_7 (time);select_9 (cmp);select_9 (swap);select_9 (time);" << endl;
    
    for (int i = 1; i <= n; i++) {
        int array[i];
        int position = random(0, i - 1);

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            random_select(array, 0, i - 1, position);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            select(array, 0, i - 1, position, 3);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            select(array, 0, i - 1, position, 5);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            select(array, 0, i - 1, position, 7);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            select(array, 0, i - 1, position, 9);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME << endl;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;
    }
}

void test_binary() {
    int n = 50000;
    int k = 100;

    long int TIME = 0L;
    clock_t tmp_time = 0;

    cout << "binary_search (start)(cmp);binary_search (start)(time);binary_search (middle)(cmp);binary_search (middle)(time);binary_search (end)(cmp);binary_search (end)(time);";
    cout << "binary_search (random)(cmp);binary_search (random)(time);binary_search (not_existing)(cmp);binary_search (not_existing)(time);" << endl;

    for (int i = 1; i <= n; i++) {
        int* array = new int[i];
        int key;

        for (int j = 0; j < k; j++) {
            for (int p = 0; p < i; p++) {
                array[p] = p;
            }
            key = random(0, (2 * i) / 10); // near start
            tmp_time = clock();
            binary_search(array, 0, i - 1, array[key]);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        TIME /= k;
        cout << COMP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        TIME = 0L;

        for (int j = 0; j < k; j++) {
            for (int p = 0; p < i; p++) {
                array[p] = p;
            }
            key = random((4 * i) / 10, (6 * i) / 10); // near middle
            tmp_time = clock();
            binary_search(array, 0, i - 1, array[key]);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        TIME = 0L;

        for (int j = 0; j < k; j++) {
            for (int p = 0; p < i; p++) {
                array[p] = p;
            }
            key = random((8 * i) / 10, i - 1); // near end
            tmp_time = clock();
            binary_search(array, 0, i - 1, array[key]);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        TIME = 0L;

        for (int j = 0; j < k; j++) {
            for (int p = 0; p < i; p++) {
                array[p] = p;
            }
            
            key = random(0, i - 1); // random
            tmp_time = clock();
            binary_search(array, 0, i - 1, array[key]);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        TIME = 0L; 

        for (int j = 0; j < k; j++) {
            for (int p = 0; p < i; p++) {
                array[p] = p;
            }
            key = -1; // non existing
            tmp_time = clock();
            binary_search(array, 0, i - 1, array[key]);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << TIME << ";" << endl;
        COMP_COUNT = 0;
        TIME = 0L; 
    }
}

void test_sorts() {
    int n = 10000;
    int k = 100;

    long int TIME = 0L;
    clock_t tmp_time = 0;

    cout << "quick_sort (cmp);quick_sort (swap);quick_sort (time);select_QS (cmp);select_QS (swap);select_QS (time);";
    cout << "dual_pivot_QS (cmp);dual_pivot_QS (swap);dual_pivot_QS (time);select_DPQS (cmp);select_DPQS (swap);select_DPQS (time);";
    cout << "quick_sort(worst_case) (cmp);quick_sort(worst_case) (swap);quick_sort(worst_case) (time);select_QS(worst_case) (cmp);select_QS(worst_case) (swap);select_QS(worst_case) (time);" << endl;
    
    for (int i = 1; i <= n; i++) {
        int array[i];

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            quick_sort(array, 0, i - 1);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            new_quick_sort(array, 0, i - 1, 5);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            dual_pivot_QS(array, 0, i - 1);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            for(int j = 0; j < i; j++) {
                array[j] = random(0, ((2 * i) - 1));
            }
            tmp_time = clock();
            new_dual_pivot_QS(array, 0, i - 1, 5);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            int val = i;
            for(int j = 0; j < i; j++) {
                array[j] = val;
                val --;
            }
            tmp_time = clock();
            quick_sort(array, 0, i - 1);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;

        for(int p = 0; p < k; p++) {
            int val = i;
            for(int j = 0; j < i; j++) {
                array[j] = val;
                val --;
            }
            tmp_time = clock();
            new_quick_sort(array, 0, i - 1, 5);
            TIME += clock() - tmp_time;
        }
        COMP_COUNT /= k;
        SWAP_COUNT /= k;
        TIME /= k;
        cout << ";" << COMP_COUNT << ";" << SWAP_COUNT << ";" << TIME << ";" << endl;
        COMP_COUNT = 0;
        SWAP_COUNT = 0;
        TIME = 0;
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * @brief returns element that would be on given position in the sorted array
 * given position stat should be between [0...(n - 1)] where n is the array size
 * In other worlds returns @position_stat smallest element (starting from zero)
 */
int random_select(int arr[], int start, int end, int position_stat) {
    COMP_COUNT++;
    if (start == end) {
        return arr[start];
    }

    int pivot = basic_partition(arr, start, end);

    int pivot_position_stat = pivot - start;
    
    if (pivot_position_stat > position_stat) {
        COMP_COUNT ++;
        return random_select(arr, start, pivot - 1, position_stat);
    }
    else if (pivot_position_stat < position_stat) {
        COMP_COUNT += 2;
        return random_select(arr, pivot + 1, end, position_stat - pivot_position_stat - 1);
    }
    else {
        COMP_COUNT += 2;
        return arr[pivot];
    }
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
            COMP_COUNT++;
        } 
        COMP_COUNT += 2;
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
int select(int arr[], int start, int end, int position_stat, int sub_arr_size) {
    COMP_COUNT++;
    if (start == end) {
        return arr[start];
    }

    int arr_size = end - start + 1;
    int median_arr_size = (arr_size + sub_arr_size - 1) / sub_arr_size;
    int median_arr[median_arr_size];

    int temp_start = start;
    int temp_end = start + sub_arr_size - 1;
    for (int i = 0; i < arr_size / sub_arr_size; i++) {
        median_arr[i] = insertion_sort_find_median(arr, temp_start, temp_end);
            
        temp_start += sub_arr_size;
        temp_end += sub_arr_size;

        SWAP_COUNT ++;
        COMP_COUNT ++;
    }

    // when last part is shorter than sub_arr size
    if (arr_size % sub_arr_size != 0) { 
        median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
        SWAP_COUNT ++;
    }
    COMP_COUNT++;

    // find median of medians using same method
    int median_of_median = select(median_arr, 0, (median_arr_size - 1), (median_arr_size - 1) / 2, sub_arr_size);

    int pivot = partition(arr, start, end, median_of_median);

    int pivot_position_stat = pivot - start;

    if (pivot_position_stat > position_stat) {
        COMP_COUNT ++;
        return select(arr, start, pivot - 1, position_stat, sub_arr_size);
    }
    else if (pivot_position_stat < position_stat) {
        COMP_COUNT += 2;
        return select(arr, pivot + 1, end, position_stat - pivot_position_stat - 1, sub_arr_size);
    }
    else {
        COMP_COUNT += 2;
        return arr[pivot];
    }
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

            SWAP_COUNT ++;
            COMP_COUNT += 2;
        }
        arr[j] = key;

        SWAP_COUNT ++;
        COMP_COUNT ++;
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
        COMP_COUNT += 2;
    }

    int new_pivot = start;
    int pivot_value = arr[end];

    for (int j = start; j < end; j++) {
        if (arr[j] <= pivot_value) {
            if (new_pivot != j) {
                swap(&arr[new_pivot], &arr[j]);
            }
            new_pivot++;
            COMP_COUNT ++;
        } 
        COMP_COUNT += 2;
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
    COMP_COUNT ++;
    if (end > 0) {
        int middle = start + (end - start) / 2;

        if (arr[middle] > key_val) {
            COMP_COUNT ++;
            return binary_search(arr, start, middle - 1, key_val);
        }
        else if (arr[middle] < key_val) {
            COMP_COUNT += 2;
            return binary_search(arr,middle + 1, end, key_val);
        }
        else {
            COMP_COUNT += 2;
            return 1;
        }
    }

    return 0;
}


/**
 * @brief implementation of quick sort algorithm
 * sub arr size is most likely = 5
 * pivot is choosen like in selection algorithm
 * 
 */
void new_quick_sort(int arr[], int start, int end, int sub_arr_size) {
    COMP_COUNT ++;
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

            SWAP_COUNT ++;
            COMP_COUNT ++;
        }

        // when last part is shorter than sub_arr size
        if (arr_size % sub_arr_size != 0) { 
            median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
        }
        COMP_COUNT ++;

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
 * sub arr size is most likely = 5
 * one pivot is choosen like in selection algorithm the second is random
 * 
 */
void new_dual_pivot_QS(int arr[], int start, int end, int sub_arr_size) {
    COMP_COUNT ++;
    if (start < end) {
        int arr_size = end - start + 1;
        int median_arr_size = (arr_size + sub_arr_size - 1) / sub_arr_size;
        int median_arr[median_arr_size];

        int temp_start = start;
        int temp_end = start + sub_arr_size;
        for (int i = 0; i < arr_size / sub_arr_size; i++) {
            median_arr[i] = insertion_sort_find_median(arr, temp_start, temp_end);
            
            temp_start += sub_arr_size;
            temp_end += sub_arr_size;

            SWAP_COUNT ++;
            COMP_COUNT ++;
        }

        // when last part is shorter than sub_arr size
        if (arr_size % sub_arr_size != 0) { 
            median_arr[median_arr_size - 1] = insertion_sort_find_median(arr, temp_start, end);
        }
        COMP_COUNT ++;

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
void partition_DPQS(int arr[], int start, int end,int *left_pivot, int *right_pivot, int pivot_val) {
    for (int i = start; i <= end ; i++) {
        if (arr[i] == pivot_val) {
            swap(&arr[i], &arr[end]);
            break;
        }
        COMP_COUNT += 2;
    }
    
    if (arr[start] > arr[end]) {
        swap(&arr[start], &arr[end]);
    }
    COMP_COUNT ++;

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
            COMP_COUNT ++;
        }
        else if (arr[i] >= right_pivot_val) {
            while (arr[new_right_pivot] > right_pivot_val && i < new_right_pivot) {
                new_right_pivot--;
                COMP_COUNT += 2;
            }
           
            swap(&arr[i], &arr[new_right_pivot]);
            new_right_pivot--;

            if (arr[i] < left_pivot_val) {
                swap(&arr[i], &arr[new_left_pivot]);
                new_left_pivot++;
            }
            COMP_COUNT ++;
        }
        COMP_COUNT += 2;
    }
    new_left_pivot--;
    new_right_pivot++;
 
    swap(&arr[start], &arr[new_left_pivot]);
    swap(&arr[end], &arr[new_right_pivot]);
 
    *right_pivot = new_right_pivot;
    *left_pivot = new_left_pivot;
}


void quick_sort(int arr[], int start, int end) {
    COMP_COUNT ++;
    if (start < end) {
        int pivot = basic_partition(arr, start, end);

        quick_sort(arr, start, pivot - 1);
        quick_sort(arr, pivot + 1, end);
    }
}


void dual_pivot_QS(int arr[], int start, int end) {
    COMP_COUNT ++;
    if (start < end) {    
        int left_pivot, right_pivot;
        partition_DP_QS(arr, start, end, &left_pivot, &right_pivot);

        dual_pivot_QS(arr, start, left_pivot - 1);
        dual_pivot_QS(arr, left_pivot + 1, right_pivot - 1);
        dual_pivot_QS(arr, right_pivot + 1, end);
    }
}


void partition_DP_QS(int arr[], int start, int end,int *left_pivot, int *right_pivot) {
    if (arr[start] > arr[end]) {
        swap(&arr[start], &arr[end]);
    }
    COMP_COUNT ++;

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
            COMP_COUNT ++;
        }
        else if (arr[i] >= right_pivot_val) {
            while (arr[new_right_pivot] > right_pivot_val && i < new_right_pivot) {
                new_right_pivot--;
                COMP_COUNT += 2;
            }
           
            swap(&arr[i], &arr[new_right_pivot]);
            new_right_pivot--;

            if (arr[i] < left_pivot_val) {
                swap(&arr[i], &arr[new_left_pivot]);
                new_left_pivot++;
            }
            COMP_COUNT ++;
        }
        COMP_COUNT += 2;
    }
    new_left_pivot--;
    new_right_pivot++;
 
    swap(&arr[start], &arr[new_left_pivot]);
    swap(&arr[end], &arr[new_right_pivot]);
 
    *right_pivot = new_right_pivot;
    *left_pivot = new_left_pivot;
}


void NC_dual_pivot_QS(int arr[], int start, int end) {
    if (start < end) {
        int left_pivot;
        int right_pivot;

        NC_partition_DP_QS(arr, start, end, &right_pivot, &left_pivot);

        NC_dual_pivot_QS(arr, start, left_pivot - 1);
        NC_dual_pivot_QS(arr, left_pivot + 1, right_pivot - 1);
        NC_dual_pivot_QS(arr, right_pivot + 1, end);
    }
}


void NC_partition_DP_QS(int arr[], int start, int end, int *right_pivot, int *left_pivot) {
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
    SWAP_COUNT += 2;
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
