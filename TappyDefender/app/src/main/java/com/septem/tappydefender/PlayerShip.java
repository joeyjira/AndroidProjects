package com.septem.tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerShip {
    private Bitmap bitmap;

    private int x, y;

    private int mSpeed = 0;

    private boolean mBoosting;

    private final int GRAVITY = -12;

    // Stop ship leaving the screen
    private int maxY;
    private int minY;

    //  Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    // A hit box for collision detection
    private Rect hitBox;

    private int shieldStrength;

    public PlayerShip(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        mSpeed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        mBoosting = false;
        maxY = screenY - bitmap.getHeight();
        minY = 0;
        shieldStrength = 2;

        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        if (mBoosting) {
            // Speed up
            mSpeed += 2;
        } else {
            mSpeed -= 5;
        }

        // Constrain top speed
        if (mSpeed > MAX_SPEED) {
            mSpeed = MAX_SPEED;
        }

        // Never stop completely
        if (mSpeed < MIN_SPEED) {
            mSpeed = MIN_SPEED;
        }

        // Move the ship vertically
        y -= mSpeed + GRAVITY;

        // But don't let ship stray off screen
        if (y < minY) {
            y = minY;
        }

        if (y > maxY) {
            y = maxY;
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    // Getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setBoosting() {
        mBoosting = true;
    }

    public void stopBoosting() {
        mBoosting = false;
    }

    public Rect getHitbox() {
        return hitBox;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }
}
