package com.example.utilization;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDAO {

    @Insert
    long insert(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity findByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    UserEntity getById(int id);

    @Query("UPDATE users SET password = :password WHERE email = :email")
    int resetPassword(String email, String password);
    
    @Update
    void update(UserEntity user);
    @Query("SELECT COUNT(*) FROM users")
    int countUsers();
}
