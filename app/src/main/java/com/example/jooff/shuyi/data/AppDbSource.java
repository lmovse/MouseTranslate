package com.example.jooff.shuyi.data;

import com.example.jooff.shuyi.data.bean.HistoryBean;
import com.example.jooff.shuyi.data.bean.TranslateBean;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/13.
 */

public interface AppDbSource {

    interface TranslateCallback {

        void onResponse(TranslateBean response);

        void onError(int errorCode);

    }

    interface HistoryDbSource {

        void saveHistoryItem(HistoryBean historyBean);

        HistoryBean getHistoryItem(String original);

        ArrayList<HistoryBean> getHistory();

        void deleteHistoryItem(String original);

    }

    interface TranslateDbSource {

       void getTrans(String original, TranslateCallback callback);

    }

}
