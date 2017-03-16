package com.example.jooff.shuyi.history;

import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.bean.HistoryBean;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/15.
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
        ArrayList<HistoryBean> items = mHistoryDbSource.getHistory();
        if (!items.isEmpty()) {
            mHistoryView.showHistory(items);
        }
    }

    @Override
    public void initTheme() {
        if (!Constant.sIsNightMode) {
            mHistoryView.setAppTheme(Constant.sColorPrimary);
        }
    }

    @Override
    public void deleteHistoryItem(int position) {
        String original = mHistoryDbSource.getHistory().get(position).getTextOriginal();
        mHistoryDbSource.deleteHistoryItem(original);
        mHistoryView.showHistoryDeleted();
    }

    @Override
    public void beginTranslate(int position) {
        HistoryBean item = mHistoryDbSource.getHistory().get(position);
        String original = item.getTextOriginal();
        mHistoryView.showTranslate(original);
    }

}
