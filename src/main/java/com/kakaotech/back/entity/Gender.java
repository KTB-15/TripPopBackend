package com.kakaotech.back.entity;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
