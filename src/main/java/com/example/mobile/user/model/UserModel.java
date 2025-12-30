package com.example.mobile.user.model;

import com.example.mobile.roles.Role;

public class UserModel { // model class no setters because unchanged after creation and used for display only 
    private final String id;
    private final String username;
    private final String email;
    private final Role roles;
    private final boolean enabled;
    // Constructors, getters, and setters
    public UserModel(String id, String username, String email, Role roles, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
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
    public Role getRoles() {
        return roles;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public String getDisplayName(){
        return username + " <" + email + ">";
    }
}
