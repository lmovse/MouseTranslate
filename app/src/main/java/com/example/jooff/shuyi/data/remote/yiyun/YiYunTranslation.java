package com.example.jooff.shuyi.data.remote.yiyun;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YiYunTranslation {

    @SerializedName("translation")
    private List<Translation> translation;

    public List<Translation> getTranslation() {
        return translation;
    }

    public void setTranslation(List<Translation> translation) {
        this.translation = translation;
    }

    public static class Translation {

        @SerializedName("translationId")
        private String translationId;

        @SerializedName("translated")
        private List<Translated> translated;

        public String getTranslationId() {
            return translationId;
        }

        public void setTranslationId(String translationId) {
            this.translationId = translationId;
        }

        public List<Translated> getTranslated() {
            return translated;
        }

        public void setTranslated(List<Translated> translated) {
            this.translated = translated;
        }

        public static class Translated {

            @SerializedName("score")
            private double score;

            @SerializedName("rank")
            private int rank;

            @SerializedName("text")
            private String text;

            @SerializedName("src-tokenized")
            private List<String> srctokenized;

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public List<String> getSrctokenized() {
                return srctokenized;
            }

            public void setSrctokenized(List<String> srctokenized) {
                this.srctokenized = srctokenized;
            }
        }
    }
}
