package com.example.jooff.shuyi.common;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Jooff on 2017/1/31.
 */

public class MySnackBar {
    public static int color = Color.parseColor("#35464e");

    public static Snackbar getSnack(View view, int resId) {
        Snackbar snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        return snackbar;
    }

}
