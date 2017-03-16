package com.example.jooff.shuyi.util;

import android.util.Log;

import com.example.jooff.shuyi.api.ShanBeiTransApi;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.data.bean.TranslateBean;
import com.example.jooff.shuyi.data.remote.RemoteJsonSource;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jooff on 2017/1/31.
 */

public class BeanFormat {
    private static TranslateBean translateBean = new TranslateBean();
    private static List<String> original = new ArrayList<>();
    private static List<String> translate = new ArrayList<>();

    public static void getBeanFromYoudao(final JSONObject json, AppDbSource.TranslateCallback callback) throws JSONException {
        translateBean.setQuery(json.getString("query"));
        translateBean.setTranslation(json.getJSONArray("translation").toString().substring(2, json.getJSONArray("translation").toString().length() - 2));
        if (json.getInt("errorCode") != 0) {
            callback.onError(1);
            return;
        }
        if (!json.isNull("basic")) {
            translateBean.setExplains(json.getJSONObject("basic").getJSONArray("explains").toString()
                    .substring(2, json.getJSONObject("basic").getJSONArray("explains").toString().length() - 2)
                    .replace("\",\"", "\n"));
            if (!json.getJSONObject("basic").isNull("us-phonetic")) {
                translateBean.setUkPhonetic(json.getJSONObject("basic").getString("uk-phonetic"));
                translateBean.setUsPhonetic(json.getJSONObject("basic").getString("us-phonetic"));
                translateBean.setUkSpeech(json.getJSONObject("basic").getString("uk-speech"));
                translateBean.setUsSpeech(json.getJSONObject("basic").getString("us-speech"));
            }
        }
        if (!json.isNull("web")) {
            for (int i = 0; i < json.getJSONArray("web").length(); i++) {
                if (!original.contains(json.getJSONArray("web").getJSONObject(i).getString("key"))) {
                    original.add(json.getJSONArray("web").getJSONObject(i).getString("key"));
                    translate.add(json.getJSONArray("web").getJSONObject(i).getJSONArray("value").toString()
                            .substring(2, json.getJSONArray("web").getJSONObject(i).getJSONArray("value").toString().length() - 2));
                }
            }
            translateBean.setOriginal(original);
            translateBean.setTranslate(translate);
        }
        callback.onResponse(translateBean);
        clearTrans();
    }

