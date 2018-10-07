package com.example.jooff.shuyi.data.remote.baidu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaiDuTranslation {

    @SerializedName("from")
    private String from;

    @SerializedName("to")
    private String to;

    @SerializedName("trans_result")
    private List<TransResult> transResult;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResult> getTransResult() {
        return transResult;
    }

    public void setTransResult(List<TransResult> transResult) {
        this.transResult = transResult;
    }

    public static class TransResult {

        @SerializedName("src")
        private String src;

        @SerializedName("dst")
        private String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }
}
