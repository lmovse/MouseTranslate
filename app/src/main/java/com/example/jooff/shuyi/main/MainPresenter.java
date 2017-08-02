package com.example.jooff.shuyi.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.api.BaiDuTransAPI;
import com.example.jooff.shuyi.api.JinShanTransApi;
import com.example.jooff.shuyi.api.ShanBeiTransApi;
import com.example.jooff.shuyi.api.YiYunTransApi;
import com.example.jooff.shuyi.api.YouDaoTransAPI;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.constant.SettingDefault;
import com.example.jooff.shuyi.constant.TransMode;
import com.example.jooff.shuyi.util.MD5Format;
import com.example.jooff.shuyi.util.UTF8Format;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public MainPresenter(SharedPreferences sharedPreferences, MainContract.View mainView) {
        mPref = sharedPreferences;
        mView = mainView;
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
        if (transFrom == R.id.source_baidu) {
            mView.showSpinner(mResultLan);
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
        String transUrl = "";
        if (TextUtils.isEmpty(original)) {
            mView.showEmptyInput();
        } else {
            switch (transFrom) {
                case R.id.source_youdao:
                    transUrl = YouDaoTransAPI.YOUDAO_URL
                            + YouDaoTransAPI.YOUDAO_ORIGINAL + UTF8Format.encode(original).replace("\n", "");
                    break;
                case R.id.source_jinshan:
                    transUrl = JinShanTransApi.JINSHAN_URL
                            + JinShanTransApi.JINSHAN_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                            + JinShanTransApi.JINSHAN_KEY;
                    break;
                case R.id.source_baidu:
                    transUrl = BaiDuTransAPI.BAIDU_URL
                            + BaiDuTransAPI.BAIDU_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                            + BaiDuTransAPI.BAIDU_ORIGINAL_LAN
                            + BaiDuTransAPI.BAIDU_RESULT_LAN + mResultLan
                            + BaiDuTransAPI.BAIDU_ID
                            + BaiDuTransAPI.BAIDU_SALT + String.valueOf(1234567899)
                            + BaiDuTransAPI.BAIDU_SIGN + MD5Format.getMd5(BaiDuTransAPI.BAIDU_ID.substring(7) + original + 1234567899 + BaiDuTransAPI.BAIDU_KEY);
                    break;
                case R.id.source_yiyun:
                    String originalLan, resultLan;
                    // 设置源语言与目标语言
                    Pattern p = Pattern.compile("[a-zA-Z]+");
                    Matcher m = p.matcher(original);
                    if (m.matches()) {
                        originalLan = "en";
                        resultLan = "zh";
                    } else {
                        originalLan = "zh";
                        resultLan = "en";
                    }
                    transUrl = YiYunTransApi.YIYUN_URL
                            + YiYunTransApi.YIYUN_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                            + YiYunTransApi.YIYUN_ORIGINAL_LAN + originalLan
                            + YiYunTransApi.YIYUN_RESULT_LAN + resultLan
                            + YiYunTransApi.YIYUN_ID
                            + YiYunTransApi.YIYUN_KEY;
                    break;
                case R.id.source_shanbei:
                    transUrl = ShanBeiTransApi.SHANBEI_SEARCH_URL + UTF8Format.encode(original).replace("\n", "");
                    break;
                default:
                    break;
            }
            mView.showTrans(transFrom, transUrl);
        }
    }

    @Override
    public void loadData() {
        mView.showHistory();
    }

    @Override
    public void refreshSource(int source) {
        transFrom = source;
        if (transFrom == R.id.source_baidu) {
            mView.showSpinner(mResultLan);
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

}
