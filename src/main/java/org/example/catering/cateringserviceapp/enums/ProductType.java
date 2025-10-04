package org.example.catering.cateringserviceapp.enums;

public enum ProductType {
    SWEET("Slatko"),
    SALTY("Slano");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
