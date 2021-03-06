package com.septem.tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

public class TDView extends SurfaceView implements Runnable {

    volatile boolean playing;
    Thread gameThread = null;

    // Game objects
    private PlayerShip player;

    // Make some random space dust
    public List<SpaceDust> dustList = new ArrayList<>();
    public List<EnemyShip> enemyShips = new ArrayList<>();

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private int screenX;
    private int screenY;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private Context context;

    private boolean gameEnded;

    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;

        startGame();
    }

    private void startGame() {
        // Initialize game objects
        player = new PlayerShip(context, screenX, screenY);

        enemyShips.clear();
        int numEnemies = 3;
        for (int i = 0; i < numEnemies; i++) {
            enemyShips.add(new EnemyShip(context, screenX, screenY));
        }

        dustList.clear();
        int numSpecs = 40;
        for (int i = 0; i < numSpecs; i++) {
            // Where will the dust spawn?
            SpaceDust spec = new SpaceDust(screenX, screenY);
            dustList.add(spec);
        }

        // Reset time and distance
        distanceRemaining = 10000; // 10km
        timeTaken = 0;

        // Get start time
        timeStarted = System.currentTimeMillis();

        gameEnded = false;
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
        // Collision detection on new positions
        // Before move because we are testing last frames
        // position which has just been drawn

        // If you are using images in excess of 100 pixels
        // wide then increase the -100 value accordingly
        boolean hitDetected = false;

        for (EnemyShip ship : enemyShips) {
            if (Rect.intersects(
                    player.getHitbox(), ship.getHitBox()
            )) {
                hitDetected = true;
                ship.setX(-300);
            }

        }

        if (hitDetected) {
            player.reduceShieldStrength();
            if (player.getShieldStrength() < 0) {
                // Game over
                gameEnded = true;
            }
        }

        // Update player
        player.update();

        // Update enemy
        for (EnemyShip ship : enemyShips) {
            ship.update(player.getSpeed());
        }

        for (SpaceDust sd : dustList) {
            sd.update(player.getSpeed());
        }

        if (!gameEnded) {
            // Subtract distance to home planet based on current speed
            distanceRemaining -= player.getSpeed();

            // How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        // Completed the game!
        if (distanceRemaining < 0) {
            // check for new fastest time
            if (timeTaken < fastestTime) {
                fastestTime = timeTaken;
            }

            // avoid ugly negative numbers in HUD
            distanceRemaining = 0;

            // Now end the game
            gameEnded = true;
        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            // Lock area of memory that's drawn to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Draw teh space dust
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Draw the dust from arrayList
            for (SpaceDust sd : dustList) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            // Draw the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );

            // Draw the enemy
            for (EnemyShip ship : enemyShips) {
                canvas.drawBitmap(
                        ship.getBitmap(),
                        ship.getX(),
                        ship.getY(),
                        paint
                );
            }

            if (!gameEnded) {
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance:" +
                        distanceRemaining / 1000 +
                        " KM", screenX / 3, screenY - 20, paint
                );

                canvas.drawText("Shield:" +
                        player.getShieldStrength(), 10, screenY - 20, paint
                );

                canvas.drawText("Speed:" +
                        player.getSpeed() * 60 +
                        " MPS", (screenX / 3) * 2, screenY - 20, paint
                );
            } else {
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Fastest:" +
                    fastestTime + "s", screenX / 2, 160, paint);

                canvas.drawText("Time:" + timeTaken +
                    "s", screenX / 2, 200, paint);

                canvas.drawText("Distance remaining:" +
                        distanceRemaining / 1000 + " KM", screenX / 2, 240, paint);

                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX / 2, 350, paint);
            }


            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {

        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                if (gameEnded) {
                    startGame();
                }
                break;
        }
        return true;
    }
}
