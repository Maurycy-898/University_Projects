package myprograms.lab03.zad2.PascalSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyQuestionFrame extends JFrame implements ActionListener {
    JButton perform;
    JTextField collectSize;
    JLabel instruction;
    JPanel panel;
    JCheckBox shouldAlign;
    int triangleSize;

    MyQuestionFrame(){

        //----------button-------------------------------------------------
        perform = new JButton("DRAW");
        perform.setPreferredSize(new Dimension(100,35));
        perform.setBackground(Color.darkGray);
        perform.setForeground(Color.white);
        perform.setFont(new Font(Font.SERIF,Font.PLAIN,15));
        perform.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        perform.addActionListener(this);
        perform.setFocusable(false);

        //---------text input----------------------------------------------
        collectSize = new JTextField();
        collectSize.setPreferredSize(new Dimension(40,30));
        collectSize.setFont(new Font(Font.SERIF,Font.BOLD,20));
        collectSize.setBackground(Color.darkGray);
        collectSize.setForeground(Color.white);
        collectSize.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.white));
        collectSize.setCaretColor(Color.WHITE);

        //----------instructions Label-----------------------------------------------------
        instruction = new JLabel("PLEASE ENTER THE TRIANGLE SIZE");
        instruction.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        instruction.setBackground(Color.darkGray);
        instruction.setForeground(Color.white);

        //--------------size-label---------------------------------------------------------
        JLabel sizeLabel = new JLabel("SIZE  =");
        sizeLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        sizeLabel.setForeground(Color.white);

        //----------------checkbox---------------------------------------------------------
        shouldAlign = new JCheckBox("  left alignment");
        shouldAlign.addActionListener(this);
        shouldAlign.setFont(new Font(Font.SERIF, Font.PLAIN, 15));
        shouldAlign.setBackground(Color.DARK_GRAY);
        shouldAlign.setForeground(Color.white);
        shouldAlign.setFocusable(false);

        //----------panel------------------------------------------------------------------
        panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.add(instruction);
        panel.add(sizeLabel);
        panel.add(collectSize);
        panel.add(perform);
        panel.add(shouldAlign);

        //-----------frame-----------------------------------------------------------------
        this.setTitle("Pascal Triangle");
        this.setBounds(500,250,400,175);
        this.setSize(400,215);
        this.setBackground(Color.yellow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(panel);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == perform){

            String data = collectSize.getText();
            boolean numberFormatIsOk = true;

            try{
                triangleSize = Integer.parseInt(data);
            }

            //---------------exception handling----------------------------------------------------------------
            catch (NumberFormatException ex){
                numberFormatIsOk = false;
            }
            if (triangleSize > 17 && numberFormatIsOk){
                this.setVisible(false);
                JOptionPane.showMessageDialog(null, "TOO BIG NUMBER", "error",JOptionPane.ERROR_MESSAGE );
                new MyQuestionFrame();
            }
            else if (triangleSize < 0 && numberFormatIsOk){
                this.setVisible(false);
                JOptionPane.showMessageDialog(null, "NUMBER MUST BE POSITIVE", "error", JOptionPane.ERROR_MESSAGE );
                new MyQuestionFrame();
            }
            else if(!numberFormatIsOk){
                this.setVisible(false);
                JOptionPane.showMessageDialog(null, "THAT IS NOT A NUMBER", "error",JOptionPane.ERROR_MESSAGE );
                new MyQuestionFrame();
            }

            //---------------showing proper pascal triangle if everything ok--------------------------------------
            else{
                this.setVisible(false);
                if(shouldAlign.isSelected()){
                    new PascalTriangleFrame(triangleSize);
                }
                else{
                    new PascalTriangleFrame2(triangleSize);
                }
            }
        }
    }
}
