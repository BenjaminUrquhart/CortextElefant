package com.noahhendrickson.elefant.commands.noah;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import com.noahhendrickson.elefant.utils.FormatUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class SendControlMessageCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        if (bundle.getUser().getIdLong() == 216709975307845633L) {
            if (bundle.hasArgs()) {
                TextChannel channel = bundle.getJDA().getTextChannelById(699015681303248896L);
                if (channel != null) {
                    ZoneId zone = ZoneId.of("America/New_York");

                    channel.sendMessage(FormatUtils.filterEveryone(String.format("`[%s]` %s %s",
                            OffsetDateTime.now().atZoneSameInstant(zone)
                                    .format(DateTimeFormatter.ISO_LOCAL_TIME).substring(0, 8), "\uD83D\uDCDB",
                            FormatUtils.formatFullUser(bundle.getUser()) + " sent a message. (" + bundle.getArgs()
                                    .stream().map(str -> str + " ").collect(Collectors.joining()) + ")"))).queue();

                    bundle.sendMessage(":ok_hand: I sent the message!");
                } else bundle.sendMessage("Can't find the error channel!");
            } else bundle.sendUsageMessage();
        } else bundle.sendMessage("You are not allowed to do this command!");
    }

    @Override
    public String getCommand() {
        return "control.send";
    }

    @Override
    public String getDescription() {
        return "Sends a message to the bot control channel.";
    }

    @Override
    public String getUsage() {
        return "{message}";
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
