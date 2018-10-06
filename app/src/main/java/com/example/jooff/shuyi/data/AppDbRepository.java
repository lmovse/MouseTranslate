package com.example.jooff.shuyi.data;

import android.content.Context;

import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.local.LocalDbSource;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.data.remote.RemoteXmlSource;

import java.util.ArrayList;

import static com.example.jooff.shuyi.constant.TransSource.FROM_BAIDU;
import static com.example.jooff.shuyi.constant.TransSource.FROM_COLLECT;
import static com.example.jooff.shuyi.constant.TransSource.FROM_GOOGLE;
import static com.example.jooff.shuyi.constant.TransSource.FROM_HISTORY;
import static com.example.jooff.shuyi.constant.TransSource.FROM_JINSHAN;
import static com.example.jooff.shuyi.constant.TransSource.FROM_SHANBEI;
import static com.example.jooff.shuyi.constant.TransSource.FROM_YIYUN;
import static com.example.jooff.shuyi.constant.TransSource.FROM_YOUDAO;

/**
 * Created by Jooff on 2017/1/20.
 * 主要的数据仓库，可以说是一个数据源的代理对象，
 * presenter 通过它来访问数据，包含本地数据与远程数据，
 * Repository 本身并不能保存与获取数据，是通过数据源的增删改查来实现
 */

public class AppDbRepository implements AppDbSource.TranslateDbSource, AppDbSource.HistoryDbSource, AppDbSource.CollectDbSource {

    private final LocalDbSource mLocalDbSource;

    private static AppDbRepository instance = null;

    private AppDbRepository(Context context) {
        mLocalDbSource = LocalDbSource.getInstance(context);
    }

    public static AppDbRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppDbRepository(context);
        }
        return instance;
    }

    @Override
    public void getTrans(int tranFrom, String transUrl, AppDbSource.TranslateCallback callback) {
        switch (tranFrom) {
            case FROM_COLLECT:
                mLocalDbSource.getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_HISTORY:
                mLocalDbSource.getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_JINSHAN:
                RemoteXmlSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_BAIDU:
                RemoteJsonSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_YIYUN:
                RemoteJsonSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_SHANBEI:
                RemoteJsonSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_YOUDAO:
                RemoteJsonSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            case FROM_GOOGLE:
                RemoteJsonSource.getInstance().getTrans(tranFrom, transUrl, callback);
                break;
            default:
                break;
        }
    }

    @Override
    public void saveHistory(History history) {
        mLocalDbSource.saveHistory(history);
    }

    @Override
    public void collectHistory(History history) {
        mLocalDbSource.collectHistory(history);
    }

    @Override
    public void cancelCollect(String original) {
        mLocalDbSource.cancelCollect(original);
    }

    @Override
    public History getHistory(String original) {
        return mLocalDbSource.getHistory(original);
    }

    @Override
    public ArrayList<History> getHistories() {
        return mLocalDbSource.getHistories();
    }

    @Override
    public void deleteHistory(String original) {
        mLocalDbSource.deleteHistory(original);
    }

    @Override
    public void deleteAllHistory() {
        mLocalDbSource.deleteAllHistory();
    }

    @Override
    public Collect getCollect(String original) {
        return mLocalDbSource.getCollect(original);
    }

    @Override
    public ArrayList<Collect> getCollects() {
        return mLocalDbSource.getCollects();
    }

    @Override
    public void deleteCollect(String original) {
        mLocalDbSource.deleteCollect(original);
    }

}
