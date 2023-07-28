package com.project.accordo.Entity;


public class TextPost extends Post{
    private String content;

    public TextPost(String uid, String name, String pversion, String pid, String type) {
        super(uid, name, pversion, pid, type);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
