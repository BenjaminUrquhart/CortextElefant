package com.noahhendrickson.elefant.infractions;

import java.util.List;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class Infractions {

    private final List<Infraction> infractions;

    public Infractions(List<Infraction> infractions) {
        this.infractions = infractions;
    }

    public List<Infraction> getInfractions() {
        return infractions;
    }
}
