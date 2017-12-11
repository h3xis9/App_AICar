package com.example.h3xis9.app_aicar;

import android.app.Application;
import android.os.SystemClock;


/**
 * Created by h3xis9 on 2017/11/20.
 */

public class SplashDelayActivity extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
