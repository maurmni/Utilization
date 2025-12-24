package com.example.utilization;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WasteGivenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(WasteGiven wasteGiven);

    @Update
    void update(WasteGiven wasteGiven);

    @Delete
    void delete(WasteGiven wasteGiven);

    @Query("SELECT * FROM wasteGiven WHERE id = :givenId")
    LiveData<WasteGiven> getByIdLive(int givenId);

    @Query("SELECT * FROM wasteGiven WHERE wasteCategory = :userId")
    List<WasteGiven> getAllForCategory(int userId);

    @Query("SELECT * FROM wasteGiven")
    LiveData<List<WasteGiven>> getAllLive();

    @Query("SELECT COUNT(*) FROM wasteGiven WHERE historyId = :historyId")
    int countByHistory(int historyId);

    @Query("SELECT COUNT(*) FROM wasteGiven WHERE historyId = :historyId AND userID = :userId")
    int countByHistoryAndUser(
            int historyId,
            int userId
    );
    @Query("SELECT * FROM wasteGiven WHERE historyId = :historyId AND userID = :userId")
    LiveData<List<WasteGiven>> getByHistoryAndUser( int historyId, int userId );
    @Query("SELECT * FROM wasteGiven WHERE historyId = :historyId")
    LiveData<List<WasteGiven>> getByHistory(int historyId);
}