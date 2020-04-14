package com.noahhendrickson.elefant.logging;

import com.noahhendrickson.elefant.utils.FormatUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.noahhendrickson.elefant.utils.FormatUtils.formatFullUser;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class BaseLogger {

    private final static String COMMAND = "\uD83D\uDD27";
    private final static String CREATE = "\uD83D\uDD8A";
    private final static String DELETE = "\uD83D\uDDD1";
    private final static String JOIN = "\uD83D\uDCE5";
    private final static String LEAVE = "\uD83D\uDCE4";
    private final static String NEW = "\uD83C\uDD95";
    private final static String BAN = "\uD83D\uDEA8";
    private final static String KICK = "\uD83D\uDC62";
    private final static String ROLE = "\uD83D\uDD11";
    private final static String NAME = "\uD83D\uDCDB";

    private void log(OffsetDateTime now, String emote, String message, List<TextChannel> channels) {
        for (TextChannel channel : channels) {
            ZoneId zone = ZoneId.of("America/New_York");

            channel.sendMessage(FormatUtils.filterEveryone(String.format("`[%s]` %s %s", now.atZoneSameInstant(zone)
                    .format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0, 8), emote, message))).queue();
        }
    }

    @SuppressWarnings("unused")
    public void logCommandUsed(Member member, TextChannel channel, String command) {
        if (member == null || command == null) return;

        List<TextChannel> channels = getLogChannels(member.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), COMMAND, formatFullUser(member.getUser()) +
                " used command in **#" + channel.getName() + "** `" + command + "`.", channels);
    }

    public void logTextChannelCreate(TextChannel channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), CREATE, "channel " + channel.getAsMention() + " was created.", channels);
    }

    public void logTextChannelDelete(TextChannel channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), DELETE, "channel #" + channel.getName() + " was deleted.", channels);
    }

    public void logVoiceChannelCreate(VoiceChannel channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), CREATE, "channel #" + channel.getName() + " was created.", channels);
    }

    public void logVoiceChannelDelete(VoiceChannel channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), DELETE, "channel #" + channel.getName() + " was deleted.", channels);
    }

    public void logCategoryCreate(Category channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), CREATE, "channel #" + channel.getName() + " was created.", channels);
    }

    public void logCategoryDelete(Category channel) {
        if (channel == null) return;

        List<TextChannel> channels = getLogChannels(channel.getGuild());
        if (channels == null) return;

        log(OffsetDateTime.now(), DELETE, "channel #" + channel.getName() + " was deleted.", channels);
    }

    public void logGuildMemberJoin(Member member) {
        if (member == null) return;

        List<TextChannel> channels = getLogChannels(member.getGuild());
        if (channels == null) return;

        OffsetDateTime now = OffsetDateTime.now();
        boolean isNew = 604800 >= member.getUser().getTimeCreated().until(now, ChronoUnit.SECONDS);

        log(now, JOIN, formatFullUser(member.getUser()) + " joined." +
                (isNew ? " " + NEW + " " : " ") + "(Created on " + member.getUser().getTimeCreated().format(
                DateTimeFormatter.ofPattern("cccc, MMMM dd, yyyy")) + ")", channels);
    }

    public void logGuildMemberRemove(Guild guild, User user) {
        if (guild == null || user == null) return;

        List<TextChannel> channels = getLogChannels(guild);
        if (channels == null) return;


        log(OffsetDateTime.now(), LEAVE, formatFullUser(user) + " left the server.", channels);
    }

    public void logGuildMemberKick(Guild guild, User user) {
        if (guild == null || user == null) return;

        List<TextChannel> channels = getLogChannels(guild);
        if (channels == null) return;


        log(OffsetDateTime.now(), KICK, formatFullUser(user) + " was kicked from the server.", channels);
    }

    public void logRoleCreate(Role role) {
        if (role == null) return;

        List<TextChannel> channels = getLogChannels(role.getGuild());
        if (channels == null) return;

        OffsetDateTime now = OffsetDateTime.now();
        log(now, CREATE, "role " + role.getName() + " was created.", channels);
    }

    public void logRoleDelete(Role role) {
        if (role == null) return;

        List<TextChannel> channels = getLogChannels(role.getGuild());
        if (channels == null) return;

        OffsetDateTime now = OffsetDateTime.now();
        log(now, DELETE, "role " + role.getName() + " was deleted.", channels);
    }

    public void logGuildBan(User user, Guild guild) {
        if (user == null || guild == null) return;

        List<TextChannel> channels = getLogChannels(guild);
        if (channels == null) return;

        OffsetDateTime now = OffsetDateTime.now();
        log(now, BAN, formatFullUser(user) + " was banned", channels);
    }

    public void logGuildUnban(User user, Guild guild) {
        if (user == null || guild == null) return;

        List<TextChannel> channels = getLogChannels(guild);
        if (channels == null) return;

        OffsetDateTime now = OffsetDateTime.now();
        log(now, BAN, formatFullUser(user) + " was unbanned", channels);
    }

    public void logGuildMemberRoleAdd(List<Role> roles, Member member) {
        System.out.println("Here");

        if (roles == null || roles.size() <= 0) return;

        List<TextChannel> channels = getLogChannels(member.getGuild());
        if (channels == null) return;

        for (Role role : roles) {
            OffsetDateTime now = OffsetDateTime.now();
            log(now, ROLE, formatFullUser(member.getUser()) +
                    " role added **" + role.getName() + "**", channels);
        }
    }

    public void logGuildMemberRoleRemove(List<Role> roles, Member member) {
        if (roles == null || roles.size() <= 0) return;

        List<TextChannel> channels = getLogChannels(member.getGuild());
        if (channels == null) return;

        for (Role role : roles) {
            OffsetDateTime now = OffsetDateTime.now();
            log(now, ROLE, formatFullUser(member.getUser()) +
                    " role removed **" + role.getName() + "**", channels);
        }
    }

    public void logGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        String newNick = event.getNewNickname();
        String prevNick = event.getOldNickname();

        List<TextChannel> channels = getLogChannels(event.getGuild());
        if (channels == null) return;

        String message;
        if (prevNick == null) message = formatFullUser(event.getUser()) + " added nickname `" + newNick + "`";
        else if (newNick == null) message = formatFullUser(event.getUser()) + " removed nickname `" + prevNick + "`";
        else message = formatFullUser(event.getUser()) + " " +
                    "changed nickname from `" + prevNick + "` to `" + newNick + "`";

        log(OffsetDateTime.now(), NAME, message, channels);
    }

    public void logUserUpdateName(UserUpdateNameEvent event) {
        event.getUser().getMutualGuilds().stream().map(this::getLogChannels).filter(Objects::nonNull).forEachOrdered(channels -> {
            String discrim = "#" + event.getUser().getDiscriminator();
            log(OffsetDateTime.now(), NAME, formatFullUser(event.getUser()) + " changed username from `" +
                    event.getOldName() + discrim + "` to `" + event.getNewName() + discrim + "`", channels);
        });
    }

    public void logUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event) {
        event.getUser().getMutualGuilds().stream().map(this::getLogChannels).filter(Objects::nonNull).forEachOrdered(channels -> {
            String name = event.getUser().getName() + "#";
            log(OffsetDateTime.now(), NAME, formatFullUser(event.getUser()) + " changed discriminator from `" +
                    name + event.getOldDiscriminator() + "` to `" + name + event.getNewDiscriminator() + "`", channels);
        });
    }

    private List<TextChannel> getLogChannels(Guild guild) {
        if (guild.getIdLong() == 259798984220999681L) {
            return Collections.singletonList(guild.getTextChannelById(699015681303248896L));
//        } else if (guild.getIdLong() == 503656531665879063L) {
//            return Collections.singletonList(guild.getTextChannelById(698989263726837912L));
        } else return null;
    }
}
