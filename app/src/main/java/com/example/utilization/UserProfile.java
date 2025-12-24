package com.example.utilization;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userProfiles")
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userName;
    public String registrationDate;
}
