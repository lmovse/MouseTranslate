package com.example.jooff.shuyi.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.constant.ThemeColor;
import com.example.jooff.shuyi.main.MainActivity;
import com.example.jooff.shuyi.util.AnimationUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jooff on 2017/1/29.
 * Tomorrow is a nice day
 */

public class ThemeFragment extends DialogFragment {
    private SharedPreferences mPreferences;
    private int colorPrimary;
    private int colorPrimaryDark;
    private boolean isNightMode;
    private int themeId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.m_dialog_theme, null);
        ButterKnife.bind(this, view);
        mPreferences = getActivity().getSharedPreferences(AppPref.ARG_NAME, Context.MODE_PRIVATE);
        isNightMode = mPreferences.getBoolean(AppPref.ARG_NIGHT, false);
        return new AlertDialog.Builder(getContext()).setTitle("主题").setView(view).create();
    }

    @OnClick({R.id.color0,
            R.id.color1,
            R.id.color2,
            R.id.color3,
            R.id.color4,
            R.id.color5,
            R.id.color6,
            R.id.color7,
            R.id.color8})
    public void setColor(Button color) {
        if (isNightMode) {
            Toast.makeText(this.getActivity(), R.string.close_night_mode, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (color.getId()) {
            case R.id.color0:
                colorPrimary = R.color.colorPrimary;
                colorPrimaryDark = R.color.colorPrimaryDark;
                themeId = ThemeColor.RED;
                break;
            case R.id.color1:
                colorPrimary = R.color.colorPrimary1;
                colorPrimaryDark = R.color.colorPrimaryDark1;
                themeId = ThemeColor.PINK;
                break;
            case R.id.color2:
                colorPrimary = R.color.colorPrimary2;
                colorPrimaryDark = R.color.colorPrimaryDark2;
                themeId = ThemeColor.BULU_GREY;
                break;
            case R.id.color3:
                colorPrimary = R.color.colorPrimary3;
                colorPrimaryDark = R.color.colorPrimaryDark3;
                themeId = ThemeColor.BULU;
                break;
            case R.id.color4:
                colorPrimary = R.color.colorPrimary4;
                colorPrimaryDark = R.color.colorPrimaryDark4;
                themeId = ThemeColor.GREEN;
                break;
            case R.id.color5:
                colorPrimary = R.color.colorPrimary5;
                colorPrimaryDark = R.color.colorPrimaryDark5;
                themeId = ThemeColor.BROWN;
                break;
            case R.id.color6:
                colorPrimary = R.color.colorPrimary6;
                colorPrimaryDark = R.color.colorPrimaryDark6;
                themeId = ThemeColor.TEAL;
                break;
            case R.id.color7:
                colorPrimary = R.color.colorPrimary7;
                colorPrimaryDark = R.color.colorPrimaryDark7;
                themeId = ThemeColor.GIRL;
                break;
            case R.id.color8:
                colorPrimary = R.color.colorPrimary8;
                colorPrimaryDark = R.color.colorPrimaryDark8;
                themeId = ThemeColor.PURPLE;
                break;
            default:
                break;
        }
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putInt(AppPref.ARG_PRIMARY, ContextCompat.getColor(getContext(), colorPrimary));
        mEditor.putInt(AppPref.ARG_DARK, ContextCompat.getColor(getContext(), colorPrimaryDark));
        mEditor.putInt(AppPref.ARG_THEME, themeId);
        mEditor.apply();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            AnimationUtil.startActivity(getActivity(), new Intent(getActivity(), MainActivity.class), color, colorPrimary, 618);
        }
        this.dismiss();
    }

}
