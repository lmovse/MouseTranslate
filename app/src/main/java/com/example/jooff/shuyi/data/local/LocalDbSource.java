package com.example.jooff.shuyi.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDbSchema;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;

import java.util.ArrayList;

/**
 * Created by Jooff on 2016/8/14.
 * Tomorrow is a nice day
 */

public class LocalDbSource implements AppDbSource.HistoryDbSource, AppDbSource.CollectDbSource, AppDbSource.TranslateDbSource {
    private static LocalDbSource sLocalDbSource = null;
    private SQLiteDatabase db;

    /**
     * 构造方法私有化，创建单例类
     */
    private LocalDbSource(Context context) {
        LocalDbOpenHelper mHelper = new LocalDbOpenHelper(context);
        db = mHelper.getWritableDatabase();
    }

    /**
     * 获取单例的实例,使用双重校验锁，保证线程安全与速率
     */
    public static LocalDbSource getInstance(Context context) {
        if (sLocalDbSource== null) {
            synchronized (LocalDbSource.class) {
                if (sLocalDbSource == null) {
                    sLocalDbSource = new LocalDbSource(context);
                }
            }
        }
        return sLocalDbSource;
    }

//--------------------------------------- Trans Source ---------------------------------------------

    @Override
    public void getTrans(int transFrom, String original, AppDbSource.TranslateCallback callback) {
        Translate trans = new Translate();
        if (transFrom == TransSource.FROM_COLLECT) {
            Collect collect = getCollect(original);
            trans.setQuery(collect.getOriginal());
            trans.setTranslation(collect.getResult());
        }
        if (transFrom == TransSource.FROM_HISTORY) {
            History history = getHistory(original);
            trans.setQuery(history.getOriginal());
            trans.setTranslation(history.getResult());
        }
        if (trans.getTranslation() == null) {
            trans.setTranslation("");
        }
        callback.onResponse(trans);
    }

//---------------------------------------- History Source ------------------------------------------

    @Override
    public void saveHistory(History history) {
        if (getHistory(history.getOriginal()) == null) {
            ContentValues values = new ContentValues();
            values.put(AppDbSchema.HistoryTable.ORIGINAL, history.getOriginal());
            values.put(AppDbSchema.HistoryTable.RESULT, history.getResult());
            db.insert(AppDbSchema.HistoryTable.TABLE_NAME, null, values);
        } else {
            ContentValues values = new ContentValues();
            String updateClause = AppDbSchema.HistoryTable.ORIGINAL + " == ? ";
            String[] updateArgs = {history.getOriginal()};
            values.put(AppDbSchema.HistoryTable.RESULT, history.getResult());
            db.update(AppDbSchema.HistoryTable.TABLE_NAME, values, updateClause, updateArgs);
        }
    }

    @Override
    public void collectHistory(History history) {
        ContentValues values = new ContentValues();
        values.put(AppDbSchema.CollectTable.ORIGINAL, history.getOriginal());
        values.put(AppDbSchema.CollectTable.RESULT, history.getResult());
        db.insert(AppDbSchema.CollectTable.TABLE_NAME, null, values);
        Object[] args = {1, history.getOriginal()};
        db.execSQL("UPDATE " + AppDbSchema.HistoryTable.TABLE_NAME + " SET collected = ? where original = ?", args);
    }

    @Override
    public void cancelCollect(String original) {
        deleteCollect(original);
        Object[] args = {0, original};
        db.execSQL("UPDATE " + AppDbSchema.HistoryTable.TABLE_NAME + " SET collected = ? where original = ?", args);
    }

    @Override
    public History getHistory(String original) {
        History item = null;
        String queryLogic = AppDbSchema.HistoryTable.ORIGINAL + " == ? ";
        String[] queryArgs = {original};
        Cursor cursor = db.query(AppDbSchema.HistoryTable.TABLE_NAME, null, queryLogic, queryArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String textOriginal = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.ORIGINAL));
            String textResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.RESULT));
            int collected = cursor.getInt(cursor.getColumnIndex(AppDbSchema.HistoryTable.COLLECTED));
            item = new History(textOriginal, textResult, collected);
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    @Override
    public ArrayList<History> getHistorys() {
        ArrayList<History> items = new ArrayList<>();
        Cursor cursor = db.query(AppDbSchema.HistoryTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String hisOrigin = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.ORIGINAL));
                String hisResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.RESULT));
                int collected = cursor.getInt(cursor.getColumnIndex(AppDbSchema.HistoryTable.COLLECTED));
                History item = new History(hisOrigin, hisResult, collected);
                items.add(0, item);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return items;
    }

    @Override
    public void deleteHistory(String original) {
        String deleteClause = AppDbSchema.HistoryTable.ORIGINAL + " == ? ";
        String[] deleteArgs = {original};
        db.delete(AppDbSchema.HistoryTable.TABLE_NAME, deleteClause, deleteArgs);
    }

//--------------------------------------- Collect Source -------------------------------------------

    @Override
    public Collect getCollect(String original) {
        Collect item = null;
        String queryLogic = AppDbSchema.CollectTable.ORIGINAL + " == ? ";
        String[] queryArgs = {original};
        Cursor cursor = db.query(AppDbSchema.CollectTable.TABLE_NAME, null, queryLogic, queryArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String textOriginal = cursor.getString(cursor.getColumnIndex(AppDbSchema.CollectTable.ORIGINAL));
            String textResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.CollectTable.RESULT));
            item = new Collect(textOriginal, textResult);
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    @Override
    public ArrayList<Collect> getCollects() {
        ArrayList<Collect> items = new ArrayList<>();
        Cursor cursor = db.query(AppDbSchema.CollectTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String hisOrigin = cursor.getString(cursor.getColumnIndex(AppDbSchema.CollectTable.ORIGINAL));
                String hisResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.CollectTable.RESULT));
                Collect item = new Collect(hisOrigin, hisResult);
                items.add(0, item);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return items;
    }

    @Override
    public void deleteCollect(String original) {
        String deleteClause = AppDbSchema.CollectTable.ORIGINAL + " == ? ";
        String[] deleteArgs = {original};
        db.delete(AppDbSchema.CollectTable.TABLE_NAME, deleteClause, deleteArgs);
    }

}
