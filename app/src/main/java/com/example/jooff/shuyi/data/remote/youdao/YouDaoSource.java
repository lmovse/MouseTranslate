package com.example.jooff.shuyi.data.remote.youdao;

import com.example.jooff.shuyi.data.AppDataSource.TranslateCallback;
import com.example.jooff.shuyi.data.AppDataSource.TranslateRemoteSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.UTF8Format;

import java.util.ArrayList;
import java.util.List;

public class YouDaoSource extends RemoteSource<YouDaoTranslation> implements TranslateRemoteSource {

    @Override
    public void getTrans(String transUrl, TranslateCallback callback) {
        request(transUrl, callback, YouDaoTranslation.class);
    }

    @Override
    public String getUrl(String original) {
        return YouDaoTransAPI.YOUDAO_URL
                + YouDaoTransAPI.YOUDAO_ORIGINAL
                + UTF8Format.encode(original);
    }

    @Override
    protected Translate parseResult(final YouDaoTranslation youDaoTranslation,
                                    final TranslateCallback callback) {
        Translate translate = new Translate();
        if (youDaoTranslation.getErrorCode() != 0) {
            callback.onError(1);
            return null;
        }
        StringBuilder translation = new StringBuilder();
        for (String trans : youDaoTranslation.getTranslation()) {
            translation.append(trans).append("\n");
        }
        translate.setQuery(youDaoTranslation.getQuery());
        translate.setTranslation(translation.substring(0, translation.length() - 1));
        YouDaoTranslation.Basic basic = youDaoTranslation.getBasic();
        if (basic != null) {
            translate.setUsPhonetic(basic.getUsphonetic());
            translate.setUkPhonetic(basic.getUkphonetic());
            translate.setUkSpeech(basic.getUkspeech());
            translate.setUsSpeech(basic.getUsspeech());
            StringBuilder explains = new StringBuilder();
            for (String explain : basic.getExplains()) {
                explains.append(explain).append("\n");
            }
            String explainsString = explains.toString();
            String explain = explainsString.substring(0, explainsString.length() - 1);
            translate.setExplains(explain);
        }
        List<YouDaoTranslation.Web> web = youDaoTranslation.getWeb();
        if (web != null && !web.isEmpty()) {
            List<String> original = new ArrayList<>();
            List<String> translates = new ArrayList<>();
            for (YouDaoTranslation.Web youDaoWeb : web) {
                original.add(youDaoWeb.getKey());
                String valueString = youDaoWeb.getValue().toString();
                translates.add(valueString.substring(1, valueString.length() - 1));
            }
            translate.setOriginal(original);
            translate.setTranslate(translates);
        }
        return translate;
    }
}
