package myprograms.lab04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

    //---------------------------- panel do rysowania figur ------------------------------------------------------------
public class PanelToDraw extends JPanel {

    //---------- inicjacja statusów figur (dzięki nim wiemy jaka figura/akcja jest wybrana)-----------------------------
    int status = 1; int rectangleStatus = 1; int circleStatus = 2; int triangleStatus = 3; int editStatus = 4;

    //---------- inicjacja zmiennych mówiących o ilości kliknięć myszy(potrzebne do rysowania trójkąta) ----------------
    boolean firstClick = true; boolean secondClick= false; boolean thirdClick = false;

    //--------- inicjacja przydatnych później punktów (wierzchołków trójkąta, pozycji myszy) ---------------------------
    Point firstVertex, secondVertex ,thirdVertex,startDrag, endDrag, mousePoint;

    //--------- inicjacja zmiennych mówiących o wybranej figurze (potrzebne do przesuwania figur) ----------------------
    boolean selectRect,selectCircle,selectTriangle = false;
    //--------- tymczasowe zmienne figur (przydatne do przesuwania figur)-----------------------------------------------
    MyTriangle tempT; MyRectangle tempR; MyCircle tempC;

    //--------- inicjacja ArrayList przechowujących figury -------------------------------------------------------------
    ArrayList <MyRectangle> rectangles = new ArrayList<>();
    ArrayList <MyCircle> circles = new ArrayList<>();
    ArrayList <MyTriangle> triangles = new ArrayList<>();


