package com.example.jooff.shuyi.history;

import android.util.Log;

import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.entity.History;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/15.
 * Tomorrow is a nice day
 */

public class HistoryPresenter implements HistoryContract.Presenter {
    private AppDbSource.HistoryDbSource mHistoryDbSource;
    private HistoryContract.View mHistoryView;

    public HistoryPresenter(AppDbSource.HistoryDbSource historyDbSource, HistoryContract.View historyView) {
        this.mHistoryDbSource = historyDbSource;
        this.mHistoryView = historyView;
    }

    @Override
    public void loadData() {
        ArrayList<History> historys = mHistoryDbSource.getHistories();
        if (!historys.isEmpty()) {
            mHistoryView.showHistory(historys);
        }
    }

    @Override
    public void initTheme() {}

    @Override
    public void collectHistory(int position) {
        mHistoryDbSource.collectHistory(mHistoryDbSource.getHistories().get(position));
    }

    @Override
    public void unCollectHistory(int position) {
        String original = mHistoryDbSource.getHistories().get(position).getOriginal();
        Log.d("取消收藏项", "unCollectHistory: " + original);
        mHistoryDbSource.cancelCollect(original);
    }

    @Override
    public void deleteHistoryItem(int position) {
        String original = mHistoryDbSource.getHistories().get(position).getOriginal();
        Log.d("删除项", "deleteHistory: " + original);
        mHistoryDbSource.deleteHistory(original);
        mHistoryView.showHistoryDeleted();
    }

    @Override
    public void beginTranslate(int position) {
        History item = mHistoryDbSource.getHistories().get(position);
        String original = item.getOriginal();
        mHistoryView.showTranslate(original);
    }

}
