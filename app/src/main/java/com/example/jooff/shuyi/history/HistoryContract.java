package com.example.jooff.shuyi.history;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;
import com.example.jooff.shuyi.data.bean.HistoryBean;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/14.
 */

public interface HistoryContract {

    interface View extends BaseView {

        void showHistory(ArrayList<HistoryBean> items);

        void showTranslate(String original);

        void showHistoryDeleted();

        void setAppTheme(int color);

    }

    interface Presenter extends BasePresenter {

        void deleteHistoryItem(int position);

        void beginTranslate(int position);

        void initTheme();

    }
}
