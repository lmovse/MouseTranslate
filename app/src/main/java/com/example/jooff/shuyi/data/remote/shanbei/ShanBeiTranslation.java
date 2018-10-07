package com.example.jooff.shuyi.data.remote.shanbei;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShanBeiTranslation {

    @SerializedName("msg")
    private String msg;

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("data")
    private Data data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("pronunciations")
        private Pronunciations pronunciations;

        @SerializedName("en_definitions")
        private EnDefinitions enDefinitions;

        @SerializedName("audio_addresses")
        private AudioAddresses audioAddresses;

        @SerializedName("uk_audio")
        private String ukAudio;

        @SerializedName("conent_id")
        private int conentId;

        @SerializedName("audio_name")
        private String audioName;

        @SerializedName("cn_definition")
        private CnDefinition cnDefinition;

        @SerializedName("num_sense")
        private int numSense;

        @SerializedName("content_id")
        private int contentId;

        @SerializedName("content_type")
        private String contentType;

        @SerializedName("sense_id")
        private int senseId;

        @SerializedName("id")
        private int id;

        @SerializedName("definition")
        private String definition;

        @SerializedName("has_collins_defn")
        private boolean hasCollinsDefn;

        @SerializedName("has_oxford_defn")
        private boolean hasOxfordDefn;

        @SerializedName("url")
        private String url;

        @SerializedName("has_audio")
        private boolean hasAudio;

        @SerializedName("en_definition")
        private CnDefinition enDefinition;

        @SerializedName("object_id")
        private int objectId;

        @SerializedName("content")
        private String content;

        @SerializedName("pron")
        private String pron;

        @SerializedName("pronunciation")
        private String pronunciation;

        @SerializedName("id_str")
        private String idStr;

        @SerializedName("audio")
        private String audio;

        @SerializedName("us_audio")
        private String usAudio;

        public Pronunciations getPronunciations() {
            return pronunciations;
        }

        public void setPronunciations(Pronunciations pronunciations) {
            this.pronunciations = pronunciations;
        }

        public EnDefinitions getEnDefinitions() {
            return enDefinitions;
        }

        public void setEnDefinitions(EnDefinitions enDefinitions) {
            this.enDefinitions = enDefinitions;
        }

        public AudioAddresses getAudioAddresses() {
            return audioAddresses;
        }

        public void setAudioAddresses(AudioAddresses audioAddresses) {
            this.audioAddresses = audioAddresses;
        }

        public String getUkAudio() {
            return ukAudio;
        }

        public void setUkAudio(String ukAudio) {
            this.ukAudio = ukAudio;
        }

        public int getConentId() {
            return conentId;
        }

        public void setConentId(int conentId) {
            this.conentId = conentId;
        }

        public String getAudioName() {
            return audioName;
        }

        public void setAudioName(String audioName) {
            this.audioName = audioName;
        }

        public CnDefinition getCnDefinition() {
            return cnDefinition;
        }

        public void setCnDefinition(CnDefinition cnDefinition) {
            this.cnDefinition = cnDefinition;
        }

        public int getNumSense() {
            return numSense;
        }

        public void setNumSense(int numSense) {
            this.numSense = numSense;
        }

        public int getContentId() {
            return contentId;
        }

        public void setContentId(int contentId) {
            this.contentId = contentId;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public int getSenseId() {
            return senseId;
        }

        public void setSenseId(int senseId) {
            this.senseId = senseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public boolean isHasCollinsDefn() {
            return hasCollinsDefn;
        }

        public void setHasCollinsDefn(boolean hasCollinsDefn) {
            this.hasCollinsDefn = hasCollinsDefn;
        }

        public boolean isHasOxfordDefn() {
            return hasOxfordDefn;
        }

        public void setHasOxfordDefn(boolean hasOxfordDefn) {
            this.hasOxfordDefn = hasOxfordDefn;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isHasAudio() {
            return hasAudio;
        }

        public void setHasAudio(boolean hasAudio) {
            this.hasAudio = hasAudio;
        }

        public CnDefinition getEnDefinition() {
            return enDefinition;
        }

        public void setEnDefinition(CnDefinition enDefinition) {
            this.enDefinition = enDefinition;
        }

        public int getObjectId() {
            return objectId;
        }

        public void setObjectId(int objectId) {
            this.objectId = objectId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPron() {
            return pron;
        }

        public void setPron(String pron) {
            this.pron = pron;
        }

        public String getPronunciation() {
            return pronunciation;
        }

        public void setPronunciation(String pronunciation) {
            this.pronunciation = pronunciation;
        }

        public String getIdStr() {
            return idStr;
        }

        public void setIdStr(String idStr) {
            this.idStr = idStr;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getUsAudio() {
            return usAudio;
        }

        public void setUsAudio(String usAudio) {
            this.usAudio = usAudio;
        }

        public static class Pronunciations {
            @SerializedName("uk")
            private String uk;
            @SerializedName("us")
            private String us;

            public String getUk() {
                return uk;
            }

            public void setUk(String uk) {
                this.uk = uk;
            }

            public String getUs() {
                return us;
            }

            public void setUs(String us) {
                this.us = us;
            }
        }

        public static class EnDefinitions {
            @SerializedName("n")
            private List<String> n;
            @SerializedName("adj")
            private List<String> adj;
            @SerializedName("v")
            private List<String> v;
            @SerializedName("adv")
            private List<String> adv;
            @SerializedName("prep")
            private List<String> prep;
            @SerializedName("num")
            private List<String> num;
            @SerializedName("pron")
            private List<String> pron;
            @SerializedName("art")
            private List<String> art;
            @SerializedName("conj")
            private List<String> conj;
            @SerializedName("interj")
            private List<String> interj;

            public List<String> getPrep() {
                return prep;
            }

            public void setPrep(List<String> prep) {
                this.prep = prep;
            }

            public List<String> getNum() {
                return num;
            }

            public void setNum(List<String> num) {
                this.num = num;
            }

            public List<String> getPron() {
                return pron;
            }

            public void setPron(List<String> pron) {
                this.pron = pron;
            }

            public List<String> getArt() {
                return art;
            }

            public void setArt(List<String> art) {
                this.art = art;
            }

            public List<String> getConj() {
                return conj;
            }

            public void setConj(List<String> conj) {
                this.conj = conj;
            }

            public List<String> getInterj() {
                return interj;
            }

            public void setInterj(List<String> interj) {
                this.interj = interj;
            }

            public List<String> getAdv() {
                return adv;
            }

            public void setAdv(List<String> adv) {
                this.adv = adv;
            }

            public List<String> getN() {
                return n;
            }

            public void setN(List<String> n) {
                this.n = n;
            }

            public List<String> getAdj() {
                return adj;
            }

            public void setAdj(List<String> adj) {
                this.adj = adj;
            }

            public List<String> getV() {
                return v;
            }

            public void setV(List<String> v) {
                this.v = v;
            }
        }

        public static class AudioAddresses {
            @SerializedName("uk")
            private List<String> uk;
            @SerializedName("us")
            private List<String> us;

            public List<String> getUk() {
                return uk;
            }

            public void setUk(List<String> uk) {
                this.uk = uk;
            }

            public List<String> getUs() {
                return us;
            }

            public void setUs(List<String> us) {
                this.us = us;
            }
        }

        public static class CnDefinition {

            @SerializedName("pos")
            private String pos;

            @SerializedName("defn")
            private String defn;

            public String getPos() {
                return pos;
            }

            public void setPos(String pos) {
                this.pos = pos;
            }

            public String getDefn() {
                return defn;
            }

            public void setDefn(String defn) {
                this.defn = defn;
            }
        }
    }
}
