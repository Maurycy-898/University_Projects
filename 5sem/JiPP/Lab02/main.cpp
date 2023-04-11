#include "GF.hpp"


int main(void) {
    long long x, y;
    char operand;

    while(true) {
        std::cin >> x;
        std::cin >> y;
        std::cin >> operand;

        GF a = GF(x); GF b = GF(y);
        switch (operand) {

        case '+': std::cout << (a + b) << std::endl; break;       
        case '-': std::cout << (a - b) << std::endl; break;      
        case '*': std::cout << (a * b) << std::endl; break;       
        case '/': std::cout << (a / b) << std::endl; break;      
        case '^': std::cout << ( a^y ) << std::endl; break;

        default: return 0;
        }
        
        std::cout << std::endl;
   }
    return 0;
}
