package com.example.mobile.auth.dto;

public class AuthSuccessData {
    private UserData user;
    private AuthData auth;

    public AuthSuccessData() {}

    public AuthSuccessData(UserData user, AuthData auth) {
        this.user = user;
        this.auth = auth;
    }

    // Getters and Setters
    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }

    public AuthData getAuth() { return auth; }
    public void setAuth(AuthData auth) { this.auth = auth; }
}