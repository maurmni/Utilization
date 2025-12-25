package com.example.utilization;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "wasteGiven",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = RecyclingHistory.class,
                        parentColumns = "id",
                        childColumns = "historyId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "userID"),
                @Index(value = "historyId")
        }
)
public class WasteGiven {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userID;
    public int historyId;
    public float wasteWeight;
    public int wasteCategory;
    public String givingDate;

    public WasteGiven() {}
}