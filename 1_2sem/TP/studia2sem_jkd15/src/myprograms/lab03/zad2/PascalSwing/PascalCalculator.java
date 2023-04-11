package myprograms.lab03.zad2.PascalSwing;

public class PascalCalculator {
    int pascalValue;
    PascalCalculator(int w, int k){
        pascalValue = value(w,k);
    }
    long factorial(int n) {
        long result = 1L;
        for (int i = n; i > 0; i--) {
            result *= i;
        }
        return result;
    }

    int value(int w, int l) {
        return Math.toIntExact(factorial(w) / (factorial(l) * factorial(w - l)));
    }
}
