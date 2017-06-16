package com.example.jooff.shuyi.translate.main;

import android.content.Context;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/1/18.
 * Tomorrow is a nice day
 */

public interface MainTranslateContract {

    interface View extends BaseView {

        void showResult(String result);

        void showPhonetic(String usPhonetic, String ukPhonetic);

        void showExplain(String explain);

        void showExplainEn(String explainEn);

        void showWeb(String web);

        void showError();

        void showNotSupport();

        void showCompletedTrans(String original);

        void showCopySuccess();

        void setAppTheme(int color);

    }

    interface Presenter extends BasePresenter {

        void playSpeech(int speechFrom);

        void setTextToClip(Context context, String trans);

        void initTheme();

    }

}
