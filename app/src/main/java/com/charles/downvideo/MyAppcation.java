package com.charles.downvideo;

import android.app.Application;

import com.lzy.okgo.OkGo;

public class MyAppcation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
