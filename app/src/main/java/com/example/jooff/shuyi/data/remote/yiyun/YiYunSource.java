package com.example.jooff.shuyi.data.remote.yiyun;

import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.UTF8Format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YiYunSource extends RemoteSource<YiYunTranslation>
        implements AppDataSource.TranslateRemoteSource {

    private static final Pattern LETTER_PATTEN = Pattern.compile("[a-zA-Z]+");

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        request(transUrl, callback, YiYunTranslation.class);
    }

    @Override
    public String getUrl(String original) {
        String originalLan, resultLan;
        Matcher m = LETTER_PATTEN.matcher(original);
        if (m.matches()) {
            originalLan = "en";
            resultLan = "zh";
        } else {
            originalLan = "zh";
            resultLan = "en";
        }
        return YiYunTransApi.YIYUN_URL
                + YiYunTransApi.YIYUN_ORIGINAL + UTF8Format.encode(original.replace("\n", ""))
                + YiYunTransApi.YIYUN_ORIGINAL_LAN + originalLan
                + YiYunTransApi.YIYUN_RESULT_LAN + resultLan
                + YiYunTransApi.YIYUN_ID
                + YiYunTransApi.YIYUN_KEY;
    }

    @Override
    protected Translate parseResult(YiYunTranslation yiYunTranslation,
                                    AppDataSource.TranslateCallback callback) {
        Translate translate = new Translate();
        StringBuilder translationBuilder = new StringBuilder();
        YiYunTranslation.Translation translation = yiYunTranslation.getTranslation().get(0);
        for (YiYunTranslation.Translation.Translated translated : translation.getTranslated()) {
            if (!translated.getText().equals(""))
                translationBuilder.append(translated.getText()).append("\n\n");
        }
        String trans = translationBuilder.substring(0, translationBuilder.length() - 2);
        translate.setTranslation(trans);
        return translate;
    }
}
