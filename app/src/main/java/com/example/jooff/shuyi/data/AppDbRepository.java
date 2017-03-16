package com.example.jooff.shuyi.data;

import android.content.Context;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.data.bean.HistoryBean;
import com.example.jooff.shuyi.data.local.LocalDbSource;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.data.remote.RemoteXmlSource;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/20.
 */

public class AppDbRepository implements AppDbSource.TranslateDbSource, AppDbSource.HistoryDbSource {
    private int tranFrom = 0;
    private final LocalDbSource mLocalDbSource;
    public static AppDbRepository instance = null;

    public AppDbRepository setTranFrom(int tranFrom) {
        this.tranFrom = tranFrom;
        return this;
    }

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
    public void getTrans(String original, AppDbSource.TranslateCallback callback) {
        switch (tranFrom) {
            case 0:
                mLocalDbSource.getTrans(original, callback);
                break;
            case R.id.source_jinshan:
                RemoteXmlSource.getInstance().getTrans(original, callback);
                break;
            case R.id.source_baidu:
                RemoteJsonSource.getInstance().setSource(0).getTrans(original, callback);
                break;
            case R.id.source_yiyun:
                RemoteJsonSource.getInstance().setSource(1).getTrans(original, callback);
                break;
            case R.id.source_shanbei:
                RemoteJsonSource.getInstance().setSource(2).getTrans(original, callback);
                break;
            case R.id.source_youdao:
                RemoteJsonSource.getInstance().setSource(3).getTrans(original, callback);
                break;
        }
    }

    @Override
    public void saveHistoryItem(HistoryBean historyBean) {
        mLocalDbSource.saveHistoryItem(historyBean);
    }

    @Override
    public HistoryBean getHistoryItem(String original) {
        return mLocalDbSource.getHistoryItem(original);
    }

    @Override
    public ArrayList<HistoryBean> getHistory() {
        return mLocalDbSource.getHistory();
    }

    @Override
    public void deleteHistoryItem(String original) {
        mLocalDbSource.deleteHistoryItem(original);
    }

}
