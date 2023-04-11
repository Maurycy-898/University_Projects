package myprograms.lab06;

import javax.swing.*;
import java.awt.*;

public class TreeVisualisation <T extends Comparable<T>> extends JFrame {
    Tree<T> tree;
    JLabel treeVisualisation;

    TreeVisualisation(Tree<T> tree){
        super("Visualisation");
        this.tree = tree;
        String treeString = "   " + this.tree.toString();

        treeVisualisation = new JLabel();
        treeVisualisation.setOpaque(true);
        treeVisualisation.setBackground(Color.DARK_GRAY);
        treeVisualisation.setForeground(Color.white);
        treeVisualisation.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        treeVisualisation.setText(treeString);

        this.add(treeVisualisation);
        this.setBounds(500, 250, 750,150);
    }

}
