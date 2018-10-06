package com.example.jooff.shuyi.data.remote;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.api.GoogleTransApi;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.constant.TransSource;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.util.EntityFormat;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

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
    public void getTrans(final int source, final String transUrl,
                         final AppDbSource.TranslateCallback callback) {
        Request request;
        if (source == TransSource.FROM_GOOGLE) {
            request = new StringRequest(transUrl, s -> EntityFormat.getBeanFromGoogle(s, callback),
                    volleyError -> callback.onError(1)) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("User-Agent", GoogleTransApi.DEFAULT_USER_AGENT);
                    return params;
                }
            };
        } else {
            request = new JsonObjectRequest(transUrl, s -> {
                try {
                    switch (source) {
                        case TransSource.FROM_BAIDU:
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
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, volleyError -> callback.onError(1));
        }
        MyApp.sRequestQueue.add(request);
    }

}
