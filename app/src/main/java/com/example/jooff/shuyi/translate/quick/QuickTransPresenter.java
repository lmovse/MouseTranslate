package com.example.jooff.shuyi.translate.quick;

import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.example.jooff.shuyi.constant.AppPref;
import com.example.jooff.shuyi.constant.SettingDefault;
import com.example.jooff.shuyi.data.AppDataRepository;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;

import java.io.IOException;

import static com.example.jooff.shuyi.constant.SettingDefault.RESULT_LAN;

/**
 * Created by Jooff on 2017/2/1.
 * Tomorrow is a nice day
 */

public class QuickTransPresenter implements QuickTransContract.Presenter {

    private QuickTransContract.View mView;

    private String mUsSpeech;

    private String original;

    private AppDataRepository mAppDbRepository;

    private SharedPreferences sharedPreferences;

    public QuickTransPresenter(SharedPreferences sharedPreferences, AppDataRepository transSource,
                               QuickTransContract.View view) {
        this.sharedPreferences = sharedPreferences;
        mView = view;
        mAppDbRepository = transSource;
    }

    @Override
    public void loadData() {
        if (original != null) {
            int transFrom = sharedPreferences.getInt(AppPref.ARG_FROM,
                    SettingDefault.TRANS_FROM);
            mAppDbRepository.setTransFrom(transFrom);
            mAppDbRepository.setResultLan(sharedPreferences.getString(AppPref.ARG_LAN, RESULT_LAN));
            mAppDbRepository.getTrans(mAppDbRepository.getUrl(original),
                    new AppDataSource.TranslateCallback() {
                        @Override
                        public void onResponse(Translate response) {
                            String original = response.getQuery();
                            if (response.getExplains() != null) {
                                String explain = response.getExplains();
                                mAppDbRepository.saveHistory(new History(original, explain, 0));
                                mView.showTrans(original, explain);
                            } else if (response.getTranslation() != null) {
                                mView.showTrans(original, response.getTranslation());
                            }
                            if (response.getUkPhonetic() != null) {
                                mUsSpeech = response.getUsSpeech();
                                mView.showSpeechAndPhonetic(response.getUsPhonetic());
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
        new Thread(() -> {
            MediaPlayer mPlay = new MediaPlayer();
            try {
                mPlay.setDataSource(mUsSpeech);
                mPlay.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlay.start();
        }).start();
    }

}
