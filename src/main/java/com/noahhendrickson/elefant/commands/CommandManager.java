package com.noahhendrickson.elefant.commands;

import com.noahhendrickson.commands.GitHubCommand;
import com.noahhendrickson.commands.JerCommand;
import com.noahhendrickson.commands.KotlinCommand;
import com.noahhendrickson.commands.YouTubeCommand;
import com.noahhendrickson.elefant.Elefant;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/8/2020
 */
public class CommandManager extends ListenerAdapter {

    private final List<ICommand> commands;
    private final boolean respondToBotsOrFakes;

    public CommandManager(Elefant elefant) {
        this.commands = Arrays.asList(
                // Java Commands
                new BotCommand(),
                new PingCommand(),

                // Kotlin Commands
                new GitHubCommand(),
                new JerCommand(),
                new KotlinCommand(),
                new YouTubeCommand()
        );
        this.respondToBotsOrFakes = Boolean.getBoolean(elefant.getLoginDetails().get(1));
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if ((event.getAuthor().isBot() || event.getAuthor().isFake()) && !respondToBotsOrFakes) return;

        Message message = event.getMessage();

        if (event.getTextChannel().getTopic() != null &&
                event.getTextChannel().getTopic().contains("{-" +
                        message.getContentStripped().split(" ")[0].substring(1) + "}")) {
            if (event.getMember() == null || !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getTextChannel().sendMessage("<:error:697249583427747900> " +
                        "This command is blocked in this channel!").complete();
                return;
            }
        }

        for (ICommand command : commands) {
            String raw = message.getContentRaw();
            if (command.getAliases().contains(raw.split(" ")[0])) {
                command.execute(raw.split(" "), event);
                return;
            }
        }
    }
}
