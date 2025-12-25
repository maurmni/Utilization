package com.example.utilization;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "RecyclingHistory",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = RecyclingPoint.class,
                        parentColumns = "id",
                        childColumns = "recyclingPoint",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = "userID"),
                @Index(value = "recyclingPoint")
        }
)
public class RecyclingHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userID;
    public Integer recyclingPoint; // Integer для возможности null
    public String date;

    public RecyclingHistory() {}

    public RecyclingHistory(int userID, Integer recyclingPoint) {
        this.userID = userID;
        this.recyclingPoint = recyclingPoint;
    }
}