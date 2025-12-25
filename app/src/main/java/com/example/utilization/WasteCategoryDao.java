package com.example.utilization;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface WasteCategoryDao {

    @Insert
    long insert(WasteCategory category);
    @Query("SELECT COUNT(*) FROM wasteCategories")
    int count();

    @Query("SELECT * FROM wasteCategories")
    LiveData<List<WasteCategory>> getAllLive();
}
