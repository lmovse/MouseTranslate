package com.example.jooff.shuyi.data.entity;


import java.io.Serializable;

/**
 * Created by jooff on 16/7/30.
 * 历史记录  bean 包含两个属性值，原文与译文以及是否收藏翻译结果，不存储具体翻译结果
 */
public class History implements Serializable {
    private String origin;
    private String result;
    private Integer collected;

    public History(String origin, String result, Integer collected) {
        this.origin = origin;
        this.result = result;
        this.collected = collected;
    }

    public String getOriginal() {
        return origin;
    }

    public String getResult() {
        return result;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    @Override
    public String toString() {
        return "History{" +
                "origin='" + origin + '\'' +
                ", result='" + result + '\'' +
                ", collected=" + collected +
                '}';
    }
}
