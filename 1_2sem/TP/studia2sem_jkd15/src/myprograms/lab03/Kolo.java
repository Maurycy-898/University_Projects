package myprograms.lab03;

public class Kolo extends Figura {
    double promien;

    Kolo(int r){
        this.promien = r;
        this.pole = Figury.EnumOne.ObliczPole(Figury.EnumOne.KOLO,r);
        this.obwod = Figury.EnumOne.ObliczObwod(Figury.EnumOne.KOLO,r);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("kolo");
    }
}
