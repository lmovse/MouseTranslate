package com.example.jooff.shuyi.data.remote.youdao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YouDaoTranslation {

    @SerializedName("basic")
    private Basic basic;

    @SerializedName("query")
    private String query;

    @SerializedName("errorCode")
    private int errorCode;

    @SerializedName("translation")
    private List<String> translation;

    @SerializedName("web")
    private List<Web> web;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

    public static class Basic {

        @SerializedName("us-phonetic")
        private String usphonetic;

        @SerializedName("uk-speech")
        private String ukspeech;

        @SerializedName("speech")
        private String speech;

        @SerializedName("phonetic")
        private String phonetic;

        @SerializedName("uk-phonetic")
        private String ukphonetic;

        @SerializedName("us-speech")
        private String usspeech;

        @SerializedName("explains")
        private List<String> explains;

        public String getUsphonetic() {
            return usphonetic;
        }

        public void setUsphonetic(String usphonetic) {
            this.usphonetic = usphonetic;
        }

        public String getUkspeech() {
            return ukspeech;
        }

        public void setUkspeech(String ukspeech) {
            this.ukspeech = ukspeech;
        }

        public String getSpeech() {
            return speech;
        }

        public void setSpeech(String speech) {
            this.speech = speech;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkphonetic() {
            return ukphonetic;
        }

        public void setUkphonetic(String ukphonetic) {
            this.ukphonetic = ukphonetic;
        }

        public String getUsspeech() {
            return usspeech;
        }

        public void setUsspeech(String usspeech) {
            this.usspeech = usspeech;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class Web {

        @SerializedName("key")
        private String key;

        @SerializedName("value")
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

}
