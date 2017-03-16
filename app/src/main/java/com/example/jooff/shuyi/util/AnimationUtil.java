package com.example.jooff.shuyi.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.jooff.shuyi.R;

/**
 * Created by Jooff on 2017/1/31.
 */

public class AnimationUtil {

    public static Animation getAlpha(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_alpha);
    }

    public static Animation getScale(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_scale);
    }

    public static void startActivity(
            final Activity thisActivity, final Intent intent, final View triggerView, int colorOrImageRes, final long durationMills) {
        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth();
        final int cy = location[1] + triggerView.getHeight() + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, thisActivity.getResources().getDisplayMetrics());
        final ImageView view = new ImageView(thisActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(colorOrImageRes);
        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);
        final int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator
                anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(durationMills);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                thisActivity.startActivity(intent);
                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        anim.start();
    }

}

