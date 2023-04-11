package myprograms.lab03;

public class Prostokat extends Czworokat{

    Prostokat(double b1, double b2) {
        super(b1, b2, b1, b2, 90);
        this.pole = Figury.EnumTwo.ObliczPole(Figury.EnumTwo.PROSTOKAT, b1, b2);
        this.obwod = Figury.EnumTwo.ObliczObwod(Figury.EnumTwo.PROSTOKAT, b1, b2);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("prostokat");
    }
}
