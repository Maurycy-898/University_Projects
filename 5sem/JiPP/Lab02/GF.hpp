#ifndef GF_HPP
#define GF_HPP

#include <iostream>

class GF {
    private:
        unsigned long long num;
        
    
    public:
        static const long long P = 1234577891LL;

        GF (long long num);
        GF inv() const;
        
        GF operator + (GF const &obj) const;
        GF operator + (long long num) const;
        friend GF operator + (long long num, GF const &obj);
        
        GF operator - (GF const &obj) const;
        GF operator - (long long num) const;
        friend GF operator + (long long num, GF const &obj);

        GF operator * (GF const &obj) const;
        GF operator * (long long num) const;
        friend GF operator + (long long num, GF const &obj);

        GF operator / (GF const &obj) const;
        GF operator / (long long num) const;
        friend GF operator + (long long num, GF const &obj);

        GF operator ^ (GF const &obj) const;
        GF operator ^ (long long exp) const;
        friend GF operator + (long long num, GF const &obj);

        friend std::ostream & operator << (std::ostream &stream, GF const &obj);
        void setNum(unsigned long long n);
        unsigned long long getNum() const;
};

#endif // GF_HPP
