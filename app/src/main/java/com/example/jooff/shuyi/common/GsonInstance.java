package com.example.jooff.shuyi.common;

import com.google.gson.Gson;

public class GsonInstance {

    private static Gson gson = new Gson();

    public static Gson newInstance() {
        return gson;
    }
}
