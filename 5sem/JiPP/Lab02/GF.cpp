#include "GF.hpp"

/** @brief Construct a new GF object */
GF::GF (long long num) {
    num = num % P;
    this->num = (num < 0LL) ? num + P : num;
}


GF GF::operator + (GF const &obj) const {
    return GF((this->num + obj.num) % P);
}

GF GF::operator + (long long num) const {
    num %= P;
    return GF((this->num + num) % P);
}

GF operator + (long long num, GF const &obj) {
    return GF(num + obj.getNum());
}


GF GF::operator - (GF const &obj) const {
    long long result = this->num - obj.num;
    if (result < 0LL) result += P;
    return GF(result);
}

GF GF::operator - (long long num) const {
    num %= P;
    long long result = this->num - num;
    if (result < 0LL) result += P;
    return GF(result);
}

GF operator - (long long num, GF const &obj) {
    return GF(num - obj.getNum());
}


GF GF::operator * (GF const &obj) const {
    return GF((this->num * obj.num) % P);
}

GF GF::operator * (long long num) const {
    num %= P;
    return GF((this->num * num) % P);
}

GF operator * (long long num, GF const &obj) {
    return GF(num * obj.getNum());
}


GF GF::operator / (GF const &obj) const {
    return *this * (obj.inv());
}

GF GF::operator / (long long num) const {
    return *this * GF(num).inv();
}

GF operator / (long long num, GF const &obj) {
    return GF(num) * obj.inv();
}


GF GF::operator ^ (GF const &obj) const {
    return *this ^ obj.num;
}

GF GF::operator ^ (long long exp) const {
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
    return GF(result);
}

GF operator ^ (long long num, GF const &obj) {
    return GF(num) ^ obj;
}


GF GF::inv() const {
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
    return GF(x);
}

std::ostream & operator << (std::ostream &stream, GF const &obj) { 
    stream << obj.num;
    return stream;
}


unsigned long long GF::getNum() const {
    return this->num;
}

void GF::setNum(unsigned long long n) {
        this->num = n;
}
