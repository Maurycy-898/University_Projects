#include "hamming.hpp"

Polynomial<GF<2>> hamming_encode(Polynomial<GF<2>> message) {
    return message * G; // 1 + x + x^3
}

Polynomial<GF<2>> hamming_decode(Polynomial<GF<2>> cipher) {
    Polynomial<GF<2>> reminder = (cipher % G);
    int bit_info = (reminder[0].getNum()) + (reminder[1].getNum()*2) + (reminder[2].getNum()*4);
    int bit_to_change;
    switch (bit_info) {
        case 1: bit_to_change = 1; break;
        case 2: bit_to_change = 2; break;
        case 3: bit_to_change = 4; break;
        case 4: bit_to_change = 7; break;
        case 5: bit_to_change = 3; break;
        case 6: bit_to_change = 6; break;
        case 7: bit_to_change = 5; break;
        default: return (cipher / G);
    }
    cipher[bit_to_change-1] = cipher[bit_to_change-1] + 1LL;
    return (cipher / G);
}


void as_bit_stream(Polynomial<GF<2>> p) {
    for (int i = 0; i <= p.degree(); i++)
        std::cout << p[i];
    std::cout << std::endl;
}
