package myprograms.lab01;

public class WierszTrojkataPascala {

    int[] wiersz;

    WierszTrojkataPascala(int n) {
        this.wiersz = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            this.wiersz[i] = wartosc(n, i);
        }
    }
    int silnia(int n) {
        int wynik = 1;
        for (int i = n; i > 0; i--) {
            wynik *= i;
        }
        return wynik;
    }

    int wartosc(int w, int l) {
        return silnia(w) / (silnia(l) * silnia(w - l));
    }
}

