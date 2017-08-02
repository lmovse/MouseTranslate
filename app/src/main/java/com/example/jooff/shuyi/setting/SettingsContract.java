package com.example.jooff.shuyi.setting;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/1/24.
 * Tomorrow is a nice day
 */

public class SettingsContract {

    interface View extends BaseView{

        void showSettings(boolean[] settings);

    }

    interface Presenter extends BasePresenter {

        void saveSettings(boolean[] settings);

    }
}
