package myprograms.tests;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Gra extends JFrame {
    int licznik=0;
    boolean czyLiczyc = true;
    Gra(){
        ImageIcon ObrazekJajka = new ImageIcon(getClass().getResource("jajko.jpg"));
        this.setLayout(new BorderLayout());
        JLabel jajko = new JLabel();
        jajko.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
        jajko.setIcon(ObrazekJajka);
        jajko.setPreferredSize(new Dimension(600,500));
        jajko.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if(czyLiczyc){ licznik++; }

                jajko.setText("  "+licznik);
                if (licznik == 27){
                    jajko.setVerticalTextPosition(JLabel.CENTER);
                    jajko.setHorizontalTextPosition(JLabel.CENTER);
                    jajko.setText("       KRECE TWOJA STARA NA KARUZELI");
                    jajko.setIcon(null);
                    czyLiczyc = false;
                }
            }
        });
        this.add(jajko,BorderLayout.CENTER);
        this.setTitle("Gra w Jajko");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100,10,1100,900);
        this.setVisible(true);
    }
}
