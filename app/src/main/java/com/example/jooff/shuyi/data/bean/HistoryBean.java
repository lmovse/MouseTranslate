package com.example.jooff.shuyi.data.bean;


import java.io.Serializable;

/**
 * Created by jooff on 16/7/30.
 * 历史记录  bean 包含两个属性值，原文与译文，不存储具体翻译结果
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
