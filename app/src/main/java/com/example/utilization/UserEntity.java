package com.example.utilization;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String email;
    public String password;
    public String name;
    public long registrationDate;

    public UserEntity() {}
}
