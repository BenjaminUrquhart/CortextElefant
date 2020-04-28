package com.noahhendrickson.elefant;

import com.noahhendrickson.elefant.listeners.LoggingListener;
import com.noahhendrickson.elefant.listeners.SuggestionsListener;
import com.noahhendrickson.elefant.logging.BaseLogger;
//import com.noahhendrickson.util.FileUtilKt;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by Noah Hendrickson on 4/8/2020
 */
public class Elefant extends ListenerAdapter {

    public static final String PREFIX = "--";

    private final BaseLogger logger;

    public Elefant() {
        this.logger = new BaseLogger();
        try {
            JDABuilder.createDefault(Files.readAllLines(new File("LoginDetails").toPath()).get(0), Arrays.asList(
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_INVITES,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_EMOJIS,
                    GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.DIRECT_MESSAGES))
                    .setActivity(Activity.listening(PREFIX + "help"))
                    .addEventListeners(
                            new CommandExecutor(logger),
                            new LoggingListener(logger),
                            new SuggestionsListener())
                    .build();
        	
        } catch (IOException | LoginException e) {
            e.printStackTrace();
        }
    }

    public BaseLogger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        new Elefant();
    }
}
