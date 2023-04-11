package myprograms.lab03;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Figury {

    ///////////////////////////////////////////////////////////////////////////////////////
    public enum EnumOne{
        KWADRAT, KOLO, PIECIOKAT, SZESCIOKAT;

        public static double ObliczPole(EnumOne figura, double a){
            return switch (figura) {
                case KWADRAT -> a * a;
                case KOLO -> Math.PI * (a * a);
                case PIECIOKAT -> (0.25 * (a * a) * (sqrt(25 + 10 * sqrt(5))));
                case SZESCIOKAT -> (6 * (a * a) * 0.25 * sqrt(3));
            };
        }
        public static double ObliczObwod(EnumOne figura, double a){
            return switch (figura){
                case KWADRAT -> 4 * a;
                case KOLO -> Math.PI * 2 * a;
                case PIECIOKAT -> 5 * a;
                case SZESCIOKAT -> 6 * a;
            };
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    public enum EnumTwo{
        PROSTOKAT, ROMB;

        public static double ObliczPole(EnumTwo figura, double a, double b){
          return switch (figura){
              case PROSTOKAT -> a * b;
              case ROMB -> a * a * sin(b);
          };
        }
        public static double ObliczObwod(EnumTwo figura, double a, double b){
            return switch (figura){
                case PROSTOKAT -> 2 * a +2 * b;
                case ROMB -> 4 * a;
            };
        }
    }
}
