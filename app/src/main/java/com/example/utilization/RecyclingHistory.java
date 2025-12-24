package com.example.utilization;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class RecyclingHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userID;
    public int recyclingPoint;
    public int wasteGivings;
    public String date;
    public RecyclingHistory() {}

    public RecyclingHistory(int userID, int recyclingPoint, int wasteGivings) {
        this.userID = userID;
        this.recyclingPoint = recyclingPoint;
        this.wasteGivings = wasteGivings;
    }
}