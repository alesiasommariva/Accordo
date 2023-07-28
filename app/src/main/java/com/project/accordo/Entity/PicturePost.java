package com.project.accordo.Entity;


import android.graphics.Bitmap;

public class PicturePost extends Post{
    private Bitmap picture;

    public PicturePost(String uid, String name, String pversion, String pid, String type) {
        super(uid, name, pversion, pid, type);
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
