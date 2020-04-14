package com.noahhendrickson.elefant.commands.punishments;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import com.noahhendrickson.elefant.infractions.InfractionType;
import com.noahhendrickson.elefant.infractions.InfractionsManager;
import com.noahhendrickson.elefant.utils.FormatUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class WarnCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            User user = null;
            String reason = "No Reason";

            if (bundle.hasArgs()) user = bundle.getUserFromMessage();
            if (user == null) {
                bundle.sendMessage("Invalid User: `" + bundle.getArgAt(0) + "`");
                return;
            }

            if (bundle.areArgsMoreThan(1))
                reason = bundle.getArgs().subList(1, bundle.getArgs().size()).stream()
                        .map(arg -> arg + " ").collect(Collectors.joining());

            final String message = "⚠️ You have been warned by " + FormatUtils.formatUser(bundle.getUser());
            final User finalUser = user;
            final String finalReason = reason;

            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage(message + " in the " +
                        bundle.getGuild().getName() + " server for `" + finalReason + "`").queue(success -> {
                }, failure -> bundle.sendMessage("⚠️ Cannot send  a private message to " +
                        FormatUtils.formatUser(finalUser) + "."));

                bundle.sendMessage(":ok_hand: I have warned " + FormatUtils.formatUser(finalUser) +
                        " for `" + finalReason + "`");
            });

            new InfractionsManager(finalUser).addInfraction(OffsetDateTime.now(), InfractionType.WARN,
                    bundle.getUser(), reason);

        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Adds a warning infraction to a user.";
    }

    @Override
    public String getUsage() {
        return "{user} [reason]";
    }

    @Override
    public List<Permission> getRequiredPermissions() {
        return Arrays.asList(Permission.MESSAGE_MANAGE, Permission.MANAGE_CHANNEL);
    }
}
