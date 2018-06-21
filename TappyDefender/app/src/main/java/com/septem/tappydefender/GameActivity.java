package com.septem.tappydefender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity{

    // Our object to handle the View
    private TDView gameView;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new TDView(this);

        // Mkae our gameView the view for the Activity
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
