package com.example.pold;

public class Diary {

    private int code;
    private String title;
    private String date;
    private String contents;
    private String uri;
    private int mood;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int Mood) {
        this.mood = mood;
    }
}