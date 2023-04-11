package myprograms.lab02;

public class Kolo extends Figura {
    int promien;

    Kolo(int r){
        this.promien = r;
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return ((this.promien*this.promien) * 3.14);
    }

    @Override
    public double ObliczObwod() {
        double pi = 3.1415;
        return (2 * this.promien * pi);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("kolo");
    }
}
