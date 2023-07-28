package com.project.accordo.Service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.accordo.Entity.Picture;
import com.project.accordo.Entity.PicturePost;

import java.util.List;

@Dao
public interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImagePost(Picture picture);

    @Query("select * from Picture where pid=:pid")
    Picture getPictureByPid(String pid);

    @Query("Delete from picture")
    void deleteAll();

}
