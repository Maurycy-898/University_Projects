#include "Polynomials.hpp"
#include "../Zad1/GF.hpp"


int main(void) {
    Polynomial<int> a({1, 5, 3, 0, 2});
    Polynomial<int> b(vector<int> ({2, 0, 2, 1}));


    cout << "a : " << a << endl;
    cout << "b : " << b << endl;
    cout << "b : " << b << endl;
    cout << "a + b : " << (a + b) << endl;
    cout << "a - b : " << (a - b) << endl;
    cout << "a * b : " << (a * b) << endl;
    cout << "a / b : " << (a / b) << endl;
    cout << "a % b : " << (a % b) << endl;

    return 0;
}
