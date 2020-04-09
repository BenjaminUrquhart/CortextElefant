package com.noahhendrickson.elefant.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Noah Hendrickson on 4/9/2020
 */
public class EightBallCommand extends Command {

    private final List<String> responses = super.asList(
            "It is certain.",
            "It is decidedly so.",
            "Without a doubt.",
            "Yes - definitely.",
            "You may rely on it.",
            "As I see it, yes.",
            "Most likely.",
            "Outlook good.",
            "Yes.",
            "Signs point to yes.",
            "Reply hazy, try again.",
            "Ask again later.",
            "Better not tell you now.",
            "Cannot predict now.",
            "Concentrate and ask again.",
            "Don't count on it.",
            "My reply is no.",
            "My sources say no.",
            "Outlook not so good.",
            "Very doubtful."
    );

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        super.sendMessage(args.length >= 2 ? responses.get(new Random().nextInt(responses.size())) :
                "You need to supply a message! " + super.getCommand(), event);
    }

    @Override
    public List<String> getAliases() {
        return super.asList("!8ball", "!eightball", "!magicball");
    }

    @Override
    public String getDescription() {
        return "Tell your fortune.";
    }

    @Override
    public String getName() {
        return "8Ball";
    }

    @Override
    public List<String> getUsageInstructions() {
        return super.asList("<message>");
    }
}
