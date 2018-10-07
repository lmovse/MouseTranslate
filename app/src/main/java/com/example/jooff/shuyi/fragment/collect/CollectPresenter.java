package com.example.jooff.shuyi.fragment.collect;

import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Collect;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/15.
 * Tomorrow is a nice day
 */

public class CollectPresenter implements CollectContract.Presenter {
    private AppDataSource.CollectDbSource mCollectDbSource;
    private CollectContract.View mCollectView;

    public CollectPresenter(AppDataSource.CollectDbSource collectDbSource, CollectContract.View collectView) {
        this.mCollectDbSource = collectDbSource;
        this.mCollectView = collectView;
    }

    @Override
    public void loadData() {
        ArrayList<Collect> items = mCollectDbSource.getCollects();
        if (!items.isEmpty()) {
            mCollectView.showCollects(items);
        }
    }

    @Override
    public void initTheme() {}

    @Override
    public void deleteCollect(int position) {
        String original = mCollectDbSource.getCollects().get(position).getOriginal();
        mCollectDbSource.deleteCollect(original);
        mCollectView.showCollectDeleted();
    }

    @Override
    public void beginTranslate(int position) {
        Collect item = mCollectDbSource.getCollects().get(position);
        String original = item.getOriginal();
        mCollectView.showTranslate(original);
    }

}
