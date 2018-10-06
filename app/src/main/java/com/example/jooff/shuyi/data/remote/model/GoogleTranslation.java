package com.example.jooff.shuyi.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleTranslation {

    @SerializedName("src")
    private String src;
    @SerializedName("confidence")
    private double confidence;
    @SerializedName("ld_result")
    private LdResult ldResult;
    @SerializedName("sentences")
    private List<Sentences> sentences;
    @SerializedName("dict")
    private List<Dict> dict;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public LdResult getLdResult() {
        return ldResult;
    }

    public void setLdResult(LdResult ldResult) {
        this.ldResult = ldResult;
    }

    public List<Sentences> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentences> sentences) {
        this.sentences = sentences;
    }

    public List<Dict> getDict() {
        return dict;
    }

    public void setDict(List<Dict> dict) {
        this.dict = dict;
    }

    public static class LdResult {
        @SerializedName("srclangs")
        private List<String> srclangs;
        @SerializedName("srclangs_confidences")
        private List<Double> srclangsConfidences;
        @SerializedName("extended_srclangs")
        private List<String> extendedSrclangs;

        public List<String> getSrclangs() {
            return srclangs;
        }

        public void setSrclangs(List<String> srclangs) {
            this.srclangs = srclangs;
        }

        public List<Double> getSrclangsConfidences() {
            return srclangsConfidences;
        }

        public void setSrclangsConfidences(List<Double> srclangsConfidences) {
            this.srclangsConfidences = srclangsConfidences;
        }

        public List<String> getExtendedSrclangs() {
            return extendedSrclangs;
        }

        public void setExtendedSrclangs(List<String> extendedSrclangs) {
            this.extendedSrclangs = extendedSrclangs;
        }
    }

    public static class Sentences {
        @SerializedName("trans")
        private String trans;
        @SerializedName("orig")
        private String orig;
        @SerializedName("backend")
        private int backend;
        @SerializedName("translit")
        private String translit;
        @SerializedName("src_translit")
        private String srcTranslit;

        public String getTranslit() {
            return translit;
        }

        public void setTranslit(String translit) {
            this.translit = translit;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public int getBackend() {
            return backend;
        }

        public void setBackend(int backend) {
            this.backend = backend;
        }

        public String getSrcTranslit() {
            return srcTranslit;
        }

        public void setSrcTranslit(String srcTranslit) {
            this.srcTranslit = srcTranslit;
        }
    }

    public static class Dict {
        @SerializedName("pos")
        private String pos;
        @SerializedName("base_form")
        private String baseForm;
        @SerializedName("pos_enum")
        private int posEnum;
        @SerializedName("terms")
        private List<String> terms;
        @SerializedName("entry")
        private List<Entry> entry;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getBaseForm() {
            return baseForm;
        }

        public void setBaseForm(String baseForm) {
            this.baseForm = baseForm;
        }

        public int getPosEnum() {
            return posEnum;
        }

        public void setPosEnum(int posEnum) {
            this.posEnum = posEnum;
        }

        public List<String> getTerms() {
            return terms;
        }

        public void setTerms(List<String> terms) {
            this.terms = terms;
        }

        public List<Entry> getEntry() {
            return entry;
        }

        public void setEntry(List<Entry> entry) {
            this.entry = entry;
        }

        public static class Entry {
            @SerializedName("word")
            private String word;
            @SerializedName("score")
            private double score;
            @SerializedName("reverse_translation")
            private List<String> reverseTranslation;
            @SerializedName("synset_id")
            private List<Integer> synsetId;

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public List<String> getReverseTranslation() {
                return reverseTranslation;
            }

            public void setReverseTranslation(List<String> reverseTranslation) {
                this.reverseTranslation = reverseTranslation;
            }

            public List<Integer> getSynsetId() {
                return synsetId;
            }

            public void setSynsetId(List<Integer> synsetId) {
                this.synsetId = synsetId;
            }
        }
    }
}
