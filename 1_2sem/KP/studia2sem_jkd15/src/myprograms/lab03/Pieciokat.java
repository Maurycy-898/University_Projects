package myprograms.lab03;
public class Pieciokat extends Figura {
    double bok;

    Pieciokat(double b){
        this.bok = b;
        this.pole = Figury.EnumOne.ObliczPole(Figury.EnumOne.PIECIOKAT, b);
        this.obwod = Figury.EnumOne.ObliczObwod(Figury.EnumOne.PIECIOKAT, b);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("pieciokat");
    }
}