    PanelToDraw() {
        //Ustawienie panelu, dodanie podwójnego bufforowania
        this.isDoubleBuffered();
        this.setOpaque(false);
    //------------------- obsługa myszy --------------------------------------------------------------------------------
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
    //---------------- gdy wciskamy mysz pobierane są współrzędne wskaźnika myszy---------------------------------------
                startDrag = new Point(e.getX(), e.getY());
                endDrag = startDrag;
                repaint();
    //jeśli wybrana jest opcja select & edit (status = editStatus) sprawdzamy metodą isHit która figura zostałą wybrana-
    //następnie tworzymy do niej wskaźnik zmienną pomocniczą i oznaczamy odpowiedni typ figury (select) jako wybrany----
                if(status == editStatus){
                    for(MyRectangle r: rectangles){
                        if(r.isHit(startDrag.x,startDrag.y)){ selectRect = true; tempR = r; break; }
                    }for(MyCircle c: circles){
                        if(c.isHit(startDrag.x,startDrag.y)){ selectCircle = true; tempC = c; break;}
                    }for(MyTriangle t: triangles){
                        if(t.isHit(startDrag.x,startDrag.y)){ selectTriangle = true; tempT = t; break; }
                    }
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
    //jeśli wybrane jest rysowanie trójkąta(triangle status), pobieramy współrzędne jego wierzchołków i rysujemy trójkąt
    //zmienne first/second/thirdClick mówią nam o numerze kliknięcia, na końcu resetujemy pomocnicze zmienne------------
                if(status == triangleStatus){
                    if(firstClick){
                        firstVertex = new Point(e.getX(),e.getY());
                        firstClick = false;
                        secondClick = true;
                    } else if(secondClick){
                        secondVertex = new Point(e.getX(),e.getY());
                        secondClick = false;
                        thirdClick = true;
                    } else if(thirdClick){
                        thirdVertex = new Point(e.getX(),e.getY());
                        thirdClick = false;
                        firstClick = true;
    //przez "rysowanie" figury mam na myśli dodanie jej do tablicy figur której zawartość jest rysowana funkcją repaint
                        MyTriangle triangle = new MyTriangle(firstVertex,secondVertex,thirdVertex);
                        triangles.add(triangle);
                        firstVertex = null; secondVertex = null; thirdVertex = null;
                        repaint();
                    }
                }
    //gdy klikamy z włączoną opcją edytowania(editStatus), przy kliknięciu na figurę możemy zmienić jej kolor-----------
    //kolor zmieniamy przy pomocy JColorChooser, zmienna oneSelected zapobiega wybraniu dwóch figur naraz---------------
                if(status == editStatus){
                    boolean oneSelected = false;
                    if(!oneSelected) {
                        for (MyRectangle r : rectangles) {
                            if (r.isHit(startDrag.x, startDrag.y)) {
                                r.color = JColorChooser.showDialog(null, "PICK A COLOR", Color.CYAN);
                                oneSelected = true; repaint(); break;
                            }
                        }
                        for (MyCircle c : circles) {
                            if (c.isHit(startDrag.x, startDrag.y)) {
                                c.color = JColorChooser.showDialog(null, "PICK A COLOR", Color.CYAN);
                                oneSelected = true; repaint(); break;
                            }
                        }
                        for (MyTriangle t : triangles) {
                            if (t.isHit(startDrag.x, startDrag.y)) {
                                t.color = JColorChooser.showDialog(null, "PICK A COLOR", Color.CYAN);
                                oneSelected = true; repaint(); break;
                            }
                        }
                    }
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (status == rectangleStatus) {
    //gdy przeciągniemy,i puścimy mysz mając włączone rysowanie prostokąta(rectangleStatus) rysujemy prostokąt----------
                    MyRectangle rectangle = new MyRectangle(
                            Math.min(startDrag.x, endDrag.x),
                            Math.min(startDrag.y, endDrag.y),
                            Math.abs(startDrag.x - endDrag.x),
                            Math.abs(startDrag.y - endDrag.y));
                    rectangles.add(rectangle);
                    startDrag = null;
                    endDrag = null;
                    repaint();
                }
    //gdy przeciągniemy,i puścimy mysz mając włączone rysowanie koła(circleStatus) rysujemy koło------------------------
                else if (status == circleStatus) {
                    int r = Math.abs(startDrag.x-endDrag.x);
                    MyCircle circle = new MyCircle(startDrag.x-r,startDrag.y-r,2*r,2*r);
                    circles.add(circle);
                    startDrag = null;
                    endDrag = null;
                    repaint();
                }
    //po puszczeniu myszy resetowanie zmiennych pomocniczych do przesuwania figur---------------------------------------
                selectTriangle=false; selectCircle=false; selectRect=false;
                tempR=null; tempC=null; tempT=null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                endDrag = new Point(e.getX(), e.getY());
    //jeśli przesuwamy wciśniętą myszą i kliknęliśmy na jakąś figurę oraz wybrane jest edytowanie(editStatus)-----------
    //przesuwamy zaznaczoną figurę odpowiednią funkcją doMove-----------------------------------------------------------
                if(status == editStatus){
                    if(selectRect){doMoveRect(tempR,e);}
                    else if(selectCircle){ doMoveCircle(tempC,e); }
                    else if(selectTriangle){ doMoveTriangle(tempT,e); }
                }
                repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
               //mousePoint przechowuje aktualne współrzędne myszy
                if(status == triangleStatus){ mousePoint = new Point(e.getX(),e.getY()); repaint();}
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {
    //jeśli wybrana jest opcja edytowania(editStatus) i poruszamy rolką myszy, zmieniamy rozmiar figur funkcją doScale--
    //metodą isHit sprawdzamy na którą figurę najechała mysz i zmieniamy jej rozmiar------------------------------------
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) { if(status==editStatus){ doScale(e);}}
            void doScale (MouseWheelEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    for (MyRectangle r: rectangles) {
                        if (r.isHit(x, y)) {
                            float amount =  e.getWheelRotation()*7;
                            r.addWidth(amount); r.addHeight(amount/1.5f);
                            repaint();
                        }
                    }for (MyCircle c: circles) {
                        if (c.isHit(x, y)) {
                            int amount =  e.getWheelRotation()*7;
                            c.addWidth(amount); c.addHeight(amount);
                            repaint();
                        }
                    }for (MyTriangle t: triangles) {
                        if(t.isHit(x, y)){
                            if(e.getWheelRotation()>0) { t.resizeTriangle(t,1.1);}
                            else{ t.resizeTriangle(t,0.9);}
                            repaint();
                        }
                    }
                }
            }
        });
    }

    // funkcja paint, jest wywoływana za przy każdym "repaint", rysuje wszystkie figury przechowywane w tablicach ------
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new Color(255, 0, 125));
    //---------- rysowanie figur i  wypełnianie przypisanej figurze kolorem --------------------------------------------
        for (MyRectangle r : rectangles) {
            g2d.setPaint(r.color);
            g2d.draw(r);
            g2d.fill(r);
        }for (MyCircle c : circles) {
            g2d.setPaint(c.color);
            g2d.draw(c);
            g2d.fill(c);
        }for (MyTriangle t : triangles) {
            g2d.setPaint(t.color);
            g2d.draw(t);
            g2d.fill(t);
        }
    //------ rysowanie podglądu prostokąta -----------------------------------------------------------------------------
        if (startDrag != null && endDrag != null && status == rectangleStatus) {
            g2d.setPaint(Color.darkGray);
            Shape rectangle = new Rectangle(
                        Math.min(startDrag.x, endDrag.x),
                        Math.min(startDrag.y, endDrag.y),
                        Math.abs(startDrag.x - endDrag.x),
                        Math.abs(startDrag.y - endDrag.y));
            g2d.draw(rectangle);
        }
    //------ rysowanie podglądu koła -----------------------------------------------------------------------------------
        else if (startDrag != null && endDrag != null && status == circleStatus) {
            g2d.setPaint(Color.darkGray);
            int r = Math.abs(startDrag.x - endDrag.x);
            Shape circle = new Ellipse2D.Float(startDrag.x - r, startDrag.y - r, 2 * r, 2 * r);
            g2d.draw(circle);
        }
    //------ rysowanie podglądu trójkąta w zależności od numeru kliknięcia ---------------------------------------------
        else if (secondClick && status == triangleStatus) {
            g2d.setPaint(Color.darkGray);
            g2d.drawLine(firstVertex.x, firstVertex.y, mousePoint.x, mousePoint.y);
        } else if (thirdClick && status == triangleStatus) {
            g2d.setPaint(Color.darkGray);
            g2d.drawLine(firstVertex.x, firstVertex.y, secondVertex.x, secondVertex.y);
            g2d.drawLine(firstVertex.x, firstVertex.y, mousePoint.x, mousePoint.y);
            g2d.drawLine(secondVertex.x, secondVertex.y, mousePoint.x, mousePoint.y);
        }

    }

    //-------------------- funkcje do przesuwania figur ----------------------------------------------------------------
    public void doMoveRect(MyRectangle r, MouseEvent e) {
        int dx = e.getX() - startDrag.x;
        int dy = e.getY() - startDrag.y;
        if(r!=null && r.isHit(startDrag.x,startDrag.y)) {
            r.addX(dx);
            r.addY(dy);
        }
        startDrag.x += dx;
        startDrag.y += dy;
    }
    public void doMoveCircle(MyCircle c ,MouseEvent e) {
        int dx = e.getX() - startDrag.x;
        int dy = e.getY() - startDrag.y;
        if(c!=null && c.isHit(startDrag.x,startDrag.y)) {
            c.addX(dx);
            c.addY(dy);
        }
        startDrag.x += dx;
        startDrag.y += dy;
    }
    public void doMoveTriangle(MyTriangle t, MouseEvent e) {
        int dx = e.getX() - startDrag.x;
        int dy = e.getY() - startDrag.y;
        Point newPosition = new Point(dx,dy);
        if(t!=null && t.isHit(startDrag.x,startDrag.y)) {
            t.moveTriangle(t, dx, dy);
        }
        startDrag.x += dx;
        startDrag.y += dy;
    }
}
