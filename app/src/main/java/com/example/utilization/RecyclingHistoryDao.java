package com.example.utilization;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecyclingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecyclingHistory history);

    @Query("SELECT * FROM RecyclingHistory WHERE userID = :userId")
    LiveData<List<RecyclingHistory>> getAllByUser(int userId);

    @Query("DELETE FROM RecyclingHistory WHERE id = :id")
    void deleteById(int id);
    @Query("SELECT COUNT(*) FROM RecyclingHistory WHERE id = :historyId")
    int exists(int historyId);
}