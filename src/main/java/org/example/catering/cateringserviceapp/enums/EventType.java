package org.example.catering.cateringserviceapp.enums;

public enum EventType {
    ALL("Svi dogadjaji"),
    WEDDING("Venčanje"),
    BIRTHDAY("Rodjendan"),
    COMPANY("Korporativni dogadjaji"),
    PRIVATE("Privatne proslave");

    private final String displayName;

    private EventType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {}

}
