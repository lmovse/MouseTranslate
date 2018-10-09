package com.example.jooff.shuyi.data;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.local.LocalSource;
import com.example.jooff.shuyi.data.remote.baidu.BaiDuSource;
import com.example.jooff.shuyi.data.remote.google.GoogleSource;
import com.example.jooff.shuyi.data.remote.jinshan.JinShanSource;
import com.example.jooff.shuyi.data.remote.shanbei.ShanBeiSource;
import com.example.jooff.shuyi.data.remote.yiyun.YiYunSource;
import com.example.jooff.shuyi.data.remote.youdao.YouDaoSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class AppDataRepository implements AppDataSource.TranslateRemoteSource, AppDataSource.HistoryDbSource, AppDataSource.CollectDbSource {

    private final LocalSource mLocalDbSource;

    private int transFrom;

    private volatile static AppDataRepository instance = null;

    public void setTransFrom(int transFrom) {
        ((LocalSource) sourceMap.get(FROM_COLLECT)).setTransFrom(transFrom);
        ((LocalSource) sourceMap.get(FROM_HISTORY)).setTransFrom(transFrom);
        this.transFrom = transFrom;
    }

    public void setResultLan(String resultLan) {
        ((GoogleSource) sourceMap.get(FROM_GOOGLE)).setResultLan(resultLan);
        ((BaiDuSource) sourceMap.get(FROM_BAIDU)).setResultLan(resultLan);
    }

    private AppDataRepository(Context context) {
        mLocalDbSource = LocalSource.getInstance(context);
        mLocalDbSource.setTransFrom(transFrom);
        sourceMap.put(FROM_BAIDU, new BaiDuSource());
        sourceMap.put(FROM_GOOGLE, new GoogleSource());
        sourceMap.put(FROM_YIYUN, new YiYunSource());
        sourceMap.put(FROM_YOUDAO, new YouDaoSource());
        sourceMap.put(FROM_JINSHAN, new JinShanSource());
        sourceMap.put(FROM_SHANBEI, new ShanBeiSource());
        sourceMap.put(FROM_COLLECT, LocalSource.getInstance(context));
        sourceMap.put(FROM_HISTORY, LocalSource.getInstance(context));
    }

    @SuppressLint("UseSparseArrays")
    private Map<Integer, AppDataSource.TranslateRemoteSource> sourceMap = new HashMap<>();

    public static AppDataRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDataRepository.class) {
                if (instance == null) {
                    instance = new AppDataRepository(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
       sourceMap.get(this.transFrom).getTrans(transUrl, callback);
    }

    @Override
    public String getUrl(String original) {
        return sourceMap.get(transFrom).getUrl(original);
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
