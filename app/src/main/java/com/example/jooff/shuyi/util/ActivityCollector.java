package com.example.jooff.shuyi.util;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Jooff on 2017/1/30.
 */

public class ActivityCollector {
    public static List<Activity> sActivities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sActivities.add(activity);
        Log.d(TAG, "addActivity: " + sActivities.size());
    }

    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    public static void finishAll() {
        Log.d(TAG, "finishAll: " + sActivities.size());
        for (Activity activity : sActivities) {
                activity.finish();
            }
        }

}
