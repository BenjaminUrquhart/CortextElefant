package com.noahhendrickson.elefant.commands.utilities;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;

import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class EmojiCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.hasArgs()) {
            if (bundle.getArgAt(0).matches("<a?:(.+):([0-9]+)>")) {
                Emote emote = bundle.getJDA().getEmoteById(bundle.getArgAt(0).replaceAll("<a?:(.+):|>", ""));

                if (emote != null) {
                    StringBuilder builder = new StringBuilder();

                    builder.append("**ID:** ").append(emote.getId());
                    builder.append("\n**Name:** ").append(emote.getName());

                    if (emote.getGuild() != null)
                        builder.append("\n**Guild:** ").append(emote.getGuild().getName()).append("(`")
                                .append(emote.getGuild().getId()).append("`)");

                    builder.append("\n**Animated:** ").append(emote.isAnimated() ? "Yes" : "No");

                    bundle.sendMessage(builder.toString(), "https://cdn.discordapp.com/emojis/" +
                            emote.getId() + "." + (emote.isAnimated() ? "gif" : "png"));

                } else bundle.sendMessage("Invalid Emoji: `" + bundle.getArgAt(0) + "`");
            } else bundle.sendMessage("Invalid Emoji: `" + bundle.getArgAt(0) + "`");
        } else bundle.sendUsageMessage();
    }

    @Override
    public String getCommand() {
        return "emoji";
    }

    @Override
    public String getDescription() {
        return "Returns information on the given emoji.";
    }

    @Override
    public String getUsage() {
        return "{emoji}";
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MESSAGE_EMBED_LINKS);
    }
}
