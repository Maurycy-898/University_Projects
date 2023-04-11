package myprograms.lab06;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeGUIMenu extends JFrame implements ActionListener {
    int status;
    int intStatus = 1;
    int doubleStatus = 2;
    int stringStatus = 3;

    Tree myTree;
    TreeVisualisation visualisation;

    myButton delete;
    myButton insert;
    myButton search;
    myButton displayTree;
    JTextField dataInput;

    TreeGUIMenu(int status){
        super("GUI menu");
        this.status = status;
        this.setLayout(new GridLayout(5,1));
        setVariables();

        dataInput = new JTextField();
        dataInput.setFont(new Font(Font.SERIF, Font.PLAIN, 40));
        dataInput.setBackground(Color.DARK_GRAY);
        dataInput.setForeground(Color.white);

        delete = new myButton("  Delete  ");
        delete.addActionListener(this);

        insert = new myButton("  Insert  ");
        insert.addActionListener(this);

        search = new myButton("  Search  ");
        search.addActionListener(this);

        displayTree = new myButton("  Display  ");
        displayTree.addActionListener(this);

        this.add(dataInput);
        this.add(insert);
        this.add(delete);
        this.add(search);
        this.add(displayTree);

        this.setBounds(100, 25, 250,350);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    void setVariables(){
        if(this.status == intStatus){
            Tree<Integer> tmpTree = new Tree<Integer>();
            myTree = tmpTree;
        }
        else if(this.status == doubleStatus){
            Tree<Double> tmpTree = new Tree<Double>();
            myTree = tmpTree;
        }
        else if(this.status == stringStatus){
            Tree<String> tmpTree = new Tree<String>();
            myTree = tmpTree;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String myInput;
        if(e.getSource() == delete){
            myInput = dataInput.getText();
            if(status==stringStatus){
                this.myTree.deleteElement(myInput);
            }
            else if(status==doubleStatus){
                try {
                    double data = Double.parseDouble(myInput);
                    this.myTree.deleteElement(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            else if(status==intStatus){
                try {
                    int data = Integer.parseInt(myInput);
                    this.myTree.deleteElement(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            this.dataInput.setText("");
        }
        if(e.getSource() == insert){
            myInput = dataInput.getText();
            if(status==stringStatus){
                this.myTree.insert(myInput);
            }
            else if(status==doubleStatus){
                try {
                    double data = Double.parseDouble(myInput);
                    this.myTree.insert(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            else if(status==intStatus){
                try {
                    int data = Integer.parseInt(myInput);
                    this.myTree.insert(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            dataInput.setText("");
        }
        if(e.getSource() == search){
            myInput = dataInput.getText();
            boolean isInTree = false;
            if(status==stringStatus){
                isInTree = myTree.containsElement(myInput);
            }
            else if(status==doubleStatus){
                try {
                    double data = Double.parseDouble(myInput);
                    isInTree = this.myTree.containsElement(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            else if(status==intStatus){
                try {
                    int data = Integer.parseInt(myInput);
                    isInTree = this.myTree.containsElement(data);
                }catch (NumberFormatException ex){
                    System.out.println("Number Format Exception");
                }
            }
            if(isInTree){
                JOptionPane.showMessageDialog(null,"This tree does CONTAIN this element: "+myInput+"   ","info",JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null,"This tree does NOT CONTAIN this element: "+myInput+"   ","info",JOptionPane.INFORMATION_MESSAGE);
            }
            this.dataInput.setText("");
        }
        if(e.getSource() == displayTree){
            visualisation = new TreeVisualisation(myTree);
            visualisation.setVisible(true);
        }

    }
}
