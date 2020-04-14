package com.noahhendrickson.elefant.commands.other;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.JDA;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public class PingCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        JDA jda = bundle.getJDA();
        bundle.sendMessage("**Pong!** Gateway: `" + jda.getGatewayPing() +
                "` Rest: `" + jda.getRestPing().complete() + "`");
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Ping the bot.";
    }
}
