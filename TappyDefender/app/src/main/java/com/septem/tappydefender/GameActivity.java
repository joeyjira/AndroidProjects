package com.septem.tappydefender;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity{
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }
}
