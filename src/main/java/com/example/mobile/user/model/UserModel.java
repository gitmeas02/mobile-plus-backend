package com.example.mobile.user.model;

public class UserModel { // model class no setters because unchanged after creation and used for display only 
    private String id;
    private String username;
    private String email;
    // Constructors, getters, and setters
    public UserModel(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getDisplayName(){
        return username + " <" + email + ">";
    }
}
