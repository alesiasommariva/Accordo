package com.project.accordo.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_picture")
public class ProfilePicture {
    @NonNull
    @PrimaryKey
    private String uid;
    @ColumnInfo(name = "picture")
    @Nullable
    private String picture;
    @ColumnInfo(name = "pversion")
    private String pversion;

    public ProfilePicture(@NonNull String uid, String picture, String pversion) {
        this.uid = uid;
        this.picture = picture;
        this.pversion = pversion;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPversion() {
        return pversion;
    }

    public void setPversion(String pversion) {
        this.pversion = pversion;
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
                "uid='" + uid + '\'' +
                ", picture='" + picture + '\'' +
                ", pversion='" + pversion + '\'' +
                '}';
    }
}
