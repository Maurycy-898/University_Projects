package myprograms.lab04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

    //--------główne okno zawiera menu i panel do rysowania figur-------------------------------------------------------
public class MainWindow extends JFrame implements ActionListener {
    //-----------inicjacja komponentów wchodzących w skład głównego okna------------------------------------------------
    PanelToDraw DrawingSurface;
    JPanel OptionPanel;
    JLabel menu;
    ButtonGroup Options;
    JButton save; JButton open;
    JButton newProject; JButton exit;
    JRadioButton circle; JRadioButton triangle;
    JRadioButton rectangle; JRadioButton selectAndEdit;
    JFileChooser fileChooser = new JFileChooser();

    //----ustawianie komponentów, ich właściwości,nazw itp., oraz dodawanie ich do głównego okna------------------------
    MainWindow(){
        super("   PAINT DEMO");

    //--------------radioButtons - ustawianie,dodanie action listenera--------------------------------------------------
        ImageIcon myIcon = new ImageIcon("/image.png");
        circle = new JRadioButton("* circle         ");
        triangle = new JRadioButton("* triangle      ");
        rectangle = new JRadioButton("* rectangle    ");
        selectAndEdit = new JRadioButton("* select & edit");
        circle.addActionListener(this);
        triangle.addActionListener(this);
        rectangle.addActionListener(this);
        selectAndEdit.addActionListener(this);
        circle.setBackground(Color.DARK_GRAY);
        triangle.setBackground(Color.DARK_GRAY);
        rectangle.setBackground(Color.DARK_GRAY);
        selectAndEdit.setBackground(Color.DARK_GRAY);
        circle.setForeground(Color.gray);
        triangle.setForeground(Color.gray);
        rectangle.setForeground(Color.gray);
        selectAndEdit.setForeground(Color.gray);
        triangle.setIcon(myIcon);
        circle.setIcon(myIcon);
        rectangle.setIcon(myIcon);
        selectAndEdit.setIcon(myIcon);
        circle.setFont(new Font(Font.SERIF,Font.ITALIC, 20));
        triangle.setFont(new Font(Font.SERIF,Font.ITALIC, 20));
        rectangle.setFont(new Font(Font.SERIF,Font.ITALIC, 20));
        selectAndEdit.setFont(new Font(Font.SERIF,Font.ITALIC, 20));
        circle.setAlignmentX(Component.CENTER_ALIGNMENT);
        triangle.setAlignmentX(Component.CENTER_ALIGNMENT);
        rectangle.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectAndEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        Options = new ButtonGroup();
        Options.add(rectangle);
        Options.add(circle);
        Options.add(triangle);
        Options.add(selectAndEdit);

    //-------------ustawianie Labeli z opisami--------------------------------------------------------------------------
        menu = new JLabel("MAIN MENU");
        menu.setFont(new Font(Font.SERIF,Font.BOLD, 30));
        menu.setForeground(Color.white);
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel selectInfo = new JLabel("SELECT  SHAPE");//("<HTML><U> Select shape  :</U></HTML>");
        selectInfo.setFont(new Font(Font.SERIF,Font.PLAIN, 20));
        selectInfo.setForeground(Color.white);
        selectInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

    //----Buttony - ustawianie i dodanie action Listenera (zapisywanie, wczytywanie, nowy plik, exit)-------------------
        save = new JButton("    SAVE FILE     ");
        save.setForeground(Color.lightGray);
        save.setBackground(Color.DARK_GRAY);
        save.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        save.setBorder(BorderFactory.createEtchedBorder());
        save.addActionListener(this);
        save.setFocusable(false);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);

        open = new JButton("    OPEN FILE    ");
        open.setForeground(Color.lightGray);
        open.setBackground(Color.DARK_GRAY);
        open.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        open.setBorder(BorderFactory.createEtchedBorder());
        open.setFocusable(false);
        open.addActionListener(this);
        open.setAlignmentX(Component.CENTER_ALIGNMENT);

        newProject = new JButton("     NEW FILE     ");
        newProject.setForeground(Color.lightGray);
        newProject.setBackground(Color.DARK_GRAY);
        newProject.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        newProject.setBorder(BorderFactory.createEtchedBorder());
        newProject.setFocusable(false);
        newProject.addActionListener(this);
        newProject.setAlignmentX(Component.CENTER_ALIGNMENT);

