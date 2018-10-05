package com.example.jooff.shuyi.data.remote;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.util.EntityFormat;

import org.json.JSONException;

/**
 * Created by Jooff on 2017/2/1.
 * 定义 Json 格式的翻译源的访问与获取格式
 */

public class RemoteJsonSource implements AppDbSource.TranslateDbSource {
    private static RemoteJsonSource instance = null;

    private RemoteJsonSource() {

    }

    public static RemoteJsonSource getInstance() {
        if (instance == null) {
            instance = new RemoteJsonSource();
        }
        return instance;
    }

    @Override
    public void getTrans(final int source, final String original, final AppDbSource.TranslateCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(original, s -> {
            try {
                switch (source) {
                    case TransSource.FROM_BAUDU:
                        EntityFormat.getBeanFromBaidu(s, callback);
                        break;
                    case TransSource.FROM_YIYUN:
                        EntityFormat.getBeanFromYiyun(s, callback);
                        break;
                    case TransSource.FROM_SHANBEI:
                        EntityFormat.getBeanFromShanBei(s, callback);
                        break;
                    case TransSource.FROM_YOUDAO:
                        EntityFormat.getBeanFromYoudao(s, callback);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> callback.onError(1));
        MyApp.sRequestQueue.add(request);
    }

}
