package com.example.h3xis9.app_aicar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by h3xis9 on 2017/11/20.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }
}
