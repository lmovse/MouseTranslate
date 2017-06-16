package com.example.jooff.shuyi.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jooff on 2017/1/30.
 * Tomorrow is a nice day
 */

public class ActivityCollector {
    private static List<Activity> sActivities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : sActivities) {
                activity.finish();
            }
        }

}
