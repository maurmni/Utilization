package com.example.utilization;

public class AuthResult {
    private User user;
    private String error;
    private boolean success;

    public AuthResult(User user) {
        this.user = user;
        this.success = true;
    }

    public AuthResult(String error) {
        this.error = error;
        this.success = false;
    }

    public boolean isSuccess() { return success; }
    public User getUser() { return user; }
    public String getError() { return error; }
}

