package com.example.jooff.shuyi.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jooff.shuyi.data.AppDbSchema;

/**
 * Created by Jooff on 2016/8/14.
 */

public class LocalDbOpenHelper extends SQLiteOpenHelper {

    /**
     * History建表语句
     */

    private static final String CREATE_HISTORY = "CREATE TABLE "
            + AppDbSchema.HistoryTable.TABLE_NAME + " ("
            + AppDbSchema.HistoryTable._ID + " integer" + " primary key autoincrement" + ", "
            + AppDbSchema.HistoryTable.ORIGINAL + " text" + ", "
            + AppDbSchema.HistoryTable.RESULT + " text" + ")";

    LocalDbOpenHelper(Context context) {
        super(context, AppDbSchema.DB_NAME, null, AppDbSchema.DB_VISION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
