package com.noahhendrickson.elefant.infractions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class InfractionsManager {

    public static final String FOLDER = "users/";

    private final Gson gson;
    private final User user;

    private final File file;

    public InfractionsManager(User user) {
        this.gson = new GsonBuilder().create();
        this.user = user;

        this.file = new File(FOLDER + user.getId() + ".json");
    }

    public void addInfraction(OffsetDateTime created, InfractionType type, User moderator, String reason) {
        if (doesFileExist()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Infraction> infractions = new ArrayList<>(getAllInfractions());
        infractions.add(new Infraction(getNextId(), created, type, user, moderator, reason));

        String json = gson.toJson(new Infractions(infractions));

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Infraction> getAllInfractions() {
        if (doesFileExist()) {
            try (Reader reader = new FileReader(file)) {

                Infractions infractions = gson.fromJson(reader, Infractions.class);
                if (infractions != null) return infractions.getInfractions();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

    public Infraction getInfraction(long id) {
        return getAllInfractions().stream().filter(infraction -> infraction.getId() == id).findFirst().orElse(null);
    }

    public long getNextId() {
        return getAllInfractions().stream().mapToLong(Infraction::getId).max().orElse(0) + 1;
    }

    private boolean doesFileExist() {
        return file.exists();
    }
}
