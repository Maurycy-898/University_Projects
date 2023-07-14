package myprograms.lab02;

public class Kwadrat extends Czworokat{
    Kwadrat(double b) {
        super(b, b, b, b, 90);
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return (this.bok1 * this.bok1);
    }

    @Override
    public double ObliczObwod() {
        return (4 * this.bok1);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("kwadrat");
    }
}
