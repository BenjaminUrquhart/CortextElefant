package com.noahhendrickson.elefant.commands;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class MuteCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            if (!bundle.getMessage().getMentionedMembers().isEmpty()) {
                Member member = bundle.getMessage().getMentionedMembers().get(0);
                User user = member.getUser();
                String reason = "No reason. ";

                Role role = bundle.getGuild().getRolesByName("Muted", false).get(0);

                if (role == null) {
                    bundle.sendMessage("Error getting the **Muted** role!");
                    return;
                }

                if (member.getRoles().contains(role)) {
                    bundle.sendMessage("\uD83D\uDEAB " + user.getAsTag() + " is already muted!");
                    return;
                }

                bundle.getGuild().addRoleToMember(member, role).queue();

                if (bundle.areArgsMoreThan(1))
                    reason = bundle.getArgs().subList(1, bundle.getArgs().size()).stream()
                            .map(arg -> arg + " ").collect(Collectors.joining());

                bundle.sendMessage("\uD83D\uDC4C " + user.getAsTag() + " is now muted! (`" +
                        reason.substring(0, reason.length() - 1) + "`)");
            } else bundle.sendMessage("Please mention a user for me to mute!");
        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Mutes a user.";
    }

    @Override
    public String getUsage() {
        return "{user} [reason]";
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
