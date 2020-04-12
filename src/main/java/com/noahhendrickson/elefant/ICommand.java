package com.noahhendrickson.elefant;

import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public interface ICommand {

    void execute(CommandBundle bundle);

    String getCommand();

    String getDescription();

    default String getUsage() {
        return "";
    }

    default List<String> getAliases() {
        return Collections.emptyList();
    }

    default List<Permission> getRequiredPermissions() {
        return Collections.emptyList();
    }

    default List<Permission> getRequiredBotPermissions() {
        return Collections.emptyList();
    }

    default boolean isHidden() {
        return false;
    }

}
