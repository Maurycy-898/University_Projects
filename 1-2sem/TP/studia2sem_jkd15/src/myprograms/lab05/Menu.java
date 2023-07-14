package myprograms.lab05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    JTextField wprowadzPrawdopodobienstwo;
    JTextField wprowadzSzybkosc;
    JTextField wprowadzKolumny;
    JTextField wprowadzRzedy;
    JButton rozpocznijSymulacje;

    Menu(){
        this.setLayout(new GridLayout(5,2,2,40));
        wprowadzPrawdopodobienstwo = new JTextField("0.25");
        wprowadzPrawdopodobienstwo.addActionListener(this);
        wprowadzSzybkosc = new JTextField("1000");
        wprowadzSzybkosc.addActionListener(this);
        wprowadzKolumny = new JTextField("25");
        wprowadzKolumny.addActionListener(this);
        wprowadzRzedy = new JTextField("25");
        wprowadzRzedy.addActionListener(this);
        rozpocznijSymulacje = new JButton(" START ");
        rozpocznijSymulacje.addActionListener(this);

        JLabel prawdopodobienstwo = new JLabel("   Prawdopodobienstwo : ");
        JLabel szybkosc = new JLabel("   Szybkosc : ");
        JLabel kolumny = new JLabel( "   Kolumny : ");
        JLabel rzedy = new JLabel("   Rzedy : ");
        JLabel rozpocznij = new JLabel("   Rozpocznij Symulacje : ");

        this.add(prawdopodobienstwo);
        this.add(wprowadzPrawdopodobienstwo);
        this.add(szybkosc);
        this.add(wprowadzSzybkosc);
        this.add(kolumny);
        this.add(wprowadzKolumny);
        this.add(rzedy);
        this.add(wprowadzRzedy);
        this.add(rozpocznij);
        this.add(rozpocznijSymulacje);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(200,100,325,350);
        this.setTitle("Menu");
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == rozpocznijSymulacje){
            double p = -1;
            int szybkosc = 0;
            int kolumny = 0;
            int rzedy = 0;

            try { p = Double.parseDouble(wprowadzPrawdopodobienstwo.getText()); }
            catch (NumberFormatException ex){ System.out.println("Error"); System.exit(0); }

            try { szybkosc = Integer.parseInt(wprowadzSzybkosc.getText()); }
            catch (NumberFormatException ex){ System.out.println("Error"); System.exit(0); }

            try { kolumny = Integer.parseInt(wprowadzKolumny.getText()); }
            catch (NumberFormatException ex){ System.out.println("Error"); System.exit(0); }

            try { rzedy = Integer.parseInt(wprowadzRzedy.getText()); }
            catch (NumberFormatException ex){ System.out.println("Error"); System.exit(0); }

            if (p<0 || p>1 || szybkosc<=0 || kolumny<=0 || rzedy<=0){
                System.out.println("Error - złe dane");
                wprowadzPrawdopodobienstwo.setText("");
                wprowadzSzybkosc.setText("");
                wprowadzKolumny.setText("");
                wprowadzRzedy.setText("");
            }
            else {
                wprowadzPrawdopodobienstwo.setText("");
                wprowadzSzybkosc.setText("");
                wprowadzKolumny.setText("");
                wprowadzRzedy.setText("");

                new SimulationFrame(rzedy,kolumny,szybkosc,p);
            }
        }
    }
}
