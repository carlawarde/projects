package com.example.flittybrit;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView {

    private Context context;
    private CharacterSprite characterSprite;
    private PipeSprite pipe1, pipe2, pipe3, pipe4, pipe5;
    private ScoreSprite scoreSprite;
    private PipeSprite rightMostPipe;
    private Bitmap background;

    private int gapHeight;
    private int velocity;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private String TAG = "debug";

    public GameView(Context context) {
        super(context);
        this.context = context;
        setFocusable(true);
    }

    protected void makeLevel() {
        //Initialise game variables
        velocity = 9;
        gapHeight = 500;

        //Set up character image array and randomise which one is chosen. Then set up the character sprite
        int[] characters = new int[4];
        characters[0] = R.drawable.brit;
        characters[1] = R.drawable.borris;
        characters[2] = R.drawable.queen;
        characters[3] = R.drawable.theresa;
        Random random = new Random();
        int currentCharacter = random.nextInt(characters.length);
        characterSprite = new CharacterSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),characters[currentCharacter]),150,150));

        //Set up pipe bitmaps
        Bitmap bmp;
        Bitmap bmp2;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_down), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_up), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        pipe1 = new PipeSprite(bmp, bmp2, screenWidth/2 + 1000, 100, gapHeight, velocity);
        pipe2 = new PipeSprite(bmp, bmp2, screenWidth/2 + 1500, 150, gapHeight, velocity);
        pipe3 = new PipeSprite(bmp, bmp2, screenWidth/2+ 2300, 200, gapHeight, velocity);
        pipe4 = new PipeSprite(bmp, bmp2, screenWidth/2+ 2900, 180, gapHeight, velocity);
        pipe5 = new PipeSprite(bmp, bmp2, screenWidth/2 + 3400, 50, gapHeight, velocity);

        rightMostPipe = pipe5;

        //Set up background bitmap
        background = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.background),screenWidth,screenHeight);

        //Set up score sprite
        scoreSprite = new ScoreSprite();
        Log.d(TAG,"Completed level set-up");
    }

    public void update() {
        gameLogic();
        characterSprite.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();
        pipe4.update();
        pipe5.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(canvas!=null) {
            canvas.drawBitmap(background,0,0,null);
            characterSprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
            pipe4.draw(canvas);
            pipe5.draw(canvas);
            scoreSprite.draw(canvas);
        }
    }

    public void gameLogic() {

        List<PipeSprite> pipes = new ArrayList<>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);
        pipes.add(pipe4);
        pipes.add(pipe5);

        PipeSprite pipe;

        for (int i = 0; i < pipes.size(); i++) {

            pipe = pipes.get(i);

            //Detect if the character is touching one of the pipes
            if (characterSprite.getY() < pipe.getY() + (screenHeight / 2) - (gapHeight / 2) && characterSprite.getX() + 130 > pipe.getX() && characterSprite.getX() < pipe.getX() + 250) {
                gameOver();
            }
            else if (characterSprite.getY() + 130 > (screenHeight / 2) + (gapHeight / 2) + pipe.getY() && characterSprite.getX() + 200 > pipe.getX() && characterSprite.getX() < pipe.getX() + 250) {
                gameOver();
            }

            //Detect if the pipe has gone off the left of the screen and regenerate further ahead
            if (pipe.getX() + 500 < 0) {
                redrawPipe(pipe);
                rightMostPipe = pipe;
                pipe.setLeftOfCharacter(false);
            }

            //Increase score if character has gone past a pipe
            if(characterSprite.getX() > pipe.getX() && !(pipe.isLeftOfCharacter())) {
                scoreSprite.update();
                pipe.setLeftOfCharacter(true);

                //increases x velocity by 1 if score is a multiple of 25
                if((scoreSprite.getScore() % 5 == 0 && scoreSprite.getScore() > 0) && velocity < 20) {
                    velocity++;
                    increaseXVelocity(pipes);
                }
            }
        }
        //Detect if the character has gone off the bottom or top of the screen
        if (characterSprite.getY() + 240 < 0)
            gameOver();
        if (characterSprite.getY() > screenHeight)
            gameOver();
    }

    private void increaseXVelocity(List<PipeSprite> pipes) {
        PipeSprite pipe;

        for (int i = 0; i < pipes.size(); i++) {

            pipe = pipes.get(i);
            pipe.setXVelocity(velocity);
        }
    }

    public void redrawPipe(PipeSprite pipe) {
        Random r = new Random();
        int value1 = r.nextInt(900 - 450 + 1)+450;
        int value2 = r.nextInt(400);

        pipe.setX(rightMostPipe.getX() + value1);
        pipe.setY(value2 - 250);
    }

    public void gameOver() {
        //Send score to game activity
        ((GameActivity) context).callGameActivity(scoreSprite.getScore());

        //Recycle Bitmaps
        background.recycle();
        characterSprite.getImage().recycle();
        pipe1.getImage().recycle();     pipe1.getImage2().recycle();
        pipe2.getImage().recycle();     pipe2.getImage2().recycle();
        pipe3.getImage().recycle();     pipe3.getImage2().recycle();
        pipe4.getImage().recycle();     pipe4.getImage2().recycle();
        pipe5.getImage().recycle();     pipe5.getImage2().recycle();
        Log.d(TAG,"Recycled bitmaps");
    }

    /// method from https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        characterSprite.setY((int)  (characterSprite.getY() - ((characterSprite.getYVelocity() * 4) * characterSprite.getYAcceleration())));

        if(characterSprite.getYAcceleration() < 12) {
            characterSprite.setYAcceleration(characterSprite.getYAcceleration()+ 1.5);
        }

        if(characterSprite.getYGravity() > 3) {
            characterSprite.setYGravity(characterSprite.getYGravity() / 3);
        }
        else if(characterSprite.getYGravity() > 1.5) {
            characterSprite.setYGravity(characterSprite.getYGravity() - 0.5);
        }
        return super.onTouchEvent(event);
    }
}

