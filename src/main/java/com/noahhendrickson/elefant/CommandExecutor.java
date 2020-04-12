package com.noahhendrickson.elefant;

import com.noahhendrickson.elefant.commands.CodeCommand;
import com.noahhendrickson.elefant.commands.HelpCommand;
import com.noahhendrickson.elefant.commands.PingCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public class CommandExecutor extends ListenerAdapter {

    private final List<ICommand> commands;

    public CommandExecutor() {
        this.commands = new ArrayList<>();

        registerCommand(new CodeCommand());
        registerCommand(new PingCommand());
        registerCommand(new HelpCommand(this));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake()) return;
        if (event.getAuthor().getIdLong() != 216709975307845633L) return;

        String contentRaw = event.getMessage().getContentRaw();

        if (contentRaw.startsWith(Elefant.PREFIX)) {
            for (ICommand command : commands) {
                String[] raw = contentRaw.split(" ");
                String cmd = raw[0].substring(Elefant.PREFIX.length());

                if (command.getCommand().equalsIgnoreCase(cmd) || command.getAliases().contains(cmd)) {
                    event.getChannel().sendTyping().queue();

                    if (event.getChannel().getTopic() != null) {
                        if (event.getChannel().getTopic().contains("{-" + cmd + "}")) {
                            event.getChannel().sendMessage("This command is unavailable in this channel!").queue();
                            return;
                        }
                    }

                    if (event.getMember() != null && !event.getMember().hasPermission(command.getRequiredPermissions())) {
                        event.getChannel().sendMessage("You do not have permission to use that command!").queue();
                        return;
                    }

                    if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(),
                            command.getRequiredBotPermissions())) {
                        event.getChannel().sendMessage("I do not have the correct permissions to execute the command!").queue();
                        return;
                    }

                    command.execute(new CommandBundle(event));
                }
            }
        }
    }

    public void registerCommand(@NotNull ICommand command) {
        if (!commands.contains(command)) {
            commands.add(command);
            System.out.println("Registering the '" + command.getCommand() + "' command with Elefant.");
        }
    }

    @Contract(" -> new")
    public final @NotNull List<ICommand> getCommands() {
        return new ArrayList<>(commands);
    }
}
