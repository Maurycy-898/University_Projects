#include "GF.hpp"

int main(void) {
    const unsigned long long p = 37;
    GF<p> z = {};
    std::cout << z << std::endl;
    long long x, y;
    char op;

    while(true) {
        std::cout << "Type a, <>, b, to calculate a <> b  (mod " << p << "):\n" << std::endl;
        
        std::cin >> x;
        std::cin >> op;
        std::cin >> y;

        GF<p> a = GF<p>(x);
        GF<p> b = GF<p>(y);

        std::cout << a << " " << op << " " << b << " = ";
        
        switch (op) {
            case '+': std::cout << (a + b) << "  (mod "<< p << ")" << std::endl; break;       
            case '-': std::cout << (a - b) << "  (mod "<< p << ")" << std::endl; break;      
            case '*': std::cout << (a * b) << "  (mod "<< p << ")" << std::endl; break;       
            case '/': std::cout << (a / b) << "  (mod "<< p << ")" << std::endl; break;      
            case '^': std::cout << (a ^ b) << "  (mod "<< p << ")" << std::endl; break;
            default: return 0; // End if incorrect data input.
        }

        std::cout << "\n" << std::endl;
    }

    return 0;
}
