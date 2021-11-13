package com.natia.secretmod.utils;

import com.natia.secretmod.SecretUtils;

import java.util.List;

public enum Location {
    VILLAGE("Village"),
    PRIVATE_ISLAND("Your Island"),
    THE_CATACOMBS("The Catacombs"),
    VOID_SEPULTURE("Void Sepulture"),
    THE_END("The End"),
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

    public static Location getCurrentLocation() {
        try {
            if (SecretUtils.isValid()) {
                List<String> scoreboard = SecretUtils.getScoreboardLines();

                for (String s : scoreboard) {
                    String sCleaned = SecretUtils.cleanSB(s);
                    for (Location location : Location.values()) {
                        if (sCleaned.contains(location.toString()))
                            return location;
                    }
                }
            }

        } catch (Exception e) { e.printStackTrace(); }
        return Location.NONE;
    }

    @Override
    public String toString() {
        return locationName;
    }
}
