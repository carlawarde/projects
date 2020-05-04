package com.example.flittybrit;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {

    private Bitmap image;
    private int x, y;
    private double yVelocity = 3;
    private double yGravity = 1;
    private double yAcceleration = 6;

    public CharacterSprite (Bitmap bmp) {
        image = bmp;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        y += yVelocity * yGravity;
        if(yAcceleration > 9) {
            yAcceleration -= 0.05;
        }
        if(yGravity < 6) {
            yGravity += 0.1;
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getYAcceleration() {
        return yAcceleration;
    }

    public void setYAcceleration(double yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public double getYGravity() {
        return yGravity;
    }

    public void setYGravity(double yGravity) {
        this.yGravity = yGravity;
    }

    public double getYVelocity() {
        return yVelocity;
    }
}
