package com.example.jooff.shuyi.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.constant.SettingDefault;
import com.example.jooff.shuyi.constant.TransMode;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDataRepository;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jooff on 2017/1/17.
 * Tomorrow is a nice day
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    private SharedPreferences mPref;

    private Boolean isDoubleClick;

    private Boolean isCopyTrans;

    private Boolean isTransMode;

    private Boolean isNightMode;

    private Boolean isNoteMode;

    private String mResultLan;

    private int transFrom;

    private int colorPrimary;

    private int colorPrimaryDark;

    private int themeId;

    private AppDataRepository mAppDbRepository;

    public MainPresenter(SharedPreferences sharedPreferences, MainContract.View mainView,
                         AppDataRepository appDbRepository) {
        mPref = sharedPreferences;
        mPref.registerOnSharedPreferenceChangeListener((spf, key) -> {
            if (key.equals(AppPref.ARG_FROM)) {
                transFrom = spf.getInt(AppPref.ARG_FROM, SettingDefault.TRANS_FROM);
            }
        });
        mView = mainView;
        mAppDbRepository = appDbRepository;
        isCopyTrans = mPref.getBoolean(AppPref.ARG_COPY, false);
        isTransMode = mPref.getBoolean(AppPref.ARG_TRANS, false);
        isNightMode = mPref.getBoolean(AppPref.ARG_NIGHT, false);
        isNoteMode = mPref.getBoolean(AppPref.ARG_NOTE, false);
        colorPrimary = mPref.getInt(AppPref.ARG_PRIMARY, Color.parseColor(SettingDefault.COLOR_PRIMARY));
        colorPrimaryDark = mPref.getInt(AppPref.ARG_DARK, Color.parseColor(SettingDefault.COLOR_PRIMARY_DARK));
        themeId = mPref.getInt(AppPref.ARG_THEME, 0);
        transFrom = mPref.getInt(AppPref.ARG_FROM, SettingDefault.TRANS_FROM);
        mResultLan = mPref.getString(AppPref.ARG_LAN, SettingDefault.RESULT_LAN);
        isDoubleClick = false;
    }

    @Override
    public void initTheme() {
        MyApp.sIsNightMode = isNightMode;
        if (isNightMode) {
            themeId = 1024;
            colorPrimary = Color.parseColor("#35464e");
            mView.openNightMode();
        }
        mView.initTheme(themeId, colorPrimary);
    }

    @Override
    public void initSettings() {
        if (isCopyTrans) {
            mView.startService();
        }
        if (!isNightMode) {
            mView.setAppTheme(colorPrimary, colorPrimaryDark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (isTransMode) {
                    mView.setTransparent(colorPrimary);
                } else {
                    mView.setMaterial(colorPrimaryDark);
                }
            }
        }
        if (transFrom == TransSource.FROM_BAIDU|| transFrom == TransSource.FROM_GOOGLE) {
            mView.showSpinner(mResultLan, transFrom);
        }
        if (isNoteMode) {
            mView.showNotification();
        }
    }

    @Override
    public void updateSetting(int position, boolean isChecked) {
        switch (position) {
            case TransMode.COPY_TANS:
                if (isChecked) {
                    mView.startService();
                } else {
                    mView.stopService();
                }
                break;
            case TransMode.TRAS_MODE:
                if (!isNightMode) {
                    if (isChecked) {
                        mView.setTransparent(colorPrimary);
                    } else {
                        mView.setMaterial(colorPrimaryDark);
                    }
                }
                break;
            case TransMode.NIGHT_MODE:
                if (isChecked) {
                    mView.openNightMode();
                    mView.startIntent();
                } else {
                    mView.closeNightMode();
                    mView.startIntent();
                }
                break;
            case TransMode.NOTI_TRANS:
                if (isChecked) {
                    mView.showNotification();
                } else {
                    mView.cancelNotification();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beginTrans(String original) {
        if (TextUtils.isEmpty(original)) {
            mView.showEmptyInput();
        } else {
            mView.showTrans(original);
        }
    }

    @Override
    public void loadData() {
        mView.showHistory();
    }

    @Override
    public void refreshSource(int source) {
        transFrom = source;
        mResultLan = SettingDefault.RESULT_LAN;
        mPref.edit().putString(AppPref.ARG_LAN, mResultLan).apply();
        if (transFrom == TransSource.FROM_BAIDU || transFrom == TransSource.FROM_GOOGLE) {
            mView.showSpinner(mResultLan, transFrom);
        } else {
            mView.hideSpinner();
        }
    }

    @Override
    public void refreshResultLan(String lan) {
        mResultLan = lan;
        mPref.edit().putString(AppPref.ARG_LAN, mResultLan).apply();
    }

    @Override
    public void handleClick() {
        Timer mTimer = new Timer();
        if (!isDoubleClick) {
            isDoubleClick = true;
            mView.showConfirmFinish();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isDoubleClick = false;
                }
            }, 2000);
        } else mView.doFinish();
    }

    @Override
    public void removeAllHistory() {
        mAppDbRepository.deleteAllHistory();
    }

    @Override
    public int getSource() {
        return mPref.getInt(AppPref.ARG_FROM, SettingDefault.TRANS_FROM);
    }

}
