package com.noahhendrickson.elefant.commands.infractions;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import com.noahhendrickson.elefant.infractions.Infraction;
import com.noahhendrickson.elefant.infractions.InfractionsManager;
import com.noahhendrickson.elefant.utils.TableGenerator;
import net.dv8tion.jda.api.Permission;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class InfSearchCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            if (bundle.getMessage().getMentionedUsers().size() > 0) {
                InfractionsManager manager = new InfractionsManager(bundle.getMessage().getMentionedUsers().get(0));

                if (manager.getAllInfractions().size() == 0) {
                    bundle.sendMessage("No infractions found!");
                    return;
                }

                List<List<String>> rows = new ArrayList<>();

                for (Infraction infraction : manager.getAllInfractions()) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(infraction.getId()));
                    row.add(OffsetDateTime.parse(infraction.getCreated())
                            .format(DateTimeFormatter.ofPattern("MMMM dd yyyy")));
                    row.add(infraction.getType().getType());
                    row.add(infraction.getUserTag());
                    row.add(infraction.getModeratorTag());
                    row.add(infraction.getReason());
                    rows.add(row);
                }

                String infractions = new TableGenerator().generateTable(Arrays.asList(
                        "ID", "Created", "Type", "User",
                        "Moderator", "Reason"), rows);

                bundle.sendMessage("```\n" + infractions + "\n```");

            } else bundle.sendMessage("Please supply a user!");
        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "inf.search";
    }

    @Override
    public String getDescription() {
        return "Searches infractions for the given user.";
    }

    @Override
    public String getUsage() {
        return "{user}";
    }

    @Override
    public List<Permission> getRequiredPermissions() {
        return Collections.singletonList(Permission.ADMINISTRATOR);
    }
}
