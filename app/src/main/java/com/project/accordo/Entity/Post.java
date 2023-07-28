package com.project.accordo.Entity;

import android.graphics.Bitmap;

import com.project.accordo.Model.MyModel;

public abstract class Post {
    private String uid;
    private String name;
    private String pversion;
    private String pid;
    private String type;
    private Bitmap profile_pic = null;


    public Post(String uid, String name, String pversion, String pid, String type) {
        this.uid = uid;
        this.name = name;
        this.pversion = pversion;
        this.pid = pid;
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPversion() {
        return pversion;
    }

    public void setPversion(String pversion) {
        this.pversion = pversion;
    }

    public boolean isToUpdateProfilePicture() {
        return MyModel.getInstance().getProfilePic(this.uid).getPicture()==null ||
                !MyModel.getInstance().getProfilePic(this.uid).getPversion().equals(this.pversion);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Bitmap profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "Post{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", pversion='" + pversion + '\'' +
                ", pid='" + pid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
