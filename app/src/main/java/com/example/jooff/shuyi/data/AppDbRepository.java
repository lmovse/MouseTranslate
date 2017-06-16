package com.example.jooff.shuyi.data;

import android.content.Context;

import com.example.jooff.shuyi.R;
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
    public void saveHistory(History history) {
        mLocalDbSource.saveHistory(history);
    }

    @Override
    public History getHistory(String original) {
        return mLocalDbSource.getHistory(original);
    }

    @Override
    public ArrayList<History> getHistorys() {
        return mLocalDbSource.getHistorys();
    }

    @Override
    public void deleteHistory(String original) {
        mLocalDbSource.deleteHistory(original);
    }

}
