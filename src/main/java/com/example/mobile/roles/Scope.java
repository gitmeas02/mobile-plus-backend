package com.example.mobile.roles;

public enum Scope {
    READ("read"),
    WRITE("write"),
    READ_WRITE("read write");

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}