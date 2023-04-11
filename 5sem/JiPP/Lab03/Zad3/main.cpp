#include "hamming.hpp"

int main(void) {
    Polynomial<GF<2>> message({GF<2>(1), GF<2>(1), GF<2>(1), GF<2>(1)});
    as_bit_stream(message);

    Polynomial<GF<2>> encoded = hamming_encode(message);
    as_bit_stream(encoded);

    // Psujemy bit
    encoded[2] = encoded[2] + 1LL;
    as_bit_stream(encoded);

    Polynomial<GF<2>> decoded = hamming_decode(encoded);
    as_bit_stream(decoded);
}