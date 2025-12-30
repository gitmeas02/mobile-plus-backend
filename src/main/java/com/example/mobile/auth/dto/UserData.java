package com.example.mobile.auth.dto;

import com.example.mobile.roles.Role;
import com.example.mobile.roles.Scope;

public class UserData {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private String scope;
    private String status;

    public UserData() {}

    public UserData(String id, String username, String fullName, String email, Role role, String scope, String status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.scope = scope;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}