package com.noahhendrickson.elefant.commands.admin;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class JoinRoleCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            Role role = getRole(bundle.getMessage());

            if (role != null) {
                List<Long> roles = Arrays.asList(697197298869993573L);

                if (roles.contains(role.getIdLong())) {
                    bundle.getGuild().addRoleToMember(bundle.getMember(), role).queue(r ->
                                    bundle.addReaction(697249279458279485L),
                            failure -> bundle.addReaction(697249583427747900L));
                } else bundle.addReaction(697249583427747900L);
            } else bundle.addReaction(697249583427747900L);
        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Assigns a role to the calling user.";
    }

    @Override
    public String getUsage() {
        return "{role}";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("add", "give");
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MANAGE_ROLES);
    }

    private Role getRole(Message message) {
        if (message.getMentionedRoles().size() > 0) return message.getMentionedRoles().get(0);

        String role = message.getContentRaw().split(" ")[1];
        List<Role> roles = message.getGuild().getRolesByName(role, true);

        if (roles.size() > 0) return roles.get(0);

        return null;
    }
}
