package com.example.test1.game;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Object2d {
    private int x;      //wspolrzedne
    private int y;
    private int speedX; //predkosc
    private int speedY;
    private final int radius;   //promien kolki
    private final Paint paint;  //canvas rysuje przedmioty
    private final int maxX;     //maksymalny x i y
    private final int maxY;
    private boolean goLeft;     //flaga wskazuje czy idziemy w lewo prawo
    private boolean goRight;
    private boolean goTop;
    private boolean goBottom;

    public Object2d(int x, int y, int color, int speedX, int speedY, int radius, int maxX, int maxY) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.radius = radius;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }

    public void setGoTop(boolean goTop) {
        this.goTop = goTop;
    }

    public void setGoBottom(boolean goBottom) {
        this.goBottom = goBottom;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public void update() { //uktualnia stan czy idziee w bok w gore
        int yDir = 0;
        if (goTop && goBottom) {
            yDir = 0;
        } else if (goTop) {
            yDir = -1;
        } else if (goBottom) {
            yDir = 1;
        }


        int xDir = 0;
        if (goLeft && goRight) {
            xDir = 0;
        } else if (goLeft) {
            xDir = -1;
        } else if (goRight) {
            xDir = 1;
        }

        if (x - radius < 0) {
            x = radius;
        } else if (x + radius > maxX) {
            x = maxX - radius;
        } else {
            x += xDir * speedX;
        }

        y += yDir * speedY;
    }

    public boolean collide(Object2d other) { //sprawdza kolizje
        return calculateDistanceBetweenPoints(x, y, other.getX(), other.getY()) <= (other.getRadius() + radius);
    }

    public boolean shouldBeBeRemoved() {
        return y >= maxY;
    }

    private double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public int getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getMaxY() {
        return maxY;
    }
}
