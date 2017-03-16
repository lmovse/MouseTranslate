package com.example.jooff.shuyi.common;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jooff on 2016/8/20.
 * 自定义 Application ，实现一些资源的程序内共享
 */

public class MyApp extends Application{
    public static RequestQueue sRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        sRequestQueue = Volley.newRequestQueue(this);
    }

}
