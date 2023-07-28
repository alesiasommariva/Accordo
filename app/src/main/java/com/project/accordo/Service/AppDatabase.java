package com.project.accordo.Service;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.accordo.Entity.Picture;
import com.project.accordo.Entity.ProfilePicture;

@Database(entities = {ProfilePicture.class, Picture.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    public abstract ProfilePictureDao profilePictureDao();
    public abstract PictureDao pictureDao();

    public static AppDatabase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (AppDatabase.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "accordoDB").build();
                }
            }
        }
        return INSTANCE;
    }
}
