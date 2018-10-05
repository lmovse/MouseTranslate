package com.example.jooff.shuyi.main;

import com.example.jooff.shuyi.base.BasePresenter;
import com.example.jooff.shuyi.base.BaseView;

/**
 * Created by Jooff on 2017/1/17.
 * Tomorrow is a nice day
 */

public interface MainContract {

    interface View extends BaseView{

        void showTrans(int transFrom, String s, String original);

        void showEmptyInput();

        void showSpinner(String lan);

        void hideSpinner();

        void startService();

        void stopService();

        void showNotification();

        void cancelNotification();

        void setTransparent(int colorPrimary);

        void setMaterial(int colorPrimaryDark);

        void initKitKatLayout();

        void initTheme(int themeId, int colorPrimary);

        void setAppTheme(int colorPrimary, int colorPrimaryDark);

        void openNightMode();

        void closeNightMode();

        void startIntent();

        void showHistory();

        void doFinish();

        void showConfirmFinish();

    }

    interface Presenter extends BasePresenter{

        void initSettings();

        void initTheme();

        void updateSetting(int position, boolean isChecked);

        void beginTrans(String original);

        void refreshSource(int source);

        void refreshResultLan(String lan);

        void handleClick();

        void removeAllHistory();
    }

}
