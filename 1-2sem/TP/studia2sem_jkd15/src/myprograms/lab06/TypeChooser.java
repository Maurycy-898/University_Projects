package myprograms.lab06;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypeChooser extends JFrame implements ActionListener {
    myButton stringTree;
    myButton doubleTree;
    myButton integerTree;
    int status;
    JLabel chooseType;

    TypeChooser(){
        super("Type Chooser");
        this.setLayout(new GridLayout(4,1));
        stringTree = new myButton("  STRING  ");
        stringTree.addActionListener(this);

        doubleTree = new myButton(" DOUBLE ");
        doubleTree.addActionListener(this);

        integerTree = new myButton(" INTEGER ");
        integerTree.addActionListener(this);

        chooseType = new JLabel("    CHOOSE TREE TYPE:   ");
        chooseType.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        chooseType.setBorder(BorderFactory.createEtchedBorder());
        chooseType.setOpaque(true);
        chooseType.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseType.setBackground(Color.darkGray);
        chooseType.setForeground(Color.white);

        this.add(chooseType);
        this.add(stringTree);
        this.add(doubleTree);
        this.add(integerTree);

        this.setBounds(50,25,340,350);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setEnabled(true);
        this.setBackground(Color.darkGray);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        status = 0;
        int intStatus = 1;
        int doubleStatus = 2;
        int stringStatus = 3;


        if(e.getSource() == stringTree){
            new TreeGUIMenu(stringStatus);
            this.setVisible(false);
        }
        else if(e.getSource() == doubleTree){
            new TreeGUIMenu(doubleStatus);
            this.setVisible(false);
        }
        else if(e.getSource() == integerTree){
            new TreeGUIMenu(intStatus);
            this.setVisible(false);
        }
    }
}
