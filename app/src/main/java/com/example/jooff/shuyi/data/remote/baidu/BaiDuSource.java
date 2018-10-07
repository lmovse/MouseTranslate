package com.example.jooff.shuyi.data.remote.baidu;

import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.example.jooff.shuyi.data.remote.RemoteSource;
import com.example.jooff.shuyi.util.MD5Format;
import com.example.jooff.shuyi.util.UTF8Format;

import java.util.List;

public class BaiDuSource extends RemoteSource<BaiDuTranslation>
        implements AppDataSource.TranslateRemoteSource {

    private String resultLan;

    public void setResultLan(String resultLan) {
        this.resultLan = resultLan;
    }

    @Override
    public void getTrans(String transUrl, AppDataSource.TranslateCallback callback) {
        request(transUrl, callback, BaiDuTranslation.class);
    }

    @Override
    public String getUrl(String original) {
        return BaiDuTransAPI.BAIDU_URL
                + BaiDuTransAPI.BAIDU_ORIGINAL + UTF8Format.encode(original)
                + BaiDuTransAPI.BAIDU_ORIGINAL_LAN
                + BaiDuTransAPI.BAIDU_RESULT_LAN + resultLan
                + BaiDuTransAPI.BAIDU_ID
                + BaiDuTransAPI.BAIDU_SALT + String.valueOf(1234567899)
                + BaiDuTransAPI.BAIDU_SIGN + MD5Format.getMd5(BaiDuTransAPI.BAIDU_ID.substring(7) + original + 1234567899 + BaiDuTransAPI.BAIDU_KEY);
    }

    @Override
    protected Translate parseResult(BaiDuTranslation baiDuTranslation,
                                    AppDataSource.TranslateCallback callback) {
        Translate translate = new Translate();
        StringBuilder src = new StringBuilder();
        StringBuilder dest = new StringBuilder();
        List<BaiDuTranslation.TransResult> transResults = baiDuTranslation.getTransResult();
        if (transResults == null || transResults.isEmpty()) {
            callback.onError(1);
            return null;
        }
        for (BaiDuTranslation.TransResult transResult : transResults) {
            src.append(transResult.getSrc()).append("\n");
            dest.append(transResult.getDst()).append("\n");
        }
        translate.setQuery(src.substring(0, src.length() - 1));
        translate.setTranslation(dest.substring(0, dest.length() - 1));
        return translate;
    }
}
