package myprograms.lab03;

public class Romb extends Czworokat{

    Romb(double b, double k) {
        super(b, b, b, b, k);
        if (k>90){k = 180 - k; this.kat = k;}
        this.pole = Figury.EnumTwo.ObliczPole(Figury.EnumTwo.ROMB, b, k);
        this.obwod = Figury.EnumTwo.ObliczObwod(Figury.EnumTwo.ROMB, b, k);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("romb");
    }
}
