package com.example.nasaapplication;

public class apod {
    private String media_type;
    private String title;
    private String url;

    public apod(String media_type, String title, String url) {
        this.media_type = media_type;
        this.title = title;
        this.url = url;
    }

    public apod() {
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
