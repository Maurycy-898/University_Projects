package myprograms.lab04;

import java.awt.*;

//----------------- klasa trójkąta -------------------------------------------------------------------------------------
public class MyTriangle extends Polygon {
    //przechowuje swój kolor
    Color color = new Color(255, 0, 125);

    MyTriangle(Point a, Point b, Point c){ super(setXs(a.x, b.x, c.x), setYs(a.y, b.y, c.y), 3); }

    //statyczne funkcje setXs,setYs - pomagają zainicjować figure w konstruktorze
    //dzięki nim możemy inicjować trójkąt za pomocą 3 punktów a nie tablic int[]
    private static int[] setXs(int ax, int bx, int cx) {
        int[] xs = new int[3];
        xs[0] = ax; xs[1] = bx; xs[2] = cx;
        return xs;
    }
    private static int[] setYs(int ay, int by, int cy) {
        int[] ys = new int[3];
        ys[0] = ay; ys[1] = by; ys[2] = cy;
        return ys;
    }

    //isHit sprawdza czy najechaliśmy myszą na figurę
    public boolean isHit(float x, float y) {
        return this.getBounds2D().contains(x, y);
    }

    //metoda moveTriangle zmienia położenie wierzchołków trójkąta
    //przydatne do przsuwania figury w metodzie "doMoveTriangle" - klasa PanelToDraw
    public static void moveTriangle(MyTriangle triangle, int x, int y) {
        triangle.xpoints[0]+=x;  triangle.ypoints[0]+=y;
        triangle.xpoints[1]+=x;  triangle.ypoints[1]+=y;
        triangle.xpoints[2]+=x;  triangle.ypoints[2]+=y;
        triangle.invalidate();
    }

    //metoda resizeTriangle zmienia wymiary figury, przydatne w funkcji "doScale" - PanelToDraw
    public static void resizeTriangle(MyTriangle triangle, double scale) {
        //wyliczane są współrzędne środka jednokładności trójkąta (~średnia arytmetyczna współrzędnych wierzchołków)
        long centerX = (triangle.xpoints[0]+triangle.xpoints[1]+triangle.xpoints[2])/3;
        long centerY = (triangle.ypoints[0]+triangle.ypoints[1]+triangle.ypoints[2])/3;

        //wierzchołki przemnażane są przez podaną skalę względem środka jednokładności
        //dzięki czemu zmieniany jest rozmiar figury i zachowane jej cechy
        triangle.xpoints[0]=(int)((triangle.xpoints[0]-centerX) * scale + centerX);
        triangle.xpoints[1]=(int)((triangle.xpoints[1]-centerX) * scale + centerX);
        triangle.xpoints[2]=(int)((triangle.xpoints[2]-centerX) * scale + centerX);
        triangle.ypoints[0]=(int)((triangle.ypoints[0]-centerY) * scale + centerY);
        triangle.ypoints[1]=(int)((triangle.ypoints[1]-centerY) * scale + centerY);
        triangle.ypoints[2]=(int)((triangle.ypoints[2]-centerY) * scale + centerY);

        triangle.invalidate();
    }

}

