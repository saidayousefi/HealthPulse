package com.example.healthpulse.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.healthpulse.data.local.model.Profile;
import java.util.List;

@Dao
public interface ProfileDao {
    @Insert
    void insert(Profile profile);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("SELECT * FROM profiles WHERE email = :email LIMIT 1")
    Profile getProfileByEmail(String email);

    @Query("SELECT * FROM profiles")
    LiveData<List<Profile>> getAllProfiles();

    @Query("SELECT * FROM profiles LIMIT 1")
    Profile getCurrentProfile();  // Add this method
}
