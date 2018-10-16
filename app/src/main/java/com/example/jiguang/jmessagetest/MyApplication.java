package com.example.jiguang.jmessagetest;

import android.app.Application;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by jiguang on 2018/5/14.
 */

public class MyApplication extends Application {
    private static final String TAG="MyApplication";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG,"onCreate()");

        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(),true);//启用消息漫游

    }
}
