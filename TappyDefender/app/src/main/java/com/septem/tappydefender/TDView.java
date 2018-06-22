package com.septem.tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TDView extends SurfaceView implements Runnable {

    volatile boolean playing;
    Thread gameThread = null;

    // Game objects
    private PlayerShip player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    public TDView(Context context) {
        super(context);
        ourHolder = getHolder();

        // Initialize player ship
        player = new PlayerShip(context);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();
    }

    private void draw() {
        // Lock area of memory that's drawn to
        canvas = ourHolder.lockCanvas();

        // Rub out the last frame
        canvas.drawColor(Color.argb(255, 0, 0, 0));

        // Draw the player
        canvas.drawBitmap(
                player.getBitmap(),
                player.getX(),
                player.getY(),
                paint
        );
    }

    private void control() {

    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
