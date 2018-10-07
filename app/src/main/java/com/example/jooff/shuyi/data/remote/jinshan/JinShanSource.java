package com.example.jooff.shuyi.data.remote.jinshan;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.AppDataSource.TranslateRemoteSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.UTF8Format;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class JinShanSource extends RemoteSource<String> implements TranslateRemoteSource {

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        request(transUrl, callback, String.class);
    }

    @Override
    public String getUrl(String original) {
        return JinShanTransApi.JINSHAN_URL
                + JinShanTransApi.JINSHAN_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                + JinShanTransApi.JINSHAN_KEY;
    }

    @Override
    protected void request(String url, AppDataSource.TranslateCallback callback, Class<String> translationClass) {
        StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
            String translation = new String(s.getBytes(ISO_8859_1), UTF_8);
            Translate translate = parseResult(translation, callback);
            if (translate != null)
                callback.onResponse(translate);
        }, volleyError -> callback.onError(1));
        MyApp.sRequestQueue.add(request);
    }

    @Override
    protected Translate parseResult(String jinShanTranslation,
                                    AppDataSource.TranslateCallback callback) {
        Translate translate = new Translate();
        List<String> original = new ArrayList<>();
        List<String> translates = new ArrayList<>();
        StringBuilder explains = new StringBuilder();
        String query = "";
        String usSpeech = "";
        String ukSpeech = "";
        String usPhonetic = "";
        String ukPhonetic = "";
        XmlPullParser parser;
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(jinShanTranslation));
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
                            translates.add(parser.nextText().replace("\n", ""));
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
                if (explain.split("\\.").length > 1) {
                    explain = explain.substring(0, explain.length() - 1);
                    translate.setExplains(explain);
                }
            }
            if (!ukPhonetic.equals("")) {
                translate.setUkPhonetic(ukPhonetic);
            }
            if (original.isEmpty()) {
                callback.onError(2);
                return null;
            }
        } catch (Exception e) {
            Log.e("JinShanSource", "parseResult: JinShan Xml parse error", e);
            return null;
        }
        translate.setQuery(query);
        translate.setUsSpeech(usSpeech);
        translate.setUkSpeech(ukSpeech);
        translate.setUsPhonetic(usPhonetic);
        translate.setOriginal(original);
        translate.setTranslate(translates);
        return translate;
    }
}
