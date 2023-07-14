package myprograms.lab04;

import java.awt.*;
import java.awt.geom.Ellipse2D;

//----------------- klasa koła------------------------------------------------------------------------------------------
public class MyCircle extends Ellipse2D.Float {
    //przechowuje swój kolor
    Color color = new Color(255, 0, 125);

    public MyCircle(int x, int y, int width, int height) {
        setFrame(x, y, width, height);
    }

    //isHit sprawdza czy najechaliśmy myszą na figurę
    public boolean isHit(int x, int y) { return this.getBounds2D().contains(x, y); }

    //addX, addY zmieniają położenie lewego górnego wierzchołka na podstawie którego rysowane jest koło
    //przydatne do przsuwania figury w metodzie "doMoveCircle" - klasa PanelToDraw
    public void addX(int x) { this.x += x; }
    public void addY(int y) { this.y += y; }

    //addWidth,addHeight zmieniają wymiary figury, przydatne w funkcji "doScale" - PanelToDraw
    public void addWidth(int w) { this.width += w; }
    public void addHeight(int h) { this.height += h; }
}

