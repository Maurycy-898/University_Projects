#include <iostream>

using namespace std;

int main(int argc, char *argv[]) {
    if (argc > 1) {
        int n = strtol(argv[1], NULL, 10);
        cout << n << endl;

        for(int i = n; i > 0; i--) {
            cout << i << endl;
        }
    }
}