        exit = new JButton("      EXIT      ");
        exit.setForeground(Color.white);
        exit.setBackground(Color.DARK_GRAY);
        exit.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        exit.setBorder(BorderFactory.createEtchedBorder());
        exit.setFocusable(false);
        exit.addActionListener(this);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

    //------------ustawianie Paneli: z Menu i opcjami(OptionPanel) oraz panelu do rysowania(DrawingSurface)-------------
        DrawingSurface = new PanelToDraw();

        OptionPanel = new JPanel();
        OptionPanel.setOpaque(true);
        OptionPanel.setBackground(Color.DARK_GRAY);
        OptionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1) );
        OptionPanel.setPreferredSize(new Dimension(250,0));
        OptionPanel.setLayout(new BoxLayout(OptionPanel,BoxLayout.PAGE_AXIS));

        OptionPanel.add(Box.createRigidArea(new Dimension(0,50)));
        OptionPanel.add(menu);
        OptionPanel.add(Box.createRigidArea(new Dimension(0,80)));
        OptionPanel.add(selectInfo);
        OptionPanel.add(Box.createRigidArea(new Dimension(0,10)));
        OptionPanel.add(rectangle);
        OptionPanel.add(circle);
        OptionPanel.add(triangle);
        OptionPanel.add(selectAndEdit);
        OptionPanel.add(Box.createRigidArea(new Dimension(0,60)));
        OptionPanel.add(newProject);
        OptionPanel.add(save);
        OptionPanel.add(open);
        OptionPanel.add(Box.createRigidArea(new Dimension(0,70)));
        OptionPanel.add(exit);

    //---------------dodawanie zainicjowanych komponentów do głównego okna,ustawienie Layoutu---------------------------
        this.setLayout(new BorderLayout());
        this.setBounds(300,100,1250,700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.setLayout(new BorderLayout());
        this.add(DrawingSurface,BorderLayout.CENTER);
        this.add(OptionPanel,BorderLayout.WEST);
        this.setVisible(true);
    }

    //-------------------programowanie akcji po wciśnięciu konkretnych przycisków---------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
    //----------------inicjacja statusów figur--------------------------------------------------------------------------
        int rectangleStatus = 1; int circleStatus = 2;
        int triangleStatus = 3; int editStatus = 4;

    //---------w zależności od wybranej figury przekazuje jej status do DrawingSurface-panelu do rysowania--------------
        if(e.getSource()==triangle){
            DrawingSurface.status = triangleStatus;
        }
        else if(e.getSource()==rectangle){
             DrawingSurface.status = rectangleStatus;
        }
        else if(e.getSource()==circle){
            DrawingSurface.status = circleStatus;
        }
        else if(e.getSource()==selectAndEdit){
            DrawingSurface.status = editStatus;
        }

    //-------obsługa przycisku newProject, usuwa wszystkie narysowane figury przechowywane w listach--------------------
        else if(e.getSource() == newProject){
            DrawingSurface.rectangles.clear();
            DrawingSurface.circles.clear();
            DrawingSurface.triangles.clear();
            DrawingSurface.repaint();
        }

    //------obsługa przycisku open: usuwa wszystkie figury, wczytuje i rysuje zapisane w wybranym pliku figury----------
        else if(e.getSource() == open){
            //------------------- usuwanie aktualnie narysowanych figur-------------------------------------------------
            DrawingSurface.rectangles.clear();
            DrawingSurface.circles.clear();
            DrawingSurface.triangles.clear();

            //-----------------------otwieranie fileChoosera i wybieranie odpowiedniego pliku---------------------------
            fileChooser.setCurrentDirectory(new File("C:\\Users\\Maurycy\\Desktop\\codes\\myPaintDrawings"));
            fileChooser.showOpenDialog(null);

            //-----tworzenie zmiennej savedFigures do której przypisujemy scannerem zapisane w pliku parametry figur----
            String savedFigures="";
            try{ Scanner s = new Scanner(new File(fileChooser.getSelectedFile().getAbsolutePath()));
                savedFigures = s.nextLine();
                s.close();
            }catch (Exception ex){ System.out.println(ex.getStackTrace()); }

            //-------rysowanie zapisanych figur funkcją ShapesDataListToFigures-----------------------------------------
            ShapesDataListToFigures(savedFigures); DrawingSurface.repaint();
        }

    //------obsługa przycisku save: zapisuje wszystkie narysowane figury (dokładniej ich parametry) do pliku txt--------
        else if(e.getSource() == save){
            //-----------------------otwieranie fileChoosera i tworzenie nowego pliku(powinien być .txt)----------------
            fileChooser.setCurrentDirectory(new File("C:\\Users\\Maurycy\\Desktop\\codes\\myPaintDrawings"));
            fileChooser.showSaveDialog(null);

            //zapisywanie nowego pliku
            //(Stringa przechowującego parametry figur utworzonego funkcją makeShapesDataList)
            //za pomocą BufferWritera
            try{ BufferedWriter writer =
                    new BufferedWriter(new FileWriter(fileChooser.getSelectedFile().getAbsolutePath()));
                writer.write(makeShapesDataList());
                writer.close();
            }catch(IOException er){ er.printStackTrace();System.out.println("Nah its fine");}
        }

    //------------- obsługa przycisku exit-po naciśnięciu kończy działanie programu ------------------------------------
        else if(e.getSource() == exit){ System.exit(0); } }

    //------- funkcja makeShapesDataList: zapisuje parametry aktualnie narysowanych figur jako Stringa -----------------
    //------- parametry ArrayList poszczególnych figur są oddzielone "?", a same parametry oddziela "," ----------------
    public String makeShapesDataList(){
        String list = "?,";
        char seperate = '?';
        for (MyRectangle r : DrawingSurface.rectangles) {
            list +=(int)r.x+","+(int)r.y+","+(int)r.width+","+(int)r.height+","+r.color.getRGB()+",";
        }
        list = list + seperate + ",";
        for (MyCircle c : DrawingSurface.circles) {
            list +=(int)c.x+","+(int)c.y+","+(int)c.width+","+(int)c.height+","+c.color.getRGB()+",";
        }
        list = list + seperate + ",";
        for (MyTriangle t : DrawingSurface.triangles) {
            list += t.xpoints[0] +","+t.ypoints[0]+","+t.xpoints[1]+","+t.ypoints[1]+","+
                    t.xpoints[2]+","+t.ypoints[2]+","+t.color.getRGB()+",";
        }
        list = list + seperate;
        return list;
    }

    //----- funkcja ShapesDataListToFigures odczytuje Stringa zapisanego przy pomocy funcji make ShapesDataList --------
    //------------ a także dodaje/rysuje odczytane figury do DrawingSurface --------------------------------------------
    public void ShapesDataListToFigures(String shapeList){
        String[] list = shapeList.split(",");
        boolean cStatus = false; boolean tStatus = false; boolean rStatus = true;
        for(int i=1; i<list.length;i++){
            if(rStatus){
                if(!list[i].equals("?")) {
                    MyRectangle rectangle = new MyRectangle(parseInt(list[i]),
                            parseInt(list[i + 1]),
                            parseInt(list[i + 2]),
                            parseInt(list[i + 3]));
                    rectangle.color = new Color(parseInt(list[i + 4]));
                    DrawingSurface.rectangles.add(rectangle);
                    i += 4; continue;
                }else {rStatus=false; cStatus=true; continue;}
            }
            if(cStatus){
                if(!list[i].equals("?")) {
                    MyCircle circle = new MyCircle(parseInt(list[i]),
                            parseInt(list[i + 1]),
                            parseInt(list[i + 2]),
                            parseInt(list[i + 3]));
                    circle.color = new Color(parseInt(list[i + 4]));
                    DrawingSurface.circles.add(circle);
                    i += 4; continue;
                }else {cStatus=false; tStatus=true; continue;}
            }
            if(tStatus){
                if(!list[i].equals("?")) {
                    Point a = new Point(Integer.parseInt(list[i]), Integer.parseInt(list[i + 1]));
                    Point b = new Point(Integer.parseInt(list[i + 2]), Integer.parseInt(list[i + 3]));
                    Point c = new Point(Integer.parseInt(list[i + 4]), Integer.parseInt(list[i + 5]));
                    MyTriangle triangle = new MyTriangle(a, b, c);
                    triangle.color = new Color(parseInt(list[i + 6]));
                    DrawingSurface.triangles.add(triangle);
                    i += 6; continue;
                }else {continue;}
            }
        }
    }
}

