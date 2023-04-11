package myprograms.lab01;

public class zad1 {
    public static void main(String[] args){
        int wielkosc;
        try{
            wielkosc=Integer.parseInt(args[0]);
        } catch (NumberFormatException ex){
            System.out.println(args[0]+ "\nnie jest liczbą!!!");
            return;
        }

        if(wielkosc<0){
            System.out.println("\n" + wielkosc +"Nieprawidłowy numer wiersza");
            return;
        }


        WierszTrojkataPascala trojkat = new WierszTrojkataPascala(wielkosc);
        System.out.println("\nProgram drukujący wyrazy " +Integer.parseInt(args[0]) +"-ego wiersza trójkata Pascala");

        for(int i = 1; i<args.length; i++){
            int pom;
            try{
                pom = Integer.parseInt(args[i]);
            }
            catch (NumberFormatException ex) {
                System.out.println(args[i] + " nie jest liczbą!!!");
                continue;
            }
            if(pom<0 || pom>wielkosc){
                System.out.println(pom+ " -> Liczba spoza zakresu");
                continue;
            }
            System.out.println("Wyraz " +pom+ " -> " +trojkat.wiersz[pom]);
        }
    }
}
