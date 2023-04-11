package myprograms.lab05;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class SimulationFrame extends JFrame {
    Field[][] polaSymulacji;
    int R,G,B,predkosc, rzedy, kolumny;
    double prawdopodobienstwo;
    Random generator = new Random();

    SimulationFrame(int rzedy, int kolumny, int predkosc, double prawdopodobienstwo){
        this.predkosc=predkosc;
        this.prawdopodobienstwo = prawdopodobienstwo;
        this.rzedy = rzedy;
        this.kolumny = kolumny;
        this.polaSymulacji = new Field[rzedy][kolumny];

        for(int i = 0; i < rzedy; i++){
            for(int j = 0; j < kolumny; j++){
                R = generator.nextInt(256);
                G = generator.nextInt(256);
                B = generator.nextInt(256);
                Field field = new Field(new Color(R,G,B), this, i, j);
                polaSymulacji[i][j] = field;
            }
        }

        this.setLayout(new GridLayout(rzedy,kolumny,2,2));
        for(int i = 0; i < kolumny; i++) {
            for (int j = 0; j < rzedy; j++) {
                this.add(polaSymulacji[i][j].wizualizacja);
                polaSymulacji[i][j].start();
            }
        }

        this.setBounds(700,100,800,700);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Symulacja");
        this.setVisible(true);
    }
}
