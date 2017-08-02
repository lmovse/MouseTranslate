package com.example.jooff.shuyi.translate.dialog;

import android.content.Intent;
import android.os.Build;

import com.example.jooff.shuyi.api.YouDaoTransAPI;
import com.example.jooff.shuyi.data.AppDbRepository;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;
import com.example.jooff.shuyi.util.UTF8Format;

/**
 * Created by Jooff on 2017/2/1.
 * Tomorrow is a nice day
 */

public class QuickTransPresenter implements QuickTransContract.Presenter {
    private QuickTransContract.View mView;
    private String original;
    private AppDbRepository mAppDbRepository;

    public QuickTransPresenter(AppDbRepository transSource
            , Intent intent
            , QuickTransContract.View view) {
        mView = view;
        mAppDbRepository = transSource;
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
                public void onResponse(Translate response) {
                    String original = response.getQuery();
                    if (response.getExplains() != null) {
                        String explain = response.getExplains();
                        mAppDbRepository.saveHistory(new History(original, explain, 0));
                        mView.showTrans(original, explain);
                    } else if (response.getTranslation() != null) {
                        mView.showTrans(original, response.getTranslation());
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
