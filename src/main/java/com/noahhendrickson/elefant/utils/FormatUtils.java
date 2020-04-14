package com.noahhendrickson.elefant.utils;

import net.dv8tion.jda.api.entities.User;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class FormatUtils {

    public static String formatUser(User user) {
        return filterEveryone(user.getAsTag());
    }

    public static String formatFullUser(User user) {
        return filterEveryone(user.getAsTag() + " (`" + user.getId() + "`)");
    }

    public static String filterEveryone(String input) {
        if (input == null) return null;
        return input.replace("@everyone", "@\u0435veryone")
                .replace("@here", "@h\u0435re")
                .replace("discord.gg/", "dis\u0441ord.gg/");
    }

}
