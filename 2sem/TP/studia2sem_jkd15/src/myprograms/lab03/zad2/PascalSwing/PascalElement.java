package myprograms.lab03.zad2.PascalSwing;

import javax.swing.*;
import java.awt.*;

public class PascalElement extends JLabel {

    PascalElement(int x, int y, int value){
        String valueOf = Integer.toString(value);
        this.setText(valueOf);
        this.setBounds(x,y,75,25);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setOpaque(true);
        this.setBackground(Color.DARK_GRAY);
        this.setForeground(Color.white);
        this.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        this.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
    }
}
