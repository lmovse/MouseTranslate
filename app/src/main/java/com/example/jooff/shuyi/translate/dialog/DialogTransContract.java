package com.example.jooff.shuyi.translate.dialog;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/2/1.
 */

public interface DialogTransContract {

    interface View extends BaseView {

        void showTrans(String original, String result);

        void showError();

        void showSpeech();

        void setAppTheme(int color);
    }

    interface Presenter extends BasePresenter {

        void beginTrans(String original);

        void playSpeech();

        void initTheme();
    }

}
