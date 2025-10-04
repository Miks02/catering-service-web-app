package org.example.catering.cateringserviceapp.enums;

public enum DeliveryStatus {
    PENDING("Na čekanju"),
    IN_TRANSIT("U tranzitu"),
    DELIVERED("Dostavljeno"),
    CANCELED("Otkazano");

    private final String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
