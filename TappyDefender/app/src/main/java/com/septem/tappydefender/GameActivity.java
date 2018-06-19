package com.septem.tappydefender;

import android.content.Context;
import android.content.Intent;

public class GameActivity {
    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, GameActivity.class);
        return intent;
    }
}
