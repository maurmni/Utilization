package com.example.utilization;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecyclingPointDao {

    @Insert
    long insert(RecyclingPoint item);

    @Query("SELECT COUNT(*) FROM recyclingPoints")
    int count();

    @Query("SELECT * FROM recyclingPoints")
    LiveData<List<RecyclingPoint>> getAllLive();
}

