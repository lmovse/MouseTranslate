package com.example.jooff.shuyi.data.entity;


import java.io.Serializable;

/**
 * Created by jooff on 16/7/30.
 * 收藏  bean 包含两个属性值，原文与译文
 */
public class Collect implements Serializable {
    private String origin;
    private String result;

    public Collect(String origin, String result) {
        this.origin = origin;
        this.result = result;
    }

    public String getOriginal() {
        return origin;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "origin='" + origin + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
