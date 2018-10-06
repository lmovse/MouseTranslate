package com.example.jooff.shuyi.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jooff.shuyi.data.AppDbSchema;

import static android.content.ContentValues.TAG;

/**
 * Created by Jooff on 2016/8/14.
 * Tomorrow is a nice day
 */

public class LocalDbOpenHelper extends SQLiteOpenHelper {

    /**
     * History建表语句
     */
    private static final String CREATE_HISTORY = "CREATE TABLE "
            + AppDbSchema.HistoryTable.TABLE_NAME + " ("
            + AppDbSchema.HistoryTable._ID + " integer" + " primary key autoincrement,"
            + AppDbSchema.HistoryTable.ORIGINAL + " text,"
            + AppDbSchema.HistoryTable.RESULT + " text, "
            + AppDbSchema.HistoryTable.COLLECTED + " tinyint unsigned default 0" + ")";

    private static final String CREATE_COLLECT = "CREATE TABLE "
            + AppDbSchema.CollectTable.TABLE_NAME + " ("
            + AppDbSchema.CollectTable._ID + " integer" + " primary key autoincrement,"
            + AppDbSchema.CollectTable.ORIGINAL + " text,"
            + AppDbSchema.CollectTable.RESULT + " text" + ")";

    LocalDbOpenHelper(Context context) {
        super(context, AppDbSchema.DB_NAME, null, AppDbSchema.DB_VISION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_COLLECT);
        Log.d(TAG, "onCreate: 新建两张成功！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            sqLiteDatabase.execSQL("ALTER TABLE "
                    + AppDbSchema.HistoryTable.TABLE_NAME + " ADD "
                    + AppDbSchema.HistoryTable.COLLECTED + " tinyint unsigned default 0 ");
            sqLiteDatabase.execSQL("CREATE TABLE "
                    + AppDbSchema.CollectTable.TABLE_NAME + "("
                    + AppDbSchema.CollectTable._ID + " integer" + " primary key autoincrement" + ", "
                    + AppDbSchema.CollectTable.ORIGINAL + " text" + ", "
                    + AppDbSchema.CollectTable.RESULT + " text" + ")");
            Log.d(TAG, "onUpgrade: 更新表成功！");
        }
    }

}
