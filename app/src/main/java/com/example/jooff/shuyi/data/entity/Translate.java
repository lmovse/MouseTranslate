package com.example.jooff.shuyi.data.entity;

import java.util.List;

/**
 * Created by Jooff on 2017/1/20.
 * 定义唯一数据 bean 以便适配多样的翻译源，其他源可以根据需要设置对应的值
 */

public class Translate {
    //    查询对象
    private String query;
    //    基本释义
    private String translation;
    //    美式拼音
    private String usPhonetic;
    //    英式拼音
    private String ukPhonetic;
    //    美式发音
    private String usSpeech;
    //    英式发音
    private String ukSpeech;
    //    英文释义
    private String explainsEn;
    //    释义数组
    private String explains;
    //    原文数组
    private List<String> original;
    //    翻译数组
    private List<String> translate;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUsPhonetic() {
        return usPhonetic;
    }

    public void setUsPhonetic(String usPhonetic) {
        this.usPhonetic = usPhonetic;
    }

    public String getUkSpeech() {
        return ukSpeech;
    }

    public void setUkSpeech(String ukSpeech) {
        this.ukSpeech = ukSpeech;
    }

    public String getUkPhonetic() {
        return ukPhonetic;
    }

    public void setUkPhonetic(String ukPhonetic) {
        this.ukPhonetic = ukPhonetic;
    }

    public String getUsSpeech() {
        return usSpeech;
    }

    public void setUsSpeech(String usSpeech) {
        this.usSpeech = usSpeech;
    }

    public String getExplainsEn() {
        return explainsEn;
    }

    public void setExplainsEn(String explainsEn) {
        this.explainsEn = explainsEn;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public List<String> getOriginal() {
        return original;
    }

    public void setOriginal(List<String> original) {
        this.original = original;
    }

    public List<String> getTranslate() {
        return translate;
    }

    public void setTranslate(List<String> translate) {
        this.translate = translate;
    }

}
