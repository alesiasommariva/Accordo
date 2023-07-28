package com.project.accordo.Entity;

public class Sponsor {

    private String url;
    private String text;
    private String image64;

    public Sponsor(String url, String text, String image64) {
        this.url = url;
        this.text = text;
        this.image64 = image64;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }
}
