package com.example.utilization;

public class AuthResult {

    private final String error;

    public AuthResult(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public String getError() {
        return error;
    }
}
