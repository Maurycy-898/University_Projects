#ifndef HAMMING_HPP
#define HAMMING_HPP

#include <iostream>
#include "../Zad1/GF.hpp"
#include "../Zad2/Polynomials.hpp"

const Polynomial<GF<2>> G = Polynomial<GF<2>>({1, 1, 0, 1});

Polynomial<GF<2>> hamming_encode(Polynomial<GF<2>> message);

Polynomial<GF<2>> hamming_decode(Polynomial<GF<2>> cipher);

void as_bit_stream(Polynomial<GF<2>> p);

#endif // _HAMMING_HPP
