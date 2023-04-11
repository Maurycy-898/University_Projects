#ifndef POLYNOMIALS_HPP
#define POLYNOMIALS_HPP

#include <iostream>
#include <vector>

using std::cout, std::endl, std::string, std::vector;


template <typename T> class Polynomial {
    private:
        vector<T> coefficients; // Polynomial coefficients

    public:
    //----------------------------------------------------------------
    // Constructors:
    //----------------------------------------------------------------
        Polynomial() {
            this->coefficients = vector<T>(0);
        }

        Polynomial(vector<T> coefficients) {
            this->coefficients = vector<T>(coefficients);
        }

        Polynomial(T coeff, int degree) {
            vector<T> coeffs = vector<T>(0);
            coeffs.resize(degree + 1);
            coeffs[degree] = coeff;
            this->coefficients = coeffs;
        }


    //----------------------------------------------------------------
    // Get this Polynomial degree
    //----------------------------------------------------------------
        int degree() const {
            return this->coefficients.size() - 1;
        }

    //----------------------------------------------------------------
    // Addition:
    //----------------------------------------------------------------
        Polynomial operator+ (const Polynomial& other) const {
            int n = std::max(degree(), other.degree());
            
            vector<T> this_coef(this->coefficients); this_coef.resize(n + 1);
            vector<T> other_coef(other.get_coefficients()); other_coef.resize(n + 1);

            vector<T> result(n + 1);

            for (int i = 0; i <= n; i++) 
                result[i] = this_coef[i] + other_coef[i];

            return Polynomial(result);
        }

        Polynomial operator+ (T value) const {
            vector<T> result(this->coefficients);
            result[0] = result[0] + value;
            return Polynomial(result);
        }

    //----------------------------------------------------------------
    // Substitution:
    //----------------------------------------------------------------
        Polynomial operator- (const Polynomial &other) const {
            int n = std::max(degree(), other.degree());
            
            vector<T> this_coef(this->coefficients); this_coef.resize(n + 1);
            vector<T> other_coef(other.get_coefficients()); other_coef.resize(n + 1);

            vector<T> result(n + 1);

            for (int i = 0; i <= n; i++) 
                result[i] = this_coef[i] - other_coef[i];

            while(result.back() == T()) result.pop_back();
            
            return Polynomial(result);
        }

        Polynomial operator- (T value) const {
            vector<T> result(this->coefficients);
            result[0] = result[0] - value;
            return Polynomial(result);
        }

    //----------------------------------------------------------------
    // Multiplication:
    //----------------------------------------------------------------
        Polynomial operator* (const Polynomial &other) const { 
            int n = degree() + other.degree();
        
            vector<T> result(n + 1);

            vector<T> this_coef(this->coefficients);
            vector<T> other_coef(other.get_coefficients());

            for (int i = 0; i < this_coef.size(); i++) {
                for (int j = 0; j < other_coef.size(); j++) {
                    result[i + j] = result[i + j] + (this_coef[i] * other_coef[j]);
                }
            } 
            return Polynomial(result);
        }

        Polynomial operator* (T value) const {
            vector<T> result(this->coefficients);
            for (unsigned int i = 0; i <= this->degree; i++)
                result[i] = result[i] * value;
            return Polynomial(result);
        }

    //----------------------------------------------------------------
    // Division:
    //----------------------------------------------------------------
        Polynomial operator/ (const Polynomial& other) const { 
            if (other.degree() < 0) throw "Division by zero";

            if (this->coefficients.size() == 0) return Polynomial();

            if (degree() < other.degree()) return Polynomial();

            T quotient_coeff = (*this)[degree()] / other[other.degree()];
            Polynomial quotient_term(quotient_coeff, (degree() - other.degree()));
            Polynomial remainder = (*this) - (other * quotient_term);

            return quotient_term + (remainder / other);
        }

        Polynomial operator/ (T value) const {
            vector<T> result(this->coefficients);
            for (unsigned int i = 0; i <= this->degree; i++)
                result[i] = result[i] / value;
            return Polynomial(result);
        }

    //----------------------------------------------------------------
    // Modulo:
    //----------------------------------------------------------------
        Polynomial operator% (const Polynomial& other) const { 
            if (other.degree() < 0) throw "Division by zero";

            if (coefficients.size() == 0) return Polynomial();

            if (degree() < other.degree()) return (*this);

            T quotient_coeff = (*this)[degree()] / other[other.degree()];
            Polynomial quotient_term(quotient_coeff, (degree() - other.degree()));
            Polynomial remainder = (*this) - (other * quotient_term);

            return remainder;
        }

        Polynomial operator% (T value) const {
            vector<T> result(this->coefficients);
            for (unsigned int i = 0; i <= this->degree; i++)
                result[i] = result[i] % value;
            return Polynomial(result);
        }

    //----------------------------------------------------------------
    // Substitution:
    //----------------------------------------------------------------
        Polynomial operator= (const Polynomial& other) {
            vector<T> result(other.get_coefficients());
            (*this).set_coefficients(result);
            return (*this);
        }

        Polynomial operator= (T value) {
            this->coefficients[0] = value;
            this->coefficients.resize(1);
            return (*this);
        }

    //----------------------------------------------------------------
    // Equality / Inequality:
    //----------------------------------------------------------------
        bool operator== (const Polynomial& other) const {
            if (degree() != other.degree()) return false;
            for (unsigned int i = 0; i <= degree(); i++) {
                if ((*this)[i] != other[i]) return false;
            }
            return true;
        }

        bool operator!= (Polynomial const &other) const {
            if (degree() != other.degree()) return true;
            for (unsigned int i = 0; i <= degree(); i++) {
                if ((*this)[i] != other[i]) return true;
            }
            return false;
        }

    //----------------------------------------------------------------
    // Access / Main Method:
    //----------------------------------------------------------------
        T operator[] (unsigned int i) const {
            return this->coefficients[i];
        }

        T& operator[] (unsigned int i) {
            return this->coefficients[i];
        }

        T operator() (T x) const {
            T value = this->coefficients[degree()];
            for (unsigned int i = degree() - 1; i >= 0; i--)
                value = x * value + coefficients [i];
            return value;
        }

    //----------------------------------------------------------------
    // Output stream:
    //----------------------------------------------------------------
        friend std::ostream& operator<< (std::ostream& stream, const Polynomial& poly) { 
            if (poly.degree() >= 0) stream << poly[0];
            if (poly.degree() >= 1 && poly[1] != T()) stream << " + " << poly[1] << "x";
            for (int i = 2; i <= poly.degree(); i++) 
                if (poly[i] != T()) stream << " + " << poly[i] << "x^" << i;
            return stream;
        }

    //----------------------------------------------------------------
    // Getters / Setters:
    //----------------------------------------------------------------
        vector<T> get_coefficients() const {
            return this->coefficients;
        }

        void set_coefficients(vector<T> coef) {
            this->coefficients = coef;
        }
};

#endif // POLYNOMIALS_HPP
