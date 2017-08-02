package com.example.jooff.shuyi.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.common.OnAppStatusListener;
import com.example.jooff.shuyi.common.ThemeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jooff on 2017/1/24.
 * Tomorrow is a nice day
 */

public class SettingsFragment extends DialogFragment implements SettingsContract.View, CompoundButton.OnCheckedChangeListener {
    private SettingsContract.Presenter mPresenter;
    private OnAppStatusListener mListener;

    @BindView(R.id.copy_trans)
    SwitchCompat copyTrans;

    @BindView(R.id.trans_mode)
    SwitchCompat transMode;

    @BindView(R.id.night_mode)
    SwitchCompat nightMode;

    @BindView(R.id.note_mode)
    SwitchCompat noteMode;

    @OnClick(R.id.color_theme)
    public void showThemes() {
        ThemeFragment fragment = new ThemeFragment();
        fragment.show(this.getActivity().getSupportFragmentManager(), "tf");
        this.dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.m_dialog_setting, null);
        ButterKnife.bind(this, view);
        mPresenter = new SettingsPresenter(getActivity().getSharedPreferences(Constant.ARG_NAME, Context.MODE_PRIVATE), this);
        initView();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_settings)
                .setView(view)
                .create();
    }

    @Override
    public void showSettings(boolean[] settings) {
        copyTrans.setChecked(settings[0]);
        transMode.setChecked(settings[1]);
        nightMode.setChecked(settings[2]);
        noteMode.setChecked(settings[3]);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.copy_trans:
                mListener.onSettingChanged(0, copyTrans.isChecked());
                break;
            case R.id.trans_mode:
                mListener.onSettingChanged(1, transMode.isChecked());
                break;
            case R.id.night_mode:
                mListener.onSettingChanged(2, nightMode.isChecked());
                this.dismiss();
                break;
            case R.id.note_mode:
                mListener.onSettingChanged(3, noteMode.isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        boolean[] settings = {copyTrans.isChecked()
                , transMode.isChecked()
                , nightMode.isChecked()
                , noteMode.isChecked()};
        mPresenter.saveSettings(settings);
    }

    @Override
    public void initView() {
        mPresenter.loadData();
        mListener = (OnAppStatusListener) getActivity();
        copyTrans.setOnCheckedChangeListener(this);
        transMode.setOnCheckedChangeListener(this);
        nightMode.setOnCheckedChangeListener(this);
        noteMode.setOnCheckedChangeListener(this);
    }

}
