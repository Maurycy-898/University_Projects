package myprograms.lab02;

public class figury {
    public static void main(String[] args) {

        String[] literyFigur = new String[args.length];
        int[] wymiary = new int[args.length];


        int ileFigur = 0;
        int ileDanych = 0;
        for (int i=0; i < args.length; i++) {
            try {
              wymiary[ileDanych] = Integer.parseInt(args[i]);
              ileDanych++;

            } catch (NumberFormatException ex){
                literyFigur[ileFigur] = args[i];
                ileFigur++;
            }
        }


        for(int k=0; k<ileDanych; k++){
            if(wymiary[k] <= 0){
                System.out.println("Podałeś złe parametry, wymiary figur i kąty muszą być większe od zera !!!");
                return;
            }
        }


        Figura[] figury = new Figura[ileFigur];
        int wymiaryWsk = 0;
        int figuryWsk = 0;
        for (int i = 0; i < ileFigur; i++){
            if (literyFigur[i].equals("o")){
                if(ileDanych - wymiaryWsk < 1) {
                    System.out.println("Podałeś za mało danych!");
                    return;
                }
                figury[figuryWsk] = new Kolo(wymiary[wymiaryWsk]);
                wymiaryWsk += 1;
                figuryWsk++;
            }
            else if (literyFigur[i].equals("c")){
                if(ileDanych - wymiaryWsk < 5) {
                    System.out.println("Podałeś za mało danych!");
                    return;
                }
                boolean czyForemny = true;
                for(int j=1; j < 3; j++){
                    if (wymiary[wymiaryWsk] != wymiary[wymiaryWsk + j]) {
                        czyForemny = false;
                        break;
                    }
                }

                if(czyForemny && (wymiary[wymiaryWsk+4]==90)){
                    figury[figuryWsk] = new Kwadrat(wymiary[wymiaryWsk]);
                    wymiaryWsk+=5;
                    figuryWsk++;
                }
               else if(czyForemny && ((wymiary[wymiaryWsk+4]<180) && (wymiary[wymiaryWsk+4]>0))){
                    figury[figuryWsk] = new Romb (wymiary[wymiaryWsk], wymiary[wymiaryWsk+4]);
                    wymiaryWsk+=5;
                    figuryWsk++;
               }
               else if((wymiary[wymiaryWsk]==wymiary[wymiaryWsk+1]) && (wymiary[wymiaryWsk+2]==wymiary[wymiaryWsk+3]) && (wymiary[wymiaryWsk+4]==90)){
                    figury[figuryWsk] = new Prostokat (wymiary[wymiaryWsk], wymiary[wymiaryWsk+1]);
                    wymiaryWsk+=5;
                    figuryWsk++;
               }
               else{
                    System.out.println("Podałeś nieobsługiwane wymiary(4-kąt) !!!");
                    break;
               }
            }
            else if(literyFigur[i].equals("p")){
                if(ileDanych - wymiaryWsk < 5) {
                    System.out.println("Podałeś za mało danych!");
                    return;
                }

                boolean czyForemny = true;
                for(int j=1; j <= 4; j++){
                    if (wymiary[wymiaryWsk] != wymiary[wymiaryWsk + j]) {
                        czyForemny = false;
                        break;
                    }
                }

                if(czyForemny) {
                    figury[figuryWsk] = new Pieciokat(wymiary[wymiaryWsk]);
                    wymiaryWsk += 5;
                    figuryWsk++;
                }
                else{
                    System.out.println("Podałeś nieobsługiwane wymiary(5-kąt) !!!");
                    break;
                }
            }
            else if(literyFigur[i].equals("s")){
                if(ileDanych - wymiaryWsk < 6) {
                    System.out.println("Podałeś za mało danych!");
                    return;
                }
                boolean czyForemny = true;
                for(int j=1; j <= 5; j++){
                    if (wymiary[wymiaryWsk] != wymiary[wymiaryWsk + j]) {
                        czyForemny = false;
                        break;
                    }
                }
                if(czyForemny) {
                    figury[figuryWsk] = new Szesciokat(wymiary[wymiaryWsk]);
                    wymiaryWsk += 6;
                    figuryWsk++;
                }
                else{
                    System.out.println("Podałeś nieobsługiwane wymiary (6-kąt) !!!");
                    break;
                }
            }
            else{
                System.out.println("Program nie obsługuje takiej figury !!!  "+ literyFigur[i]);
                break;
            }
        }


        System.out.println("Ilosc Figur: "+ ileFigur + "\n");

        for(int k = 0; k < ileFigur; k++){
           System.out.print("Figura nr"+ (k+1) + " -> ");
           figury[k].DrukujNazwe();
           System.out.println("\npole: "+ figury[k].pole +"\tobwod: "+ figury[k].obwod +"\n");
        }
    }
}
