package com.example.jooff.shuyi.collect;

import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.entity.History;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/15.
 * Tomorrow is a nice day
 */

public class CollectPresenter implements CollectContract.Presenter {
    private AppDbSource.HistoryDbSource mHistoryDbSource;
    private CollectContract.View mHistoryView;

    public CollectPresenter(AppDbSource.HistoryDbSource historyDbSource, CollectContract.View historyView) {
        this.mHistoryDbSource = historyDbSource;
        this.mHistoryView = historyView;
    }

    @Override
    public void loadData() {
        ArrayList<History> items = mHistoryDbSource.getCollects();
        if (!items.isEmpty()) {
            mHistoryView.showCollects(items);
        }
    }

    @Override
    public void initTheme() {}

    @Override
    public void deleteCollect(int position) {
        String original = mHistoryDbSource.getHistorys().get(position).getOriginal();
        mHistoryDbSource.deleteCollect(original);
        mHistoryView.showCollectDeleted();
    }

    @Override
    public void beginTranslate(int position) {
        History item = mHistoryDbSource.getHistorys().get(position);
        String original = item.getOriginal();
        mHistoryView.showTranslate(original);
    }

}
