package myprograms.lab02;
import static java.lang.Math.sqrt;

public class Pieciokat extends Figura {

    double bok;

    Pieciokat(double b){
        this.bok = b;
        this.pole = ObliczPole();
        this.obwod = ObliczObwod();
    }

    @Override
    public double ObliczPole() {
        return (0.25 * (this.bok * this.bok) * (sqrt(25 + 10*sqrt(5))));
    }

    @Override
    public double ObliczObwod(){
        return (5 * this.bok);
    }

    @Override
    public void DrukujNazwe() {
        System.out.print("pieciokat");
    }
}
