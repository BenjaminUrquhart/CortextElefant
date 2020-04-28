package com.noahhendrickson.elefant.commands;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.CommandExecutor;
import com.noahhendrickson.elefant.Elefant;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class HelpCommand implements ICommand {

    private final CommandExecutor executor;

    public HelpCommand(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
        	String cmd = bundle.getArgAt(0);
            
        	if (cmd.startsWith(Elefant.PREFIX)) {
        		cmd = cmd.substring(Elefant.PREFIX.length());
        	}

            ICommand command = executor.getCommand(cmd);
            
            if (command == null) {
            	bundle.sendMessage("I can't find the `" + cmd + "` command right now!");
            	return;
            }
            		
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(bundle.getGuild().getSelfMember().getColor())
                    .setTitle(command.getCommand().substring(0, 1).toUpperCase() + command.getCommand().substring(1))
                    .setDescription(command.getDescription());

            List<Permission> userPerms = command.getRequiredPermissions();
            if (userPerms.size() > 0) embed.addField("Required Permissions",
                    userPerms.stream().map(Permission::getName)
                            .collect(Collectors.joining(", ")), true);

            List<Permission> botPerms = command.getRequiredBotPermissions();
            if (userPerms.size() > 0) embed.addField("Required Bot Permissions",
                    botPerms.stream().map(Permission::getName)
                            .collect(Collectors.joining(", ")), true);

            embed.addField("Command Usage", "```fix\n" + Elefant.PREFIX +
                    command.getCommand() + (command.getUsage().equals("") ? "" : " ") +
                    command.getUsage() + "\n```", false);

            bundle.sendMessage(embed);

        } else {
            StringBuilder message = new StringBuilder();

            executor.getCommands().forEach(command -> {
                if (bundle.getMember().hasPermission(command.getRequiredPermissions()) && !command.isHidden())
                    message.append("**").append(Elefant.PREFIX).append(command.getCommand())
                            .append(command.getUsage().equals("") ? "" : " ").append(command.getUsage()).append("** - ")
                            .append(command.getDescription()).append("\n\n");
            });

            message.append("\nFor support, join https://discord.gg/vNg5RJS");

            bundle.sendMessage(
                    new EmbedBuilder()
                            .setColor(bundle.getGuild().getSelfMember().getColor())
                            .setTitle("Help")
                            .setThumbnail(bundle.getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setDescription(message.toString())
            );
        }
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "List all the commands and get information on specific commands.";
    }

    @Override
    public String getUsage() {
        return "[command]";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("bot", "bothelp", "helpbot", "elefant");
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MESSAGE_EMBED_LINKS);
    }
}
