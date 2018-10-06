package com.example.jooff.shuyi.data;

import android.content.Context;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.local.LocalDbSource;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.data.remote.RemoteXmlSource;

import java.util.ArrayList;

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
            case TransSource.FROM_COLLECT:
                mLocalDbSource.getTrans(TransSource.FROM_COLLECT, transUrl, callback);
                break;
            case TransSource.FROM_HISTORY:
                mLocalDbSource.getTrans(TransSource.FROM_HISTORY, transUrl, callback);
                break;
            case R.id.source_jinshan:
                RemoteXmlSource.getInstance().getTrans(TransSource.FROM_JINSHAN, transUrl, callback);
                break;
            case R.id.source_baidu:
                RemoteJsonSource.getInstance().getTrans(TransSource.FROM_BAIDU, transUrl, callback);
                break;
            case R.id.source_yiyun:
                RemoteJsonSource.getInstance().getTrans(TransSource.FROM_YIYUN, transUrl, callback);
                break;
            case R.id.source_shanbei:
                RemoteJsonSource.getInstance().getTrans(TransSource.FROM_SHANBEI, transUrl, callback);
                break;
            case R.id.source_youdao:
                RemoteJsonSource.getInstance().getTrans(TransSource.FROM_YOUDAO, transUrl, callback);
            case R.id.source_google:
                RemoteJsonSource.getInstance().getTrans(TransSource.FROM_GOOGLE, transUrl, callback);
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
