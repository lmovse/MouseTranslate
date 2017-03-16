package com.example.jooff.shuyi.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jooff.shuyi.data.AppDbSchema;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.bean.HistoryBean;
import com.example.jooff.shuyi.data.bean.TranslateBean;

import java.util.ArrayList;

/**
 * Created by Jooff on 2016/8/14.
 */

public class LocalDbSource implements AppDbSource.HistoryDbSource,AppDbSource.TranslateDbSource {
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

    @Override
    public void getTrans(String original, AppDbSource.TranslateCallback callback) {
        HistoryBean his = getHistoryItem(original);
        TranslateBean trans = new TranslateBean();
        if (his.getTextResult() != null) {
            trans.setQuery(his.getTextOriginal());
            trans.setTranslation(his.getTextResult());
            callback.onResponse(trans);
        } else {
            callback.onError(0);
        }
    }

    @Override
    public void saveHistoryItem(HistoryBean historyBean) {
        if (getHistoryItem(historyBean.getTextOriginal()) == null) {
            ContentValues values = new ContentValues();
            values.put(AppDbSchema.HistoryTable.ORIGINAL, historyBean.getTextOriginal());
            values.put(AppDbSchema.HistoryTable.RESULT, historyBean.getTextResult());
            db.insert(AppDbSchema.HistoryTable.TABLE_NAME, null, values);
        }
    }

    @Override
    public HistoryBean getHistoryItem(String original) {
        HistoryBean item = null;
        String queryLogic = AppDbSchema.HistoryTable.ORIGINAL + " == ? ";
        String[] queryArgs = {original};
        Cursor cursor = db.query(AppDbSchema.HistoryTable.TABLE_NAME, null, queryLogic, queryArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String textOriginal = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.ORIGINAL));
            String textResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.RESULT));
            item = new HistoryBean(textOriginal, textResult);
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    @Override
    public ArrayList<HistoryBean> getHistory() {
        ArrayList<HistoryBean> items = new ArrayList<>();
        Cursor cursor = db.query(AppDbSchema.HistoryTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String textOriginal = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.ORIGINAL));
                String textResult = cursor.getString(cursor.getColumnIndex(AppDbSchema.HistoryTable.RESULT));
                HistoryBean item = new HistoryBean(textOriginal, textResult);
                items.add(0, item);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return items;
    }

    @Override
    public void deleteHistoryItem(String original) {
        //指定删除的逻辑
        String deleteLogic = AppDbSchema.HistoryTable.ORIGINAL + " == ? ";
        String[] deleteArgs = {original};
        db.delete(AppDbSchema.HistoryTable.TABLE_NAME, deleteLogic, deleteArgs);
    }

}
