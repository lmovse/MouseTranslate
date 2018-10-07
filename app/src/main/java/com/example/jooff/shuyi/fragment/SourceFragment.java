package com.example.jooff.shuyi.fragment;

import android.annotation.SuppressLint;
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
import com.example.jooff.shuyi.constant.SettingDefault;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.listener.OnAppStatusListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jooff on 2017/1/29.
 * Tomorrow is a nice day
 */

public class SourceFragment extends DialogFragment {

    private SharedPreferences mPref;

    @BindView(R.id.change_source) RadioGroup mSources;

    @SuppressLint("UseSparseArrays")
    private Map<Integer, Integer> idFromMap = new HashMap<>();

    public SourceFragment() {
        idFromMap.put(R.id.source_baidu, TransSource.FROM_BAIDU);
        idFromMap.put(R.id.source_google, TransSource.FROM_GOOGLE);
        idFromMap.put(R.id.source_jinshan, TransSource.FROM_JINSHAN);
        idFromMap.put(R.id.source_yiyun, TransSource.FROM_YIYUN);
        idFromMap.put(R.id.source_youdao, TransSource.FROM_YOUDAO);
        idFromMap.put(R.id.source_shanbei, TransSource.FROM_SHANBEI);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.m_dialog_source, null);
        ButterKnife.bind(this, view);
        mPref = getActivity().getSharedPreferences(AppPref.ARG_NAME, Context.MODE_PRIVATE);
        Set<Map.Entry<Integer, Integer>> entries = idFromMap.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            if (entry.getValue().equals(mPref.getInt(AppPref.ARG_FROM, SettingDefault.TRANS_FROM))) {
                mSources.check(entry.getKey());
                break;
            }
        }
        mSources.setOnCheckedChangeListener((group, sourceId) -> {
            OnAppStatusListener mListener = (OnAppStatusListener) getActivity();
            Integer transFrom = idFromMap.get(sourceId);
            mPref.edit().putInt(AppPref.ARG_FROM, transFrom).apply();
            mListener.onSourceChanged(transFrom);
        });
        return new AlertDialog.Builder(getContext()).setTitle("换源").setView(view).create();
    }

}
