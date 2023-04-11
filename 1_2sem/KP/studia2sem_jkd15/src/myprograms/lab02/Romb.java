package myprograms.lab02;
import static java.lang.Math.sin;

public class Romb extends Czworokat{

    Romb(double b, double k) {
        super(b, b, b, b, k);
        if (k>90){k = 180 - k; this.kat = k;}
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return (this.bok1*this.bok1) * sin(kat);
    }

    @Override
    public double ObliczObwod() {
        return (4 * this.bok1);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("romb");
    }
}
