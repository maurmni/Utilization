package com.example.utilization;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wasteCategories")
public class WasteCategory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String wasteName;
    public String description;
    public int pointsPerKg;

    public WasteCategory() {}
    public WasteCategory(String wasteName, String description, int pointsPerKg) {
        this.wasteName = wasteName;
        this.description = description;
        this.pointsPerKg = pointsPerKg;
    }
}
