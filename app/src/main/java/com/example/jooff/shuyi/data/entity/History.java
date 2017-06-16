package com.example.jooff.shuyi.data.entity;


import java.io.Serializable;

/**
 * Created by jooff on 16/7/30.
 * 历史记录  bean 包含两个属性值，原文与译文，不存储具体翻译结果
 */

public class History implements Serializable{
    private String origin;
    private String result;

    public History(String origin, String result){
        this.origin = origin;
        this.result = result;
    }

    public String getOriginal() {
        return origin;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "History{" +
                "origin='" + origin + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

}
