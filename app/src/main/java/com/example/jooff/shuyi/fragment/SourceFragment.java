package com.example.jooff.shuyi.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioGroup;

import com.example.jooff.shuyi.R;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.listener.OnAppStatusListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jooff on 2017/1/29.
 * Tomorrow is a nice day
 */

public class SourceFragment extends DialogFragment {
    private SharedPreferences mPref;
    @BindView(R.id.change_source) RadioGroup mSources;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.m_dialog_source, null);
        ButterKnife.bind(this, view);
        mPref = getActivity().getSharedPreferences(AppPref.ARG_NAME, Context.MODE_PRIVATE);
        mSources.check(mPref.getInt(AppPref.ARG_FROM, R.id.source_youdao));
        mSources.setOnCheckedChangeListener((group, sourceId) -> {
            OnAppStatusListener mListener = (OnAppStatusListener) getActivity();
            mListener.onSourceChanged(sourceId);
            mPref.edit().putInt(AppPref.ARG_FROM, sourceId).apply();
        });
        return new AlertDialog.Builder(getContext()).setTitle("换源").setView(view).create();
    }

}
