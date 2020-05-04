package com.example.flittybrit;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ScoreSprite {
    private int x, y;
    private int score;
    private String scoreAsString;
    private Paint paint;

    public ScoreSprite() {
        this.score = 0;
        this.scoreAsString = "0";
        this.x = Resources.getSystem().getDisplayMetrics().widthPixels/2 - 50;
        this.y = Resources.getSystem().getDisplayMetrics().heightPixels/2 - 250;

        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(100);
        paint.setShadowLayer(6,1,1,Color.BLACK);
    }

    public void draw(Canvas canvas) {
        canvas.drawText(scoreAsString,x,y, paint);
    }

    public void update() {
        score++;
        scoreAsString = Integer.toString(score);
    }

    public int getScore() {
        return this.score;
    }

}