    public static void getBeanFromJinShan(String s, AppDbSource.TranslateCallback callback) throws XmlPullParserException, IOException {
        clearTrans();
        StringBuilder explains = new StringBuilder();
        String query = "";
        String usSpeech = "";
        String ukSpeech = "";
        String usPhonetic = "";
        String ukPhonetic = "";
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(s));
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            if (eventType == XmlPullParser.START_TAG) {
                switch (nodeName) {
                    case "key":
                        query = parser.nextText();
                        break;
                    case "pos":
                        explains.append(parser.nextText());
                        break;
                    case "acceptation":
                        explains.append(parser.nextText());
                        break;
                    case "orig":
                        original.add(parser.nextText().replace("\n", ""));
                        break;
                    case "trans":
                        translate.add(parser.nextText().replace("\n", ""));
                        break;
                    case "pron":
                        if (ukSpeech.equals("")) {
                            ukSpeech = parser.nextText();
                        } else {
                            usSpeech = parser.nextText();
                        }
                        break;
                    case "ps":
                        if (ukPhonetic.equals("")) {
                            ukPhonetic = parser.nextText();
                        } else {
                            usPhonetic = parser.nextText();
                        }
                        break;
                    default:
                        break;
                }
            }
            eventType = parser.next();
        }
        String explain = explains.toString();
        if (!explain.equals("")) {
            Log.d("explains", "getBeanFromJinShan: " + explain);
            if (explain.split("\\.").length > 1) {
                explain = explain.substring(0, explain.length() - 1);
                translateBean.setTranslation(explain.split("\\.")[1].split("；")[0].replace(" ", ""));
                translateBean.setExplains(explain);
            }
        }
        if (!ukPhonetic.equals("")) {
            translateBean.setUkPhonetic(ukPhonetic);
        }
        if (original.isEmpty()){
            callback.onError(2);
            clearTrans();
            return;
        }
        translateBean.setQuery(query);
        translateBean.setUsSpeech(usSpeech);
        translateBean.setUkSpeech(ukSpeech);
        translateBean.setUsPhonetic(usPhonetic);
        translateBean.setOriginal(original);
        translateBean.setTranslate(translate);
        callback.onResponse(translateBean);
        clearTrans();
    }

    public static void getBeanFromBaidu(JSONObject json, AppDbSource.TranslateCallback callback) throws JSONException {
        TranslateBean translateBean = new TranslateBean();
        translateBean.setQuery(json.getJSONArray("trans_result").getJSONObject(0).getString("src"));
        translateBean.setTranslation(json.getJSONArray("trans_result").getJSONObject(0).getString("dst"));
        callback.onResponse(translateBean);
    }

    public static void getBeanFromYiyun(JSONObject json, AppDbSource.TranslateCallback callback) throws JSONException {
        TranslateBean translateBean = new TranslateBean();
        translateBean.setQuery(json.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("src-tokenized").replace(" ", ""));
        translateBean.setTranslation(json.getJSONArray("translation").getJSONObject(0).getJSONArray("translated").getJSONObject(0).getString("text"));
        callback.onResponse(translateBean);
    }

    public static void getBeanFromShanBei(JSONObject json, AppDbSource.TranslateCallback callback) throws JSONException {
        String translation;
        List<String> original = new ArrayList<>();
        List<String> translate = new ArrayList<>();
        if (json.get("status_code").equals(1)) {
            callback.onError(2);
        }
        if (json.toString().contains("last")) {
            int i;
            for (i = 0; i < json.getJSONArray("data").length(); i++) {
                original.add(json.getJSONArray("data").getJSONObject(i).getString("first")
                        + json.getJSONArray("data").getJSONObject(i).getString("mid")
                        + json.getJSONArray("data").getJSONObject(i).getString("last"));
                translate.add(json.getJSONArray("data").getJSONObject(i).getString("translation"));
            }
            translateBean.setOriginal(original);
            translateBean.setTranslate(translate);
            callback.onResponse(translateBean);
        }
        if (json.toString().contains("pronunciations")) {
            translateBean.setExplains(json.getJSONObject("data").getString("definition")
                    .replace(" ", "")
                    .replace(".", ". "));
            if (json.getJSONObject("data").getJSONObject("en_definitions").toString().length() > 2) {
                translateBean.setExplainsEn(json.getJSONObject("data").getJSONObject("en_definitions").toString()
                        .substring(1, json.getJSONObject("data").getJSONObject("en_definitions").toString().length() - 2)
                        .replace("\"", "")
                        .replace(":[", ": ")
                        .replace("],", "\n\n"));
            }
            translateBean.setQuery(json.getJSONObject("data").getString("content"));
            if (!json.getJSONObject("data").getJSONObject("pronunciations").getString("uk").equals("")) {
                translateBean.setUkPhonetic(json.getJSONObject("data").getJSONObject("pronunciations").getString("uk"));
                translateBean.setUsPhonetic(json.getJSONObject("data").getJSONObject("pronunciations").getString("us"));
                translateBean.setUkSpeech(json.getJSONObject("data").getString("uk_audio"));
                translateBean.setUsSpeech(json.getJSONObject("data").getString("us_audio"));
            }
            translation = json.getJSONObject("data").getString("definition");
            if (translation.contains(".")) {
                translation = translation.split("\\.")[1]
                        .split(",")[0]
                        .replace(" ", "");
            }
            translateBean.setTranslation(translation);
            if (json.getJSONObject("data").getString("id") != null) {
                String id = ShanBeiTransApi.SHANBEI_EXAMPLE_URL + json.getJSONObject("data").getString("id") + "&type=sys";
                RemoteJsonSource.getInstance().setSource(2).getTrans(id, callback);
            }
        }
    }

    private static void clearTrans() {
        translateBean = new TranslateBean();
        original = new ArrayList<>();
        translate = new ArrayList<>();
    }

}

