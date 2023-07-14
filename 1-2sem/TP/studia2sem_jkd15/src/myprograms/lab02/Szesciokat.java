package myprograms.lab02;
import static java.lang.Math.sqrt;

public class Szesciokat extends Figura {
    double bok;

    Szesciokat(double b){
        this.bok = b;
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return (6 * (this.bok*this.bok) * 0.25 * sqrt(3));
    }

    @Override
    public double ObliczObwod() {
        return (6 * this.bok);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("szesciokat");
    }
}
