package com.example.jooff.shuyi.translate.copy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;

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

public class CopyTransPresenter implements CopyTransContract.Presenter {
    private CopyTransContract.View mView;
    private String original;
    private String source;
    private AppDbRepository mAppDbRepository;

    public CopyTransPresenter(AppDbRepository transSource
            , Intent intent
            , CopyTransContract.View view) {
        mView = view;
        mAppDbRepository = transSource;
        original = getOriginal(intent);
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
                    if (response.getUsSpeech() != null) {
                        source = response.getUsSpeech();
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
    public void playSpeech() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mPlay = new MediaPlayer();
                try {
                    mPlay.setDataSource(source);
                    mPlay.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlay.start();
            }
        }).start();
    }

    private String getOriginal(Intent intent) {
        String original = null;
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
