package com.example.jooff.shuyi.listener;

/**
 * Created by Jooff on 2017/1/30.
 * Tomorrow is a nice day
 */

public interface OnAppStatusListener {

    void onSettingChanged(int position, boolean isChecked);

    void onSourceChanged(int source);

    void onSuccess(int transFrom, String original);

}
