package com.example.jooff.shuyi.data.remote;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jooff.shuyi.common.GsonInstance;
import com.example.jooff.shuyi.common.MyApp;
import com.example.jooff.shuyi.data.AppDataSource;
import com.example.jooff.shuyi.data.entity.Translate;
import com.google.gson.Gson;

import static com.android.volley.VolleyLog.TAG;

public abstract class RemoteSource<T> {

    protected void request(String url, AppDataSource.TranslateCallback callback,
                           Class<T> translationClass) {
        StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
            Gson gson = GsonInstance.newInstance();
            T result = null;
            System.out.println(s);
            try {
                result = gson.fromJson(s, translationClass);
            } catch (Exception e) {
                Log.e(TAG, "request: Gson parse error", e);
            }
            if (result != null) {
                Translate translate = parseResult(result, callback);
                if (translate != null) {
                    callback.onResponse(translate);
                }
            }
        }, volleyError -> {
            Log.e(TAG, "request: request error", volleyError);
            callback.onError(1);
        });
        MyApp.sRequestQueue.add(request);
    }

    protected abstract Translate parseResult(T t, AppDataSource.TranslateCallback callback);

}
