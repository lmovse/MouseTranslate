package com.example.jooff.shuyi.translate.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.jooff.shuyi.common.Constant;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.bean.HistoryBean;
import com.example.jooff.shuyi.data.bean.TranslateBean;

import java.io.IOException;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Jooff on 2017/1/18.
 */

public class MainTranslatePresenter implements MainTranslateContract.Presenter {
    private String mOriginal;
    private String mUsSpeech;
    private String mUkSpeech;
    private int transFrom;
    private AppDbRepository mAppDbRepository;
    private MainTranslateContract.View mView;

    public MainTranslatePresenter(Bundle bundle
            , AppDbRepository transSource
            , MainTranslateContract.View view) {
        transFrom = bundle.getInt(Constant.ARG_FROM);
        mOriginal = bundle.getString(Constant.ARG_ORIGINAL);
        mAppDbRepository = transSource;
        mView = view;
    }

    @Override
    public void loadData() {
        mAppDbRepository.setTranFrom(transFrom).getTrans(mOriginal, new AppDbSource.TranslateCallback() {
            @Override
            public void onResponse(TranslateBean response) {
                if (response == null) {
                    mView.showError();
                }
                assert response != null;
                mView.showCompletedTrans(response.getQuery());
                if (transFrom != 0) {
                    HistoryBean historyBean = new HistoryBean(response.getQuery(), response.getTranslation());
                    mAppDbRepository.saveHistoryItem(historyBean);
                }
                if (response.getTranslation() != null) {
                    mView.showResult(response.getTranslation());
                }
                if (response.getExplains() != null) {
                    mView.showExplain(response.getExplains());
                }
                if (response.getExplainsEn() != null) {
                    mView.showExplainEn(response.getExplainsEn());
                }
                if (response.getUkPhonetic() != null) {
                    mView.showPhonetic(response.getUsPhonetic(), response.getUkPhonetic());
                    mUsSpeech = response.getUsSpeech();
                    mUkSpeech = response.getUkSpeech();
                }
                if (response.getOriginal() != null) {
                    StringBuilder mWeb = new StringBuilder();
                    mWeb.append("\n");
                    for (int i = 0; i < response.getOriginal().size(); i++) {
                        mWeb.append(String.valueOf(i + 1))
                                .append("  ")
                                .append(response.getOriginal().get(i))
                                .append("\n    ")
                                .append(response.getTranslate().get(i))
                                .append("\n")
                                .append("\n");
                    }
                    mView.showWeb(mWeb.toString());
                }
            }

            @Override
            public void onError(int errorCode) {
                switch (errorCode) {
                    case 0:
                        break;
                    case 1:
                        mView.showError();
                        break;
                    case 2:
                        mView.showNotSupport();
                        break;
                }
            }
        });
    }

    @Override
    public void playSpeech(int speechFrom) {
        final int from = speechFrom;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mPlay = new MediaPlayer();
                try {
                    if (from == 0) {
                        mPlay.setDataSource(mUsSpeech);
                    } else {
                        mPlay.setDataSource(mUkSpeech);
                    }
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
        if (!Constant.sIsNightMode) {
            mView.setAppTheme(Constant.sColorPrimary);
        }
    }

    @Override
    public void setTextToClip(Context context, String trans) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData result = ClipData.newPlainText("result", trans);
        clipboardManager.setPrimaryClip(result);
        mView.showCopySuccess();
    }

}
