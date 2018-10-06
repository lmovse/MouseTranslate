package com.example.jooff.shuyi.util;

import com.example.jooff.shuyi.api.BaiDuTransAPI;
import com.example.jooff.shuyi.api.GoogleTransApi;
import com.example.jooff.shuyi.api.JinShanTransApi;
import com.example.jooff.shuyi.api.ShanBeiTransApi;
import com.example.jooff.shuyi.api.YiYunTransApi;
import com.example.jooff.shuyi.api.YouDaoTransAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.jooff.shuyi.constant.TransSource.FROM_BAIDU;
import static com.example.jooff.shuyi.constant.TransSource.FROM_GOOGLE;
import static com.example.jooff.shuyi.constant.TransSource.FROM_JINSHAN;
import static com.example.jooff.shuyi.constant.TransSource.FROM_SHANBEI;
import static com.example.jooff.shuyi.constant.TransSource.FROM_YIYUN;
import static com.example.jooff.shuyi.constant.TransSource.FROM_YOUDAO;

public class TransUrlParser {

    private static final Pattern LETTER_PATTEN = Pattern.compile("[a-zA-Z]+");

    public static String getTransUrl(final int transFrom, final String original, final String mResultLan) {
        String transUrl = "";
        switch (transFrom) {
            case FROM_YOUDAO:
                transUrl = YouDaoTransAPI.YOUDAO_URL
                        + YouDaoTransAPI.YOUDAO_ORIGINAL + UTF8Format.encode(original).replace("\n", "");
                break;
            case FROM_JINSHAN:
                transUrl = JinShanTransApi.JINSHAN_URL
                        + JinShanTransApi.JINSHAN_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                        + JinShanTransApi.JINSHAN_KEY;
                break;
            case FROM_BAIDU:
                transUrl = BaiDuTransAPI.BAIDU_URL
                        + BaiDuTransAPI.BAIDU_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                        + BaiDuTransAPI.BAIDU_ORIGINAL_LAN
                        + BaiDuTransAPI.BAIDU_RESULT_LAN + mResultLan
                        + BaiDuTransAPI.BAIDU_ID
                        + BaiDuTransAPI.BAIDU_SALT + String.valueOf(1234567899)
                        + BaiDuTransAPI.BAIDU_SIGN + MD5Format.getMd5(BaiDuTransAPI.BAIDU_ID.substring(7) + original + 1234567899 + BaiDuTransAPI.BAIDU_KEY);
                break;
            case FROM_YIYUN:
                String originalLan, resultLan;
                Matcher m = LETTER_PATTEN.matcher(original);
                if (m.matches()) {
                    originalLan = "en";
                    resultLan = "zh";
                } else {
                    originalLan = "zh";
                    resultLan = "en";
                }
                transUrl = YiYunTransApi.YIYUN_URL
                        + YiYunTransApi.YIYUN_ORIGINAL + UTF8Format.encode(original).replace("\n", "")
                        + YiYunTransApi.YIYUN_ORIGINAL_LAN + originalLan
                        + YiYunTransApi.YIYUN_RESULT_LAN + resultLan
                        + YiYunTransApi.YIYUN_ID
                        + YiYunTransApi.YIYUN_KEY;
                break;
            case FROM_SHANBEI:
                transUrl = ShanBeiTransApi.SHANBEI_SEARCH_URL + UTF8Format.encode(original).replace("\n", "");
                break;
            case FROM_GOOGLE:
                transUrl = GoogleTransApi.TRANS_URL
                        + GoogleTransApi.SRC_LANG
                        + GoogleTransApi.TARGET_LANG + mResultLan
                        + GoogleTransApi.TK + TkUtil.getTK(original)
                        + GoogleTransApi.ORIGINAL + UTF8Format.encode(original);
            default:
                break;
        }
        return transUrl;
    }
}
