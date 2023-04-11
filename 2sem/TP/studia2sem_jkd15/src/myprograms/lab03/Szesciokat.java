package myprograms.lab03;
import static java.lang.Math.sqrt;

public class Szesciokat extends Figura {
    double bok;

    Szesciokat(double b){
        this.bok = b;
        this.pole = Figury.EnumOne.ObliczPole(Figury.EnumOne.SZESCIOKAT, b);
        this.obwod = Figury.EnumOne.ObliczObwod(Figury.EnumOne.SZESCIOKAT, b);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("szesciokat");
    }
}
