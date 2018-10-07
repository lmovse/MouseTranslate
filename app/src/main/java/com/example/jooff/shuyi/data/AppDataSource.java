package com.example.jooff.shuyi.data;

import com.example.jooff.shuyi.data.entity.Collect;
import com.example.jooff.shuyi.data.entity.History;
import com.example.jooff.shuyi.data.entity.Translate;

import java.util.ArrayList;

/**
 * Created by Jooff on 2017/1/13.
 * 定义一个数据源接口，实现了此数据源接口的类便可以提供数据
 */

public interface AppDataSource {

    interface TranslateCallback {

        void onResponse(Translate response);

        void onError(int errorCode);

    }

    interface HistoryDbSource {

        void saveHistory(History history);

        void collectHistory(History history);

        void cancelCollect(String original);

        History getHistory(String original);

        ArrayList<History> getHistories();

        void deleteHistory(String original);

        void deleteAllHistory();
    }

    interface CollectDbSource {
        void deleteCollect(String original);
        Collect getCollect(String original);
        ArrayList<Collect> getCollects();
    }

    interface TranslateRemoteSource {

       void getTrans(String transUrl, TranslateCallback callback);

       String getUrl(String original);

    }

}
