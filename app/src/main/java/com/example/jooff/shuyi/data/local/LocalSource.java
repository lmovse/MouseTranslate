package com.example.jooff.shuyi.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;

import java.util.ArrayList;

import static com.example.jooff.shuyi.data.AppDbSchema.CollectTable;
import static com.example.jooff.shuyi.data.AppDbSchema.HistoryTable;

/**
 * Created by Jooff on 2016/8/14.
 * Tomorrow is a nice day
 */

public class LocalSource implements AppDataSource.HistoryDbSource, AppDataSource.CollectDbSource, AppDataSource.TranslateRemoteSource {

    private volatile static LocalSource sLocalDbSource = null;

    private SQLiteDatabase db;

    public static final String DELETE_ALL_HISTORY_SQL = "DELETE FROM " + HistoryTable.TABLE_NAME;

    private int transFrom;

    public void setTransFrom(int transFrom) {
        this.transFrom = transFrom;
    }

    private LocalSource(Context context) {
        LocalDbOpenHelper mHelper = new LocalDbOpenHelper(context);
        db = mHelper.getWritableDatabase();
    }

    public static LocalSource getInstance(Context context) {
        if (sLocalDbSource == null) {
            synchronized (LocalSource.class) {
                if (sLocalDbSource == null) {
                    sLocalDbSource = new LocalSource(context);
                }
            }
        }
        return sLocalDbSource;
    }

//--------------------------------------- Trans Source ---------------------------------------------

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        Translate trans = new Translate();
        if (transFrom == TransSource.FROM_COLLECT) {
            Collect collect = getCollect(transUrl);
            trans.setQuery(collect.getOriginal());
            trans.setTranslation(collect.getResult());
        }
        if (transFrom == TransSource.FROM_HISTORY) {
            History history = getHistory(transUrl);
            trans.setQuery(history.getOriginal());
            trans.setTranslation(history.getResult());
        }
        if (trans.getTranslation() == null) {
            trans.setTranslation("");
        }
        callback.onResponse(trans);
    }

    @Override
    public String getUrl(String original) {
        return original;
    }

//---------------------------------------- History Source ------------------------------------------

    @Override
    public void saveHistory(History history) {
        if (getHistory(history.getOriginal()) == null) {
            ContentValues values = new ContentValues();
            values.put(HistoryTable.ORIGINAL, history.getOriginal());
            values.put(HistoryTable.RESULT, history.getResult());
            db.insert(HistoryTable.TABLE_NAME, null, values);
        } else {
            ContentValues values = new ContentValues();
            String updateClause = HistoryTable.ORIGINAL + " == ? ";
            String[] updateArgs = {history.getOriginal()};
            values.put(HistoryTable.RESULT, history.getResult());
            db.update(HistoryTable.TABLE_NAME, values, updateClause, updateArgs);
        }
    }

    @Override
    public void collectHistory(History history) {
        ContentValues values = new ContentValues();
        values.put(CollectTable.ORIGINAL, history.getOriginal());
        values.put(CollectTable.RESULT, history.getResult());
        db.insert(CollectTable.TABLE_NAME, null, values);
        Object[] args = {1, history.getOriginal()};
        db.execSQL("UPDATE " + HistoryTable.TABLE_NAME + " SET collected = ? where original = ?", args);
    }

    @Override
    public void cancelCollect(String original) {
        deleteCollect(original);
        Object[] args = {0, original};
        db.execSQL("UPDATE " + HistoryTable.TABLE_NAME + " SET collected = ? where original = ?", args);
    }

    @Override
    public History getHistory(String original) {
        History item = null;
        String queryLogic = HistoryTable.ORIGINAL + " == ? ";
        String[] queryArgs = {original};
        Cursor cursor = db.query(HistoryTable.TABLE_NAME, null, queryLogic, queryArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String textOriginal = cursor.getString(cursor.getColumnIndex(HistoryTable.ORIGINAL));
            String textResult = cursor.getString(cursor.getColumnIndex(HistoryTable.RESULT));
            int collected = cursor.getInt(cursor.getColumnIndex(HistoryTable.COLLECTED));
            item = new History(textOriginal, textResult, collected);
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    @Override
    public ArrayList<History> getHistories() {
        ArrayList<History> items = new ArrayList<>();
        Cursor cursor = db.query(HistoryTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String hisOrigin = cursor.getString(cursor.getColumnIndex(HistoryTable.ORIGINAL));
                String hisResult = cursor.getString(cursor.getColumnIndex(HistoryTable.RESULT));
                int collected = cursor.getInt(cursor.getColumnIndex(HistoryTable.COLLECTED));
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
        String deleteClause = HistoryTable.ORIGINAL + " == ? ";
        String[] deleteArgs = {original};
        db.delete(HistoryTable.TABLE_NAME, deleteClause, deleteArgs);
    }

    @Override
    public void deleteAllHistory() {
        db.execSQL(DELETE_ALL_HISTORY_SQL);
    }

//--------------------------------------- Collect Source -------------------------------------------

    @Override
    public Collect getCollect(String original) {
        Collect item = null;
        String queryLogic = CollectTable.ORIGINAL + " == ? ";
        String[] queryArgs = {original};
        Cursor cursor = db.query(CollectTable.TABLE_NAME, null, queryLogic, queryArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String textOriginal = cursor.getString(cursor.getColumnIndex(CollectTable.ORIGINAL));
            String textResult = cursor.getString(cursor.getColumnIndex(CollectTable.RESULT));
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
        Cursor cursor = db.query(CollectTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String hisOrigin = cursor.getString(cursor.getColumnIndex(CollectTable.ORIGINAL));
                String hisResult = cursor.getString(cursor.getColumnIndex(CollectTable.RESULT));
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
        String deleteClause = CollectTable.ORIGINAL + " == ? ";
        String[] deleteArgs = {original};
        db.delete(CollectTable.TABLE_NAME, deleteClause, deleteArgs);
    }

}
