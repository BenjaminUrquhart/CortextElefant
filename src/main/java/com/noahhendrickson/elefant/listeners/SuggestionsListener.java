package com.noahhendrickson.elefant.listeners;

import com.noahhendrickson.elefant.CommandBundle;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class SuggestionsListener extends ListenerAdapter {

    private final List<Long> channelIds;

    public SuggestionsListener() {
        this.channelIds = Collections.singletonList(507387919779233816L);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (channelIds.contains(event.getChannel().getIdLong())) {
            if (event.getAuthor().isBot() || event.getAuthor().isFake()) return;
            JDA jda = event.getJDA();

            final Emote SUCCESS = jda.getEmoteById(697249279458279485L);
            final Emote UNDEFINED = jda.getEmoteById(697249477135827055L);
            final Emote ERROR = jda.getEmoteById(697249583427747900L);
            User author = event.getAuthor();

            if (SUCCESS == null || UNDEFINED == null || ERROR == null) return;

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(event.getGuild().getSelfMember().getColor())
                            .setAuthor(author.getAsTag(), null, author.getEffectiveAvatarUrl())
                            .setDescription(event.getMessage().getContentRaw())
                            .setFooter(CommandBundle.FOOTER).build()
            ).queue(message -> {
                message.addReaction(SUCCESS).queue();
                message.addReaction(UNDEFINED).queue();
                message.addReaction(ERROR).queue();
            });

            event.getMessage().delete().queue();
        }
    }
}
