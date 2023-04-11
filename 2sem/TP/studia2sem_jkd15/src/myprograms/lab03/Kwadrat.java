package myprograms.lab03;

public class Kwadrat extends Czworokat{
    Kwadrat(double b) {
        super(b, b, b, b, 90);
        this.pole = Figury.EnumOne.ObliczPole(Figury.EnumOne.KWADRAT, b);
        this.obwod = Figury.EnumOne.ObliczObwod(Figury.EnumOne.KWADRAT, b);
    }
    @Override
    public void DrukujNazwe() {
        System.out.print("kwadrat");
    }
}
