package com.example.jooff.shuyi.data;

import android.provider.BaseColumns;

/**
 * Created by Jooff on 2017/1/13.
 */

public class AppDbSchema {
    public static final String DB_NAME = "mouse_translate";
    public static final int DB_VISION = 1;

    // Use private constructor to prevent init class
    private AppDbSchema() {
    }

    public static final class HistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "History";
        public static final String ORIGINAL = "original";
        public static final String RESULT = "result";
    }

}
