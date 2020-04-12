package com.noahhendrickson.elefant.commands;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class UnmuteCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            if (!bundle.getMessage().getMentionedMembers().isEmpty()) {
                Member member = bundle.getMessage().getMentionedMembers().get(0);
                User user = member.getUser();
                Role role = bundle.getGuild().getRolesByName("Muted", false).get(0);

                if (role == null) {
                    bundle.sendMessage("Error getting the **Muted** role!");
                    return;
                }

                if (!member.getRoles().contains(role)) {
                    bundle.sendMessage("\uD83D\uDEAB " + user.getAsTag() + " is not muted!");
                    return;
                }

                bundle.getGuild().removeRoleFromMember(member, role).queue();

                bundle.sendMessage("\uD83D\uDC4C " + user.getAsTag() + " is now unmuted!");
            } else bundle.sendMessage("Please mention a user for me to unmute!");
        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "Unmutes a user.";
    }

    @Override
    public String getUsage() {
        return "{user}";
    }

    @Override
    public List<Permission> getRequiredPermissions() {
        return Collections.singletonList(Permission.MESSAGE_MANAGE);
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MANAGE_ROLES);
    }
}
