package com.noahhendrickson.elefant.infractions;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public enum InfractionType {

    WARN("WARN"), MUTE("MUTE");

    private final String type;

    InfractionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
