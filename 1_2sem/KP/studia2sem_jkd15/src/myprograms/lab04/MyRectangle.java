package myprograms.lab04;

import java.awt.*;
import java.awt.geom.Rectangle2D;

//----------------- klasa prostokąta------------------------------------------------------------------------------------
class MyRectangle extends Rectangle2D.Float {
    //przechowuje swój kolor
    Color color = new Color(255, 0, 125);

    public MyRectangle(int x, int y, int width, int height) { setRect(x, y, width, height); }

    //isHit sprawdza czy najechaliśmy myszą na figurę
    public boolean isHit(float x, float y) { return this.getBounds2D().contains(x, y); }

    //addX, addY zmieniają położenie lewego górnego wierzchołka na podstawie którego rysowany jest prostokąt
    //przydatne do przsuwania figury w metodzie "doMoveRect" - klasa PanelToDraw
    public void addX(float x) { this.x += x; }
    public void addY(float y) { this.y += y; }

    //addWidth,addHeight zmieniają wymiary figury, przydatne w funkcji "doScale" - PanelToDraw
    public void addWidth(float w) { this.width += w; }
    public void addHeight(float h) { this.height += h; }
}
