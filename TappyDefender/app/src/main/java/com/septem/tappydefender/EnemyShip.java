package com.septem.tappydefender;

import android.graphics.Bitmap;

public class EnemyShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    // Detect enemies leaving the screen
    private int maxX;
    private int minX;

    // Spawn enemies within screen bounds
    private int maxY;
    private int minY;
}
