package com.project.accordo.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "picture")
public class Picture {
    @PrimaryKey
    @NonNull
    private String pid;
    @ColumnInfo(name = "picture")
    private String picture;

    public Picture(@NonNull String pid, String picture) {
        this.pid = pid;
        this.picture = picture;
    }

    @NonNull
    public String getPid() {
        return pid;
    }

    public void setPid(@NonNull String pid) {
        this.pid = pid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
