package com.example.jooff.shuyi.translate.main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.data.AppDataRepository;
import com.example.jooff.shuyi.data.AppDataSource.TranslateCallback;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;

import java.io.IOException;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.example.jooff.shuyi.constant.AppPref.ARG_FROM;
import static com.example.jooff.shuyi.constant.AppPref.ARG_LAN;
import static com.example.jooff.shuyi.constant.SettingDefault.RESULT_LAN;
import static com.example.jooff.shuyi.constant.SettingDefault.TRANS_FROM;
import static com.example.jooff.shuyi.constant.TransSource.FROM_COLLECT;
import static com.example.jooff.shuyi.constant.TransSource.FROM_HISTORY;

/**
 * Created by Jooff on 2017/1/18.
 * Tomorrow is a nice day
 */

public class MainTransPresenter implements MainTransContract.Presenter {

    private String mUsSpeech;

    private String mUkSpeech;

    private AppDataRepository mAppDbRepository;

    private String original;

    private int transFrom;

    private MainTransContract.View mView;

    public MainTransPresenter(Bundle bundle, AppDataRepository repository,
                              MainTransContract.View view, SharedPreferences pref) {
        transFrom = bundle.getInt(ARG_FROM);
        original = bundle.getString(AppPref.ARG_ORIGINAL);
        mView = view;
        mAppDbRepository = repository;
        mAppDbRepository.setTransFrom(transFrom);
        mAppDbRepository.setResultLan(pref.getString(ARG_LAN, RESULT_LAN));
        pref.registerOnSharedPreferenceChangeListener(((sharedPreferences, key) -> {
            if (key.equals(ARG_FROM)) {
                mAppDbRepository.setTransFrom(sharedPreferences.getInt(ARG_FROM, TRANS_FROM));
            } else if (key.equals(ARG_LAN)) {
                mAppDbRepository.setResultLan(sharedPreferences.getString(ARG_LAN, RESULT_LAN));
            }
        }));
    }

    @Override
    public void loadData() {
        mAppDbRepository.getTrans(mAppDbRepository.getUrl(original), new TranslateCallback() {
            @Override
            public void onResponse(Translate translate) {
                if (translate == null) {
                    mView.showError();
                    return;
                }
                mView.showCompletedTrans(transFrom, translate.getQuery());
                if (transFrom != FROM_COLLECT && transFrom != FROM_HISTORY) {
                    History history = new History(original, translate.getTranslation(), 0);
                    mAppDbRepository.saveHistory(history);
                }
                if (translate.getTranslation() != null) {
                    mView.showResult(translate.getTranslation());
                }
                if (translate.getExplains() != null
                        || translate.getExplainsEn() != null
                        || translate.getUkPhonetic() != null) {
                    mView.showDict();
                }
                if (translate.getExplains() != null) {
                    mView.showExplain(translate.getExplains());
                }
                if (translate.getExplainsEn() != null) {
                    mView.showExplainEn(translate.getExplainsEn());
                }
                if (translate.getUkPhonetic() != null) {
                    mView.showPhonetic(translate.getUsPhonetic(), translate.getUkPhonetic());
                    mUsSpeech = translate.getUsSpeech();
                    mUkSpeech = translate.getUkSpeech();
                }
                if (translate.getOriginal() != null) {
                    StringBuilder mWeb = new StringBuilder();
                    mWeb.append("\n");
                    for (int i = 0; i < translate.getOriginal().size(); i++) {
                        mWeb.append(translate.getOriginal().get(i))
                                .append("\n")
                                .append(translate.getTranslate().get(i))
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
        new Thread(() -> {
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
        }).start();
    }

    @Override
    public void initTheme() {
        if (!MyApp.sIsNightMode) {
            mView.setAppTheme(MyApp.sColorPrimary);
        }
    }

    @Override
    public void setTextToClip(Context context, String trans) {
        ClipboardManager clipboardManager =
                (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData result = ClipData.newPlainText("result", trans);
        clipboardManager.setPrimaryClip(result);
        mView.showCopySuccess();
    }
}
