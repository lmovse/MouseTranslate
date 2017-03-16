package com.example.jooff.shuyi.data.bean;


import java.io.Serializable;

/**
 * Created by jooff on 16/7/30.
 */

public class HistoryBean implements Serializable{
    private String textOriginal;
    private String textResult;

    public HistoryBean(String textOriginal, String textResult){
        this.textOriginal = textOriginal;
        this.textResult = textResult;
    }

    public String getTextOriginal() {
        return textOriginal;
    }

    public String getTextResult() {
        return textResult;
    }

}
