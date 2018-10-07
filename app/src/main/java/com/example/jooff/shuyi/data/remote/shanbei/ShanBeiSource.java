package com.example.jooff.shuyi.data.remote.shanbei;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.common.GsonInstance;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.UTF8Format;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ShanBeiSource extends RemoteSource<ShanBeiTranslation>
        implements AppDataSource.TranslateRemoteSource {

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        request(transUrl, callback, ShanBeiTranslation.class);
    }

    @Override
    public String getUrl(String original) {
        return ShanBeiTransApi.SHANBEI_SEARCH_URL + UTF8Format.encode(original);
    }

    @Override
    protected Translate parseResult(ShanBeiTranslation translation,
                                    AppDataSource.TranslateCallback callback) {
        if (translation.getStatusCode() == 1) {
            callback.onError(2);
            return null;
        }
        Translate translate = new Translate();
        ShanBeiTranslation.Data data = translation.getData();
        translate.setQuery(data.getContent());
        translate.setExplains(data.getCnDefinition().getDefn().trim());
        populateEnDefinitions(translate, data);
        ShanBeiTranslation.Data.Pronunciations pronunciations = data.getPronunciations();
        if (pronunciations != null) {
            translate.setUkPhonetic(pronunciations.getUk());
            translate.setUsPhonetic(pronunciations.getUs());
        }
        translate.setUkSpeech(data.getUkAudio());
        translate.setUsSpeech(data.getUsAudio());
        int id = translation.getData().getId();
        if (id != 0) {
            String exampleUrl = ShanBeiTransApi.SHANBEI_EXAMPLE_URL + id + ShanBeiTransApi.TYPE;
            StringRequest request = new StringRequest(Request.Method.GET, exampleUrl, s -> {
                Gson gson = GsonInstance.newInstance();
                ShanBeiExample shanBeiExample = gson.fromJson(s, ShanBeiExample.class);
                List<String> originals = new ArrayList<>();
                List<String> translates = new ArrayList<>();
                for (ShanBeiExample.Data exampleData : shanBeiExample.getData()) {
                    String original = exampleData.getAnnotation()
                            .replace("<vocab>", "")
                            .replace("</vocab>", "");
                    originals.add(original);
                    translates.add(exampleData.getTranslation());
                }
                translate.setOriginal(originals);
                translate.setTranslate(translates);
                callback.onResponse(translate);
            }, error -> Log.d("ShanBeiSource", "parseResult: get example error", error));
            MyApp.sRequestQueue.add(request);
        }
        return translate;
    }

    private void populateEnDefinitions(Translate translate, ShanBeiTranslation.Data data) {
        StringBuilder enDefinition = new StringBuilder();
        ShanBeiTranslation.Data.EnDefinitions enDefinitions = data.getEnDefinitions();
        if (enDefinitions.getAdj() != null) {
            enDefinition.append("adj. ").append(ary2Str(enDefinitions.getAdj())).append("\n");
        }
        if (enDefinitions.getN() != null) {
            enDefinition.append("n. ").append(ary2Str(enDefinitions.getN())).append("\n");
        }
        if (enDefinitions.getV() != null) {
            enDefinition.append("v. ").append(ary2Str(enDefinitions.getV())).append("\n");
        }
        if (enDefinitions.getAdv() != null) {
            enDefinition.append("adv. ").append(ary2Str(enDefinitions.getAdv())).append("\n");
        }
        if (enDefinitions.getInterj() != null) {
            enDefinition.append("interj. ").append(ary2Str(enDefinitions.getInterj())).append("\n");
        }
        if (enDefinitions.getNum() != null) {
            enDefinition.append("num. ").append(ary2Str(enDefinitions.getNum())).append("\n");
        }
        if (enDefinitions.getArt() != null) {
            enDefinition.append("art. ").append(ary2Str(enDefinitions.getArt())).append("\n");
        }
        if (enDefinitions.getPrep() != null) {
            enDefinition.append("prep. ").append(ary2Str(enDefinitions.getPrep())).append("\n");
        }
        if (enDefinitions.getPron() != null) {
            enDefinition.append("pron. ").append(ary2Str(enDefinitions.getPron())).append("\n");
        }
        if (enDefinitions.getConj() != null) {
            enDefinition.append("conj. ").append(ary2Str(enDefinitions.getConj())).append("\n");
        }
        translate.setExplainsEn(enDefinition.substring(0, enDefinition.length() - 1));
    }

    private String ary2Str(final List<String> ary) {
        StringBuilder strBuilder = new StringBuilder();
        for (String s : ary) {
            strBuilder.append(s).append(";");
        }
        return strBuilder.substring(0, strBuilder.length() - 1);
    }
}
