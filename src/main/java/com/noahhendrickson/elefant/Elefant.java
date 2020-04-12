package com.noahhendrickson.elefant;

import com.noahhendrickson.elefant.listeners.SuggestionsListener;
import com.noahhendrickson.util.FileUtilKt;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

/**
 * Created by Noah Hendrickson on 4/8/2020
 */
public class Elefant {

    public static final String PREFIX = "!";

    public static void main(String[] args) {
        new Elefant();
    }

    public Elefant() {
        try {
            JDABuilder.createDefault(FileUtilKt.readFileLines("LoginDetails").get(0), Arrays.asList(
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_INVITES,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_EMOJIS))
                    .setActivity(Activity.listening("for " + PREFIX + "help"))
                    .addEventListeners(new CommandExecutor(), new SuggestionsListener()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
