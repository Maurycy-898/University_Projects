#ifndef GF_HPP
#define GF_HPP

#include <iostream>


template <const unsigned long long  P> class GF {
    private:
        unsigned long long num;
        
    
    public:
        GF (long long num);
        GF inv() const;
        
        GF operator + (GF const &obj) const;
        GF operator + (long long num) const;
        friend GF operator+ <> (long long num, GF const &obj);
        
        GF operator - (GF const &obj) const;
        GF operator - (long long num) const;
        friend GF operator- <> (long long num, GF const &obj);

        GF operator * (GF const &obj) const;
        GF operator * (long long num) const;
        friend GF<P> operator* <> (long long num, GF<P> const &obj);

        GF operator / (GF const &obj) const;
        GF operator / (long long num) const;
        friend GF<P> operator/ <> (long long num, GF<P> const &obj);

        GF operator ^ (GF const &obj) const;
        GF operator ^ (long long exp) const;
        friend GF<P> operator^ <> (long long num, const GF<P>& obj);

        friend std::ostream& operator<< <> (std::ostream& stream, const GF<P>& obj);
        
        void setNum(unsigned long long n);
        unsigned long long getNum() const;
};

#endif // GF_HPP
