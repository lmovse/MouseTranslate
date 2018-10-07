package com.example.jooff.shuyi.fragment.history;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;
import com.example.jooff.shuyi.data.entity.History;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/14.
 * Tomorrow is a nice day
 */

public interface HistoryContract {

    interface View extends BaseView {

        void showHistory(ArrayList<History> items);

        void showTranslate(String original);

        void showHistoryDeleted();

    }

    interface Presenter extends BasePresenter {

        void deleteHistoryItem(int position);

        void beginTranslate(int position);

        void collectHistory(int position);

        void unCollectHistory(int position);
    }
}
