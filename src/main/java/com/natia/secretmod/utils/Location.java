package com.natia.secretmod.utils;

public enum Location {
    VILLAGE("Village"),
    PRIVATE_ISLAND("Your Island"),
    NONE("None");
    private String locationName;
    private Location(String locationName) {
        this.locationName = locationName;
    }

    public String asName() {
        return locationName;
    }

    public static Location getAsLocation(String locationName) {
        for (Location location : Location.values()) {
            if (location.asName().contains(locationName)) {
                return location;
            }
        }

        return NONE;
    }
}
