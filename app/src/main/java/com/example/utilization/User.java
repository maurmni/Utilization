package com.example.utilization;
import java.util.Date;

public class User {
    private String userId;
    private String userName;
    private String email;
    private Date registrationDate;
    private int totalPoints;

    public User() {}

    public User(String userId, String userName, String email, Date registrationDate, int totalPoints) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.totalPoints = totalPoints;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
}

