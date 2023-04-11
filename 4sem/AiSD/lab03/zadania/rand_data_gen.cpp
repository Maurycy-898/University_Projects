#include <iostream>
#include <time.h>
#include <random>
#include <stdlib.h>

using namespace std;

int main(int argc, char *argv[]) {
    if (argc > 1) {
        int n = strtol(argv[1], NULL, 10);
        cout << n << endl;

        random_device rd;
        mt19937 mt_gen(rd());
        uniform_int_distribution<> distribution(0, (2*n - 1));

        int rand_num;
        for(int i = 0; i < n; i++) {
            rand_num = distribution(mt_gen);
            cout << rand_num << endl;
        }
    }
}