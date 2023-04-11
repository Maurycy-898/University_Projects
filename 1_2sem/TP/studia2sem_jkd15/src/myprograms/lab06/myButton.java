package myprograms.lab06;

import javax.swing.*;
import java.awt.*;

public class myButton extends JButton {
    myButton(String title){
        super(title);
        this.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setFocusable(false);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setBackground(Color.darkGray);
        this.setForeground(Color.white);
    }
}
