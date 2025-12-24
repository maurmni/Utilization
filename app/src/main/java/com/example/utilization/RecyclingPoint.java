package com.example.utilization;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "recyclingPoints")
public class RecyclingPoint {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String pointName;
    public String address;
    public String phoneNumber;
    public int acceptedCategories;

    public RecyclingPoint() {}

    public RecyclingPoint(String pointName, String address, String phoneNumber, int acceptedCategories) {
        this.pointName = pointName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.acceptedCategories = acceptedCategories;
    }
}
