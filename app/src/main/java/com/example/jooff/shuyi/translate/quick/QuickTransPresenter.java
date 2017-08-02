package com.example.jooff.shuyi.translate.quick;

import android.media.MediaPlayer;

import com.example.jooff.shuyi.api.YouDaoTransAPI;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.util.UTF8Format;

import java.io.IOException;

/**
 * Created by Jooff on 2017/2/1.
 * Tomorrow is a nice day
 */

public class QuickTransPresenter implements QuickTransContract.Presenter {
    private QuickTransContract.View mView;
    private String mUsSpeech;
    private String original;
    private AppDbRepository mAppDbRepository;

    public QuickTransPresenter(AppDbRepository transSource
            , QuickTransContract.View view) {
        mView = view;
        mAppDbRepository = transSource;
    }

    @Override
    public void loadData() {
        String url;
        if (original != null) {
            url = YouDaoTransAPI.YOUDAO_URL
                    + YouDaoTransAPI.YOUDAO_ORIGINAL
                    + UTF8Format.encode(original.replace("\n", ""));
            RemoteJsonSource.getInstance().getTrans(TransSource.FROM_YOUDAO, url,
                    new AppDbSource.TranslateCallback() {
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

}
