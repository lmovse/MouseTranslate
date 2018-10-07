package com.example.jooff.shuyi.data.remote.google;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.common.GsonInstance;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.TkUtil;
import com.example.jooff.shuyi.util.UTF8Format;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleSource extends RemoteSource<GoogleTranslation>
        implements AppDataSource.TranslateRemoteSource {

    private String mResultLan;

    public void setResultLan(String mResultLan) {
        this.mResultLan = mResultLan;
    }

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        request(transUrl, callback, GoogleTranslation.class);
    }

    @Override
    protected void request(String url, AppDataSource.TranslateCallback callback,
                           Class<GoogleTranslation> translationClass) {
        StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
            Gson gson = GsonInstance.newInstance();
            GoogleTranslation result = gson.fromJson(s, translationClass);
            if (result != null) {
                callback.onResponse(parseResult(result, callback));
            }
        }, volleyError -> callback.onError(1)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> heads = new HashMap<>();
                heads.put("User-Agent", GoogleTransApi.DEFAULT_USER_AGENT);
                return heads;
            }
        };
        MyApp.sRequestQueue.add(request);
    }

    @Override
    public String getUrl(String original) {
        return GoogleTransApi.TRANS_URL
                + GoogleTransApi.SRC_LANG
                + GoogleTransApi.TARGET_LANG + mResultLan
                + GoogleTransApi.TK + TkUtil.getTK(original)
                + GoogleTransApi.ORIGINAL + UTF8Format.encode(original);
    }

    @Override
    protected Translate parseResult(GoogleTranslation googleTranslation,
                                    AppDataSource.TranslateCallback callback) {
        if (googleTranslation == null) {
            callback.onError(1);
            return null;
        }
        List<GoogleTranslation.Sentences> sentences = googleTranslation.getSentences();
        if (sentences == null || sentences.isEmpty()) {
            callback.onError(1);
            return null;
        }
        StringBuilder orig = new StringBuilder();
        StringBuilder translation = new StringBuilder();
        for (GoogleTranslation.Sentences sentence : sentences) {
            if (sentence.getTrans() != null) {
                orig.append(sentence.getOrig());
                translation.append(sentence.getTrans());
            }
        }
        Translate translate = new Translate();
        translate.setQuery(orig.toString());
        translate.setTranslation(translation.toString());
        return translate;
    }
}
