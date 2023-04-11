package myprograms.lab03.zad2.PascalSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PascalTriangleFrame2 extends JFrame implements ActionListener {
    JLabel title;
    JPanel container;
    JButton button;

    PascalTriangleFrame2(int n){

        //-------------title----------------------------------------------------------------------------
        String titleOfThis = "PASCAL TRIANGLE,  SIZE  =  " + n;
        title = new JLabel(titleOfThis);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        title.setForeground(Color.white);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(95,20,500, 40);
        title.setPreferredSize(new Dimension(500,40));
        //title.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        //------------button----------------------------------------------------------------------------
        button = new JButton("TRY AGAIN");
        button.setBounds(650,20,150,40);
        button.setPreferredSize(new Dimension(150,35));
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setFont(new Font(Font.SERIF,Font.PLAIN,15));
        button.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        button.addActionListener(this);
        button.setFocusable(false);

        //------------look and container----------------------------------------------------------------
        container = new JPanel();
        container.setBackground(Color.DARK_GRAY);
        container.setLayout(null);
        container.add(title);
        container.add(button);

        //-------------------printing the Pascal Triangle-------------------------------------------------
        int y = 120;
        int constOfX;
        if(n % 2 == 0){
            constOfX = 10 + 100*(n/2);
        }else{
            constOfX = 50 + 50*(n-1);
        }
        int x = constOfX;
        PascalCalculator calculator = new PascalCalculator(0,0);
        int pascalValue;
        int m = 1;

        for(int i = 0; i < n; i++){

            for(int j = 0; j < m; j++){
                pascalValue = calculator.value(i,j);
                container.add(new PascalElement(x,y,pascalValue));
                x+=100;
            }
            constOfX-=50;
            x = constOfX;

            y += 45;
            m++;
        }

        //-----------------frame properties---------------------------------------------------------------
        this.setBounds(100,80,1200,700);
        this.setTitle("Pascal Triangle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(container);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button){
            this.setVisible(false);
            new MyQuestionFrame();
        }
    }
}
