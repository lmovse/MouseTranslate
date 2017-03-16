package com.example.jooff.shuyi.data.remote;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.util.BeanFormat;
import com.example.jooff.shuyi.common.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jooff on 2017/2/1.
 */

public class RemoteJsonSource implements AppDbSource.TranslateDbSource {
    private int source;
    private static RemoteJsonSource instance = null;

    private RemoteJsonSource() {

    }

    public  RemoteJsonSource setSource(int source){
        this.source = source;
        return this;
    }

    public static RemoteJsonSource getInstance() {
        if (instance == null) {
            instance = new RemoteJsonSource();
        }
        return instance;
    }

    @Override
    public void getTrans(final String original, final AppDbSource.TranslateCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(original, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject s) {
                try {
                    switch (source) {
                        case 0:
                            BeanFormat.getBeanFromBaidu(s, callback);
                            break;
                        case 1:
                            BeanFormat.getBeanFromYiyun(s, callback);
                            break;
                        case 2:
                            BeanFormat.getBeanFromShanBei(s, callback);
                            break;
                        case 3:
                            BeanFormat.getBeanFromYoudao(s, callback);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(1);
            }
        });
        MyApp.sRequestQueue.add(request);
    }

}
