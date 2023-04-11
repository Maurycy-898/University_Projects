#include "GF.hpp"

//----------------------------------------------------------------
// Constructor:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P>::GF (long long num) {
    this->num = ((num % P) < 0LL) ? num + P : num;  
}


//----------------------------------------------------------------
// Addition:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P> GF<P>::operator + (GF const &obj) const {
    return GF<P>(this->num + obj.num);
}

template <unsigned long long P> 
GF<P> GF<P>::operator + (long long num) const {
    return GF<P>(this->num + num);
}

template <unsigned long long P>
GF<P> operator + (long long num, GF<P> const &obj) {
    return GF<P>(num + obj.getNum());
}


//----------------------------------------------------------------
// Subtraction:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P> GF<P>::operator - (GF const &obj) const {
    return GF<P>(this->num - obj.num);
}

template <unsigned long long P>
GF<P> GF<P>::operator - (long long num) const {
    return GF<P>(this->num - num);
}

template <unsigned long long P>
GF<P> operator - (long long num, GF<P> const &obj) {
    return GF<P>(num - obj.getNum());
}


//----------------------------------------------------------------
// Multiplication:
//---------------------------------------------------------------- 
template <unsigned long long P>
GF<P> GF<P>::operator * (GF const &obj) const {
    return GF<P>(this->num * obj.num);
}

template <unsigned long long P>
GF<P> GF<P>::operator * (long long num) const {
    return GF<P>(this->num * (num % P));
}

template <unsigned long long P>
GF<P> operator * (long long num, GF<P> const &obj) {
    return GF<P>(num * obj.getNum());
}


//----------------------------------------------------------------
// Division:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P> GF<P>::operator / (GF const &obj) const {
    return (*this) * (obj.inv());
}

template <unsigned long long P>
GF<P> GF<P>::operator / (long long num) const {
    return (*this) * GF<P>(num).inv();
}

template <unsigned long long P>
GF<P> operator / (long long num, GF<P> const &obj) {
    return GF<P>(num) * obj.inv();
}


//----------------------------------------------------------------
// Exxponentiation:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P> GF<P>::operator ^ (GF const &obj) const {
    return (*this) ^ obj.num;
}

template <unsigned long long P>
GF<P> GF<P>::operator ^ (long long exp) const {
    long long num;
    if (exp < 0L) {
        exp = -exp;
        num = inv().getNum();
    } else { num = getNum(); }

    long long result = 1LL;
    while (exp > 0) {
        if ((exp & 1) != 0) 
            result = (result * num) % P; 
        num = (num * num) % P;
        exp = exp >> 1;
    }
    return GF<P>(result);
}

template <unsigned long long P>
GF<P> operator ^ (long long num, const GF<P>& obj) {
    return GF<P>(num) ^ obj;
}


//----------------------------------------------------------------
// Inversion:
//----------------------------------------------------------------
template <unsigned long long P>
GF<P> GF<P>::inv() const {
    if (num == 0L) throw "NoInvException";
    long long p = P, n = this->num;
    long long x = 1LL, x1 = 0LL, y = 0LL, y1 = 1LL, tmp, quotient;

    while (p != 0LL) {
        quotient = n / p;
        tmp = x; x = x1;
        x1 = tmp - quotient * x1;
        tmp = y; y = y1;
        y1 = tmp - quotient * y1;
        tmp = n; n = p;
        p = tmp % p;
    }

    if (x < 0LL) { x += P; }
    return GF<P>(x);
}


//----------------------------------------------------------------
// Output stream:
//----------------------------------------------------------------
template <unsigned long long P> std::ostream& operator << (std::ostream& stream, const GF<P>& obj) { 
    stream << obj.num;
    return stream;
}


//----------------------------------------------------------------
// Getter/Setter:
//----------------------------------------------------------------
template <unsigned long long P>
unsigned long long GF<P>::getNum() const {
    return this->num;
}

template <unsigned long long P>
void GF<P>::setNum(unsigned long long n) {
    this->num = ((n % P) < 0LL) ? n + P : n;
}
