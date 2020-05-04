package com.example.flittybrit;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PipeSprite {

    private Bitmap image;
    private Bitmap image2;
    private int x, y;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int gapHeight;
    private int xVelocity;
    private boolean leftOfCharacter = false;

    public PipeSprite (Bitmap bmp, Bitmap bmp2, int x, int y, int gapHeight, int xVelocity) {
        this.image = bmp;
        this.image2 = bmp2;
        this.y = y;
        this.x = x;
        this.gapHeight = gapHeight;
        this.xVelocity = xVelocity;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, -(gapHeight / 2) + y, null);
        canvas.drawBitmap(image2,x, ((screenHeight / 2) + (gapHeight / 2)) + y, null);
    }

    public void update() {
        x -= xVelocity;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getImage2() {
        return image2;
    }

    public boolean isLeftOfCharacter() {
        return leftOfCharacter;
    }

    public void setLeftOfCharacter(boolean leftOfCharacter) {
        this.leftOfCharacter = leftOfCharacter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }
}
