package com.example.jooff.shuyi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jooff on 16/7/29.
 */

public class UTF8Format {

    public static String encode(String original) {
        if (original == null) {
            return null;
        }
        try {
            original = URLEncoder.encode(original, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return original;
    }

}
