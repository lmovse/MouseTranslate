package com.example.jooff.shuyi.translate.dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.example.jooff.shuyi.api.YouDaoTransAPI;
import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.bean.TranslateBean;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.util.UTF8Format;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by Jooff on 2017/2/1.
 */

public class DialogTransPresenter implements DialogTransContract.Presenter {
    private DialogTransContract.View mView;
    private int colorPrimary;
    private boolean isNightMode;
    private String mUsSpeech;
    private String original;

    public DialogTransPresenter(SharedPreferences preferences, Intent intent, DialogTransContract.View view) {
        mView = view;
        colorPrimary = preferences.getInt(Constant.ARG_PRIMARY, Color.parseColor("#F44336"));
        isNightMode = preferences.getBoolean(Constant.ARG_NIGHT, false);
        original = getOriginal(intent);
    }

    @Override
    public void loadData() {
        String url;
        if (original != null) {
            url = YouDaoTransAPI.YOUDAO_URL
                    + YouDaoTransAPI.YOUDAO_ORIGINAL + UTF8Format.encode(original.replace("\n", ""));
            RemoteJsonSource.getInstance().setSource(3).getTrans(url, new AppDbSource.TranslateCallback() {
                @Override
                public void onResponse(TranslateBean response) {
                    String original = response.getQuery();
                    if (response.getExplains() != null) {
                        String explain = response.getExplains();
                        mView.showTrans(original, explain);
                    } else if (response.getTranslation() != null) {
                        mView.showTrans(original, response.getTranslation());
                    }
                    Log.d(TAG, "onResponse: " + response.getUsPhonetic());
                    if (response.getUkPhonetic() != null) {
                        Log.d(TAG, "onResponse: ok" );
                        mUsSpeech = response.getUsSpeech();
                        mView.showSpeech();
                    }
                }

                @Override
                public void onError(int errorCode) {
                    mView.showError();
                }
            });
        }
    }

    @Override
    public void beginTrans(String original) {
        this.original = original;
        loadData();
    }

    @Override
    public void playSpeech() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mPlay = new MediaPlayer();
                try {
                    mPlay.setDataSource(mUsSpeech);
                    mPlay.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlay.start();
            }
        }).start();
    }

    @Override
    public void initTheme() {
        Log.d(TAG, "initLayout: " + isNightMode);
        if (!isNightMode) {
            mView.setAppTheme(colorPrimary);
        }
    }

    private String getOriginal(Intent intent) {
        String original = null;
        if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            original = intent.getStringExtra(Intent.EXTRA_TEXT);
            return original;
        }
        if (intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                original = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            }
            return original;
        }
        if (intent.getStringExtra("original") != null) {
            original = intent.getStringExtra("original");
            return original;
        }
        return null;
    }

}
