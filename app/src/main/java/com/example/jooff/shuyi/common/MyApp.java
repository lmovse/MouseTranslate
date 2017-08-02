package com.example.jooff.shuyi.common;

import android.app.Application;
import android.graphics.Color;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jooff on 2016/8/20.
 * 自定义 Application ，实现一些资源的程序内共享
 */

public class MyApp extends Application{
    public static RequestQueue sRequestQueue;
    public static boolean sIsNightMode = false;
    public static int sColorPrimary = Color.parseColor("#35464e");

    @Override
    public void onCreate() {
        super.onCreate();
        sRequestQueue = Volley.newRequestQueue(this);
    }

}
