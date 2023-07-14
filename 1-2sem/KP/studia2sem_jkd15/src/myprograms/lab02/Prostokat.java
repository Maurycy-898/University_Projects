package myprograms.lab02;

public class Prostokat extends Czworokat{

    Prostokat(double b1, double b2) {
        super(b1, b2, b1, b2, 90);
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return (this.bok1 * this.bok2);
    }

    @Override
    public double ObliczObwod() {
        return (2*this.bok1 + 2* this.bok2);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("prostokat");
    }
}
