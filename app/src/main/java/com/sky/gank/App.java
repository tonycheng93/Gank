package com.sky.gank;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by tonycheng on 2016/11/24.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
