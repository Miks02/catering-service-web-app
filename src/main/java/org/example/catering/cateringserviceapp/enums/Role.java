package org.example.catering.cateringserviceapp.enums;

public enum Role {
    CLIENT("Klijent"),
    MANAGER("Menadzer"),
    ADMIN("Administrator");

    private final String displayName;
    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
