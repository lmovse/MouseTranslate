package com.example.jooff.shuyi.settings;

import android.content.SharedPreferences;

import static com.example.jooff.shuyi.common.Constant.ARG_COPY;
import static com.example.jooff.shuyi.common.Constant.ARG_NIGHT;
import static com.example.jooff.shuyi.common.Constant.ARG_NOTE;
import static com.example.jooff.shuyi.common.Constant.ARG_TRANS;

/**
 * Created by Jooff on 2017/1/24.
 * Tomorrow is a nice day
 */

public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View mView;
    private SharedPreferences mPref;

    public SettingsPresenter(SharedPreferences sharedPreferences, SettingsContract.View settingsView) {
        mPref = sharedPreferences;
        mView = settingsView;
    }

    @Override
    public void loadData() {
        boolean[] mSettings = {mPref.getBoolean(ARG_COPY, false)
                , mPref.getBoolean(ARG_TRANS, false)
                , mPref.getBoolean(ARG_NIGHT, false)
                , mPref.getBoolean(ARG_NOTE, false)};
        mView.showSettings(mSettings);
    }

    @Override
    public void saveSettings(boolean[] settings) {
        SharedPreferences.Editor mEditor = mPref.edit();
        mEditor.putBoolean(ARG_COPY, settings[0]);
        mEditor.putBoolean(ARG_TRANS, settings[1]);
        mEditor.putBoolean(ARG_NIGHT, settings[2]);
        mEditor.putBoolean(ARG_NOTE, settings[3]);
        mEditor.apply();
    }

}
