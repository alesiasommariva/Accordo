package com.project.accordo.Service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.accordo.Entity.ProfilePicture;

import java.util.List;

@Dao
public interface ProfilePictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfilePic(ProfilePicture profilePicture);

    @Query("SELECT * FROM profile_picture WHERE uid=:uid")
    ProfilePicture getProfilePictureByUid(String uid);

    @Query("SELECT * FROM profile_picture")
    List<ProfilePicture> getAllProfilePictures();

    @Query("DELETE FROM profile_picture")
    void deleteAllProfilePictures();
}
