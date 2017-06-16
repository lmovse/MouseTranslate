package com.example.jooff.shuyi.data;

import android.provider.BaseColumns;

/**
 * Created by Jooff on 2017/1/13.
 * 数据库模型类，包括建表语句表值，方便对各种表进行管理，虽然现在只有一张，好像以后也不会有两张==。
 */

public class AppDbSchema {
    public static final String DB_NAME = "mouse_translate";
    public static final int DB_VISION = 1;

    // Use private constructor to prevent init class
    private AppDbSchema() {}

    public static final class HistoryTable implements BaseColumns {
        public static final String TABLE_NAME = "History";
        public static final String ORIGINAL = "original";
        public static final String RESULT = "result";
    }

}
