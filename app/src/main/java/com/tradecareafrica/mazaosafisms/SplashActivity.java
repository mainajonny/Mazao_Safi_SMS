package com.tradecareafrica.mazaosafisms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    final public static int MY_PERMISSION_SEND_SMS = 0;
    final public static int MY_PERMISSION_INTERNET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash_screen);

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }
}