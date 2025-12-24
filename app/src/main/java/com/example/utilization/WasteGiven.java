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
                        entity = RecyclingHistory.class,
                        parentColumns = "id",
                        childColumns = "historyId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = WasteCategory.class,
                        parentColumns = "id",
                        childColumns = "wasteCategory",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("historyId"),
                @Index("userID"),
                @Index("wasteCategory")
        }
)
public class WasteGiven {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int historyId;
    public int userID;
    public float wasteWeight;
    public int wasteCategory;
    public String givingDate;
}
