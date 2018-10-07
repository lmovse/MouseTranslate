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
import com.example.jooff.shuyi.activity.MainActivity;
import com.example.jooff.shuyi.util.AnimationUtil;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.Objects.requireNonNull;

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
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.m_dialog_theme, null);
        ButterKnife.bind(this, view);
        mPreferences = getActivity().getSharedPreferences(AppPref.ARG_NAME, Context.MODE_PRIVATE);
        isNightMode = mPreferences.getBoolean(AppPref.ARG_NIGHT, false);
        return new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("主题").setView(view).create();
    }

    @OnClick({R.id.color_girl,
            R.id.color_pink,
            R.id.color_blue_grey,
            R.id.color_blue,
            R.id.color_green,
            R.id.color_brown,
            R.id.color_teal,
            R.id.color_red,
            R.id.color_purple})
    public void setColor(Button color) {
        if (isNightMode) {
            Toast.makeText(this.getActivity(), R.string.close_night_mode, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (color.getId()) {
            case R.id.color_girl:
                colorPrimary = R.color.colorPrimaryGirl;
                colorPrimaryDark = R.color.colorPrimaryDarkGirl;
                themeId = ThemeColor.GIRL;
                break;
            case R.id.color_pink:
                colorPrimary = R.color.colorPrimaryPink;
                colorPrimaryDark = R.color.colorPrimaryDarkPink;
                themeId = ThemeColor.PINK;
                break;
            case R.id.color_blue_grey:
                colorPrimary = R.color.colorPrimaryBlueGrey;
                colorPrimaryDark = R.color.colorPrimaryDarkBlueGrey;
                themeId = ThemeColor.BLUE_GREY;
                break;
            case R.id.color_blue:
                colorPrimary = R.color.colorPrimaryBlue;
                colorPrimaryDark = R.color.colorPrimaryDarkBlue;
                themeId = ThemeColor.BLUE;
                break;
            case R.id.color_green:
                colorPrimary = R.color.colorPrimaryGreen;
                colorPrimaryDark = R.color.colorPrimaryDarkGreen;
                themeId = ThemeColor.GREEN;
                break;
            case R.id.color_brown:
                colorPrimary = R.color.colorPrimaryBrown;
                colorPrimaryDark = R.color.colorPrimaryDarkBrown;
                themeId = ThemeColor.BROWN;
                break;
            case R.id.color_teal:
                colorPrimary = R.color.colorPrimaryTeal;
                colorPrimaryDark = R.color.colorPrimaryDarkTeal;
                themeId = ThemeColor.TEAL;
                break;
            case R.id.color_red:
                colorPrimary = R.color.colorPrimaryRed;
                colorPrimaryDark = R.color.colorPrimaryDarkRed;
                themeId = ThemeColor.RED;
                break;
            case R.id.color_purple:
                colorPrimary = R.color.colorPrimaryPurple;
                colorPrimaryDark = R.color.colorPrimaryDarkPurple;
                themeId = ThemeColor.PURPLE;
                break;
            default:
                break;
        }
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putInt(AppPref.ARG_PRIMARY, ContextCompat.getColor(requireNonNull(getContext()), colorPrimary));
        mEditor.putInt(AppPref.ARG_DARK, ContextCompat.getColor(getContext(), colorPrimaryDark));
        mEditor.putInt(AppPref.ARG_THEME, themeId);
        mEditor.apply();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            AnimationUtil.startActivity(requireNonNull(getActivity()), new Intent(getActivity(), MainActivity.class), color, colorPrimary, 618);
        }
        this.dismiss();
    }

}
