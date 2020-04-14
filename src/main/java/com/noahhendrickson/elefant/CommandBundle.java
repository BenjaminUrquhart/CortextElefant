package com.noahhendrickson.elefant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public class CommandBundle {

    public static final String FOOTER = "Elefant Productions";

    private final ICommand command;
    private final List<String> args;

    private final User user;
    private final Member member;
    private final Message message;
    private final TextChannel channel;
    private final Guild guild;
    private final JDA jda;

    public CommandBundle(@NotNull GuildMessageReceivedEvent event, ICommand command) {
        this.command = command;

        List<String> argsWithCommand = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        this.args = argsWithCommand.subList(1, argsWithCommand.size());

        this.user = event.getAuthor();
        this.member = event.getMember();
        this.message = event.getMessage();
        this.channel = event.getChannel();
        this.guild = event.getGuild();
        this.jda = event.getJDA();
    }

    public boolean hasArgs() {
        return args.size() > 0;
    }

    public boolean areArgsEqualTo(int length) {
        return args.size() == length;
    }

    public boolean areArgsMoreThan(int moreThan) {
        return args.size() > moreThan;
    }

    public boolean areArgsLessThan(int lessThan) {
        return args.size() < lessThan;
    }

    public String getArgAt(int at) {
        if (at > args.size())
            throw new IllegalArgumentException("The index of " + at + " is larger than the argument array!");
        if (at < 0)
            throw new IllegalArgumentException("The index cannot be negated!");

        return args.get(at);
    }

    public void sendMessage(@NotNull Message... messages) {
        for (Message message : messages) channel.sendMessage(message).queue();
    }

    public void sendMessage(@NotNull String... messages) {
        for (String message : messages) channel.sendMessage(message).queue();
    }

    public void sendMessage(@NotNull EmbedBuilder... embeds) {
        for (EmbedBuilder embed : embeds) channel.sendMessage(embed.setFooter(FOOTER).build()).queue();
    }

    public void addReaction(long emoteId) {
        Emote emote = jda.getEmoteById(emoteId);
        if (emote != null)
            message.addReaction(emote).queue();
    }

    public User getUserFromMessage() {
        if (message.getMentionedUsers().size() > 0) return message.getMentionedUsers().get(0);
//        String content = message.getContentStripped().substring(command.getCommand().length() + 2);
//
//        System.out.println(content);
//
//        if (message.getJDA().getUsersByName(content, false).size() > 0)
//            return message.getJDA().getUsersByName(content, false).get(0);
//
//        try {
//            if (message.getJDA().getUserById(content) != null)
//                return message.getJDA().getUserById(content);
//        } catch (Exception ignored) {
//        }

        return null;
    }

    public void sendUsageMessage() {
        sendMessage(
                new EmbedBuilder()
                        .setColor(getGuild().getSelfMember().getColor())
                        .setTitle(command.getCommand().substring(0, 1).toUpperCase() + command.getCommand().substring(1))
                        .setDescription(command.getDescription())
                        .addField("Command Usage", "```fix\n" + Elefant.PREFIX +
                                command.getCommand() + (command.getUsage().equals("") ? "" : " ") +
                                command.getUsage() + "\n```", false)
        );
    }

    public ICommand getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public User getUser() {
        return user;
    }

    public Member getMember() {
        return member;
    }

    public Message getMessage() {
        return message;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public JDA getJDA() {
        return jda;
    }
}
