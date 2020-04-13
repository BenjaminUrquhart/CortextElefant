package com.noahhendrickson.elefant.commands.utilities;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class AvatarCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        User user = bundle.getUser();

        if (bundle.hasArgs()) user = bundle.getUserFromMessage();

        if (user != null) {
            bundle.sendMessage(new EmbedBuilder().setImage(user.getEffectiveAvatarUrl()));
        } else bundle.sendMessage("Invalid User: `" + bundle.getMessage().getContentStripped()
                .substring(getCommand().length() + 2) + "`");

    }

    @Override
    public String getCommand() {
        return "avatar";
    }

    @Override
    public String getDescription() {
        return "Displays the given user's avatar.";
    }

    @Override
    public String getUsage() {
        return "{user}";
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MESSAGE_EMBED_LINKS);
    }
}
