#ifndef GF_HPP
#define GF_HPP

#include <iostream>


template <const unsigned long long P> class GF {
    private:
        unsigned long long num;
    

    public:
    //----------------------------------------------------------------
    // Constructors:
    //----------------------------------------------------------------
        GF () { this->num = 0LL; }

        GF (long long num) {
            if (! is_Prime(P)) throw "IsNotPrimeException";
            this->num = ((num %= (long long) P) < 0LL)? (num + P) : num;  
        }
        

    //----------------------------------------------------------------
    // Addition:
    //----------------------------------------------------------------
        GF operator + (GF const &obj) const {
            return GF(this->num + obj.num);
        }

        GF operator + (long long num) const {
            return GF(this->num + (num %= (long long) P));
        }

        friend GF operator + (long long num, GF const &obj) {
            return GF((num %= (long long) P) + obj.getNum());
        }
        

    //----------------------------------------------------------------
    // Subtraction:
    //----------------------------------------------------------------
        GF operator - (GF const &obj) const {
            return GF(this->num - obj.num);
        }

        GF operator - (long long num) const {
            return GF(this->num - (num %= (long long) P));
        }

        friend GF operator - (long long num, GF const &obj) {
            return GF((num %= (long long) P) - obj.getNum());
        }


    //----------------------------------------------------------------
    // Multiplication:
    //---------------------------------------------------------------- 
        GF operator * (GF const &obj) const {
            return GF(this->num * obj.num);
        }

        GF operator * (long long num) const {
            return GF(this->num * (num %= (long long) P));
        }

        friend GF operator * (long long num, GF const &obj) {
            return GF((num %= (long long) P) * obj.getNum());
        }


    //----------------------------------------------------------------
    // Division:
    //----------------------------------------------------------------
        GF operator / (GF const &obj) const {
            return (*this) * (obj.inv());
        }

        GF operator / (long long num) const {
            return (*this) * GF(num).inv();
        }

        friend GF operator / (long long num, GF const &obj) {
            return GF(num) * obj.inv();
        }


    //----------------------------------------------------------------
    // Modulo:
    //----------------------------------------------------------------
        GF operator % (GF const &obj) const {
            return GF(this->num % obj.getNum());
        }

        GF operator % (long long num) const {
            return GF(this->num % num);
        }

        friend GF operator % (long long num, GF const &obj) {
            return GF(num % obj.getNum());
        }


    //----------------------------------------------------------------
    // Exponentiation:
    //----------------------------------------------------------------
        GF operator ^ (GF const &obj) const {
            return (*this) ^ obj.num;
        }

        GF operator ^ (long long exp) const {
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

        friend GF operator ^ (long long num, GF const &obj) {
            return GF(num) ^ obj;
        }


    //----------------------------------------------------------------
    // Substitution:
    //----------------------------------------------------------------
        GF operator = (GF const &obj) {
            this->num = obj.getNum();
            return (*this);
        }

        GF operator = (long long num) {
            this->num = ((num %= (long long) P) < 0LL)? (num + P) : num;
            return (*this);
        }

        operator long long() {
            return this->num;
        }

    //----------------------------------------------------------------
    // Equality:
    //----------------------------------------------------------------
        bool operator == (GF const &obj) const {
            return (obj.getNum() == this->num);
        }

        bool operator == (long long num) const {
            return (num == (long long) this->num);
        }

        friend bool operator == (long long num, GF const &obj) {
            return (num == obj.getNum);
        }


    //----------------------------------------------------------------
    // Inquality:
    //----------------------------------------------------------------
        bool operator != (GF const &obj) const {
            return (obj.getNum() != this->num);
        }

        bool operator != (unsigned long long num) const {
            return (num != this->num);
        }

        friend bool operator != (unsigned long long num, GF const &obj) {
            return (num != obj.getNum());
        }


    //----------------------------------------------------------------
    // Inversion:
    //----------------------------------------------------------------
        GF inv() const {
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


    //----------------------------------------------------------------
    // Checking if Prime:
    //----------------------------------------------------------------
        bool is_Prime(unsigned long long n) {
            if (n <= 1) return false;
            if (n <= 3) return true;

            if (n % 2 == 0 || n % 3 == 0) return false;

            for (long long i = 5LL; i * i <= n; i = i + 6) {
                if (n % i == 0LL || n % (i + 2LL) == 0LL) return false;
            }

            return true;
        }


    //----------------------------------------------------------------
    // Output stream:
    //----------------------------------------------------------------
        friend std::ostream& operator << (std::ostream &stream, GF const &obj) { 
            stream << obj.getNum();
            return stream;
        }


    //----------------------------------------------------------------
    // Getter/Setter:
    //----------------------------------------------------------------
        unsigned long long getNum() const {
            return this->num;
        }

        void setNum(unsigned long long num) {
            this->num = ((num %= (long long) P) < 0LL) ? num + P : num;;
        }
};


#endif // GF_HPP
