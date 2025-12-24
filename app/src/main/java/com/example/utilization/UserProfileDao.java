package com.example.utilization;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserProfileDao {

    @Insert
    void insert(UserProfile user);

    @Query("SELECT * FROM userProfiles")
    List<UserProfile> getAll();
}
