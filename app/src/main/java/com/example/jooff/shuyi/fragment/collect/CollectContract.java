package com.example.jooff.shuyi.fragment.collect;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;
import com.example.jooff.shuyi.data.entity.Collect;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/14.
 * Tomorrow is a nice day
 */

public interface CollectContract {

    interface View extends BaseView {

        void showCollects(ArrayList<Collect> items);

        void showTranslate(String original);

        void showCollectDeleted();

    }

    interface Presenter extends BasePresenter {

        void deleteCollect(int position);

        void beginTranslate(int position);

    }
}
