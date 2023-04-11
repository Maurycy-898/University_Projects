package myprograms.lab05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Field extends Thread {
    private Color kolor;
    JLabel wizualizacja;
    SimulationFrame obszarSymulacji;
    private boolean jestAktywne = true;
    private int rzad,kolumna;

    Field (Color kolor, SimulationFrame obszarSymulacji, int rzad, int kolumna){
        this.obszarSymulacji = obszarSymulacji;
        this.kolor = kolor;
        this.rzad = rzad;
        this.kolumna = kolumna;
        wizualizacja = new JLabel();
        wizualizacja.setOpaque(true);
        wizualizacja.setBackground(kolor);
        wizualizacja.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(jestAktywne == true){wizualizacja.setText("OFF");}
                if(jestAktywne == false){wizualizacja.setText("ON");}
            }
        });
    }

    @Override
    public void run(){
        while (true){
            if (jestAktywne){
                int sleeptime = (obszarSymulacji.generator.nextInt(10*obszarSymulacji.predkosc)+5*obszarSymulacji.predkosc)/10;
                try { sleep(sleeptime); }
                catch (Exception e) { System.out.println("ERROR"); System.exit(0); }

                double p = obszarSymulacji.generator.nextDouble();
                if(p <= obszarSymulacji.prawdopodobienstwo){
                    int R = obszarSymulacji.generator.nextInt(256);
                    int G = obszarSymulacji.generator.nextInt(256);
                    int B = obszarSymulacji.generator.nextInt(256);
                    this.kolor = new Color(R,G,B);
                    this.wizualizacja.setBackground(kolor);
                }
                else{
                    this.kolor = sredniKolorSasiadow();
                    this.wizualizacja.setBackground(kolor);
                }
                if(wizualizacja.getText().equals("OFF")){
                    wizualizacja.setText("");
                    kolor = new Color(0,0,0);
                    wizualizacja.setBackground(null);
                    jestAktywne = false;
                }
            }

            if(!jestAktywne) {
                if(wizualizacja.getText().equals("ON")){
                    wizualizacja.setText("");
                    jestAktywne = true;
                }
            }
        }
    }

    public Color sredniKolorSasiadow(){
        int nowyRzad1, nowaKolumna1, nowyRzad2, nowaKolumna2;
        int R1=0; int R2=0; int R3=0; int R4=0;
        int G1=0; int G2=0; int G3=0; int G4=0;
        int B1=0; int B2=0; int B3=0; int B4=0;
        int aktywniSasiedzi=0;

        if(rzad + 1 >= obszarSymulacji.rzedy){ nowyRzad1 = 0;} else{nowyRzad1 = rzad+1;}
        if(obszarSymulacji.polaSymulacji[nowyRzad1][kolumna].jestAktywne) {
            R1 = obszarSymulacji.polaSymulacji[nowyRzad1][kolumna].kolor.getRed();
            G1 = obszarSymulacji.polaSymulacji[nowyRzad1][kolumna].kolor.getGreen();
            B1 = obszarSymulacji.polaSymulacji[nowyRzad1][kolumna].kolor.getBlue();
            aktywniSasiedzi++;
        }

        if(rzad - 1 < 0){ nowyRzad2 = obszarSymulacji.rzedy - 1;} else{ nowyRzad2 = rzad-1;}
        if(obszarSymulacji.polaSymulacji[nowyRzad2][kolumna].jestAktywne) {
            R2 = obszarSymulacji.polaSymulacji[nowyRzad2][kolumna].kolor.getRed();
            G2 = obszarSymulacji.polaSymulacji[nowyRzad2][kolumna].kolor.getGreen();
            B2 = obszarSymulacji.polaSymulacji[nowyRzad2][kolumna].kolor.getBlue();
            aktywniSasiedzi++;
        }

        if(kolumna + 1 >= obszarSymulacji.kolumny){ nowaKolumna1 = 0;} else{ nowaKolumna1 = kolumna+1;}
        if(obszarSymulacji.polaSymulacji[rzad][nowaKolumna1].jestAktywne) {
            R3 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna1].kolor.getRed();
            G3 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna1].kolor.getGreen();
            B3 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna1].kolor.getBlue();
            aktywniSasiedzi++;
        }

        if(kolumna - 1 < 0){ nowaKolumna2 = obszarSymulacji.kolumny - 1;} else{ nowaKolumna2 = kolumna-1;}
        if(obszarSymulacji.polaSymulacji[rzad][nowaKolumna2].jestAktywne) {
            R4 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna2].kolor.getRed();
            G4 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna2].kolor.getGreen();
            B4 = obszarSymulacji.polaSymulacji[rzad][nowaKolumna2].kolor.getBlue();
            aktywniSasiedzi++;
        }

        if(aktywniSasiedzi>0) {
            int R = (R1 + R2 + R3 + R4) / aktywniSasiedzi;
            int G = (G1 + G2 + G3 + G4) / aktywniSasiedzi;
            int B = (B1 + B2 + B3 + B4) / aktywniSasiedzi;
            return new Color(R,G,B);
        }
        else{return this.kolor;}
    }
}
