package com.example.jooff.shuyi.data.remote.shanbei;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShanBeiExample {
    @SerializedName("msg")
    private String msg;
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("data")
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("username")
        private String username;
        @SerializedName("vocabulary_id")
        private int vocabularyId;
        @SerializedName("word")
        private String word;
        @SerializedName("user")
        private User user;
        @SerializedName("userid")
        private int userid;
        @SerializedName("mid")
        private String mid;
        @SerializedName("audio_name")
        private String audioName;
        @SerializedName("id")
        private int id;
        @SerializedName("audio_addresses")
        private AudioAddresses audioAddresses;
        @SerializedName("version")
        private int version;
        @SerializedName("unlikes")
        private int unlikes;
        @SerializedName("likes")
        private int likes;
        @SerializedName("last")
        private String last;
        @SerializedName("translation")
        private String translation;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("annotation")
        private String annotation;
        @SerializedName("first")
        private String first;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getVocabularyId() {
            return vocabularyId;
        }

        public void setVocabularyId(int vocabularyId) {
            this.vocabularyId = vocabularyId;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getAudioName() {
            return audioName;
        }

        public void setAudioName(String audioName) {
            this.audioName = audioName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AudioAddresses getAudioAddresses() {
            return audioAddresses;
        }

        public void setAudioAddresses(AudioAddresses audioAddresses) {
            this.audioAddresses = audioAddresses;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getUnlikes() {
            return unlikes;
        }

        public void setUnlikes(int unlikes) {
            this.unlikes = unlikes;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public static class User {
        }

        public static class AudioAddresses {
        }
    }
}
