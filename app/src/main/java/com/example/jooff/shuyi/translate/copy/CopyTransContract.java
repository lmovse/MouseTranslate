package com.example.jooff.shuyi.translate.copy;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/2/1.
 * Tomorrow is a nice day
 */

public interface CopyTransContract {

    interface View extends BaseView {

        void showTrans(String original, String result);

        void showError();

        void showSpeechAndPhonetic(String phonetic);

    }

    interface Presenter extends BasePresenter {

        void playSpeech();

    }

}
