package com.noahhendrickson.elefant.listeners;

import com.noahhendrickson.elefant.logging.BaseLogger;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class LoggingListener extends ListenerAdapter {

    private final BaseLogger logger;

    public LoggingListener(BaseLogger logger) {
        this.logger = logger;
    }

    @Override
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
        logger.logTextChannelCreate(event.getChannel());
    }

    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        logger.logTextChannelDelete(event.getChannel());
    }

    @Override
    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
        logger.logVoiceChannelCreate(event.getChannel());
    }

    @Override
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
        logger.logVoiceChannelDelete(event.getChannel());
    }

    @Override
    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
        logger.logCategoryCreate(event.getCategory());
    }

    @Override
    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
        logger.logCategoryDelete(event.getCategory());
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        logger.logGuildMemberJoin(event.getMember());
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        if (event.getGuild().retrieveAuditLogs().complete().get(0).getType() == ActionType.KICK)
            logger.logGuildMemberKick(event.getGuild(), event.getUser());
        else logger.logGuildMemberRemove(event.getGuild(), event.getUser());
    }

    @Override
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
        logger.logRoleCreate(event.getRole());
    }

    @Override
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
        logger.logRoleDelete(event.getRole());
    }

    @Override
    public void onGuildBan(@Nonnull GuildBanEvent event) {
        logger.logGuildBan(event.getUser(), event.getGuild());
    }

    @Override
    public void onGuildUnban(@Nonnull GuildUnbanEvent event) {
        logger.logGuildUnban(event.getUser(), event.getGuild());
    }

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
        logger.logGuildMemberRoleAdd(event.getRoles(), event.getMember());
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
        logger.logGuildMemberRoleRemove(event.getRoles(), event.getMember());
    }

    @Override
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
        logger.logGuildMemberUpdateNickname(event);
    }

    @Override
    public void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {
        logger.logUserUpdateName(event);
    }

    @Override
    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {
        logger.logUserUpdateDiscriminator(event);
    }
}
