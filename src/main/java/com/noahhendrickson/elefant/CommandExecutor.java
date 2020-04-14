package com.noahhendrickson.elefant;

import com.noahhendrickson.elefant.commands.HelpCommand;
import com.noahhendrickson.elefant.commands.infractions.InfCheckCommand;
import com.noahhendrickson.elefant.commands.infractions.InfSearchCommand;
import com.noahhendrickson.elefant.commands.noah.SendControlMessageCommand;
import com.noahhendrickson.elefant.commands.other.CodeCommand;
import com.noahhendrickson.elefant.commands.other.JerCommand;
import com.noahhendrickson.elefant.commands.other.PingCommand;
import com.noahhendrickson.elefant.commands.punishments.MuteCommand;
import com.noahhendrickson.elefant.commands.punishments.UnmuteCommand;
import com.noahhendrickson.elefant.commands.punishments.WarnCommand;
import com.noahhendrickson.elefant.commands.utilities.AvatarCommand;
import com.noahhendrickson.elefant.commands.utilities.EmojiCommand;
import com.noahhendrickson.elefant.commands.utilities.RandomCoinCommand;
import com.noahhendrickson.elefant.commands.utilities.RandomNumberCommand;
import com.noahhendrickson.elefant.logging.BaseLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public class CommandExecutor extends ListenerAdapter {

    private final List<ICommand> commands;
    private final BaseLogger logger;

    public CommandExecutor(BaseLogger logger) {
        this.commands = new ArrayList<>();
        this.logger = logger;

        // Noah
        registerCommand(new SendControlMessageCommand());

        // Infractions
        registerCommand(new InfCheckCommand());
        registerCommand(new InfSearchCommand());

        // Punishments
        registerCommand(new WarnCommand());

        // Utilities
        registerCommand(new AvatarCommand());
        registerCommand(new EmojiCommand());
        registerCommand(new RandomCoinCommand());
        registerCommand(new RandomNumberCommand());

        registerCommand(new CodeCommand());
        registerCommand(new JerCommand());
        registerCommand(new MuteCommand());
        registerCommand(new PingCommand());
        registerCommand(new UnmuteCommand());
        registerCommand(new HelpCommand(this));
    }


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot() || author.isFake()) return;

        String contentRaw = event.getMessage().getContentRaw();

        if (contentRaw.startsWith(Elefant.PREFIX)) {
            for (ICommand command : commands) {
                String[] raw = contentRaw.split(" ");
                String cmd = raw[0].substring(Elefant.PREFIX.length());

                Guild guild = event.getGuild();
                TextChannel channel = event.getChannel();

                if (command.getCommand().equalsIgnoreCase(cmd) || command.getAliases().contains(cmd)) {
                    if (channel.getTopic() != null) {
                        if (channel.getTopic().contains("{-" + cmd + "}")) {
                            channel.sendMessage("This command is unavailable in this channel!").queue();
                            return;
                        }
                    }

                    if (event.getMember() != null && !event.getMember().hasPermission(command.getRequiredPermissions())) {
                        channel.sendMessage("You do not have permission to use that command! `" +
                                command.getRequiredPermissions() + "`").queue();
                        return;
                    }

                    if (!guild.getSelfMember().hasPermission(channel,
                            command.getRequiredBotPermissions())) {
                        channel.sendMessage("I do not have the correct permissions to execute the " +
                                "command! `" + command.getRequiredPermissions() + "`").queue();
                        return;
                    }

                    try {
                        command.execute(new CommandBundle(event, command));
                    } catch (Exception e) {
                        final String errorCode = getErrorCode();
                        event.getChannel().sendMessage("<:error:697249583427747900> " +
                                "Something went wrong! Your error code is `" + errorCode + "`").queue();

                        TextChannel errorChannel = event.getJDA().getTextChannelById(699015681303248896L);
                        if (errorChannel != null)
                            errorChannel.sendMessage(
                                    new EmbedBuilder()
                                            .setColor(new Color(255, 85, 85))
                                            .setTitle("Command Error: " + command.getCommand())
                                            .setDescription("```\n" + e.getLocalizedMessage() + "\n```")
                                            .addField("Server", "( " + guild.getName() + ") `" +
                                                    guild.getId() + "`", true)
                                            .addField("Error Code", errorCode, true)
                                            .addField("Exact Command Ran", contentRaw, false)
                                            .addField("Author", "( " + author.getAsTag() + ") `" +
                                                    author.getId() + "`", true)
                                            .addField("Channel", "( " + channel.getName() + ") `" +
                                                    channel.getId() + "`", true)
                                            .build()
                            ).queue();
                        e.printStackTrace();
                    }
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

    private String getErrorCode() {
        final Random random = new Random();
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        return IntStream.range(0, 10 + random.nextInt(5)).mapToObj(i -> String.valueOf(characters
                .charAt(random.nextInt(characters.length())))).collect(Collectors.joining());
    }
}
