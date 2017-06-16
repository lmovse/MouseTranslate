package com.example.jooff.shuyi.data.remote;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.data.AppDbSource;
import com.example.jooff.shuyi.util.EntityFormat;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Jooff on 2017/1/25.
 * 定义 XML 源的访问与获取格式
 */

public class RemoteXmlSource implements AppDbSource.TranslateDbSource {
    private static RemoteXmlSource instance = null;

    private RemoteXmlSource() {
    }

    public static RemoteXmlSource getInstance() {
        if (instance == null) {
            instance = new RemoteXmlSource();
        }
        return instance;
    }

    @Override
    public void getTrans(String original, final AppDbSource.TranslateCallback callback) {
        StringRequest stringRequest = new StringRequest(original, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    EntityFormat.getBeanFromJinShan(new String(s.getBytes("ISO-8859-1"), "utf-8"), callback);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(1);
            }
        });
        MyApp.sRequestQueue.add(stringRequest);
    }
}
