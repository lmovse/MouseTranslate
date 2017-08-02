package com.example.jooff.shuyi.translate.dialog;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/2/1.
 * Tomorrow is a nice day
 */

public interface QuickTransContract {

    interface View extends BaseView {

        void showTrans(String original, String result);

        void showError();

    }

    interface Presenter extends BasePresenter {

        void beginTrans(String original);

    }

}
