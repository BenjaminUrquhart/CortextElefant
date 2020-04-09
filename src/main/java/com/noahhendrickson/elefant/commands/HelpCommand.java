package com.noahhendrickson.elefant.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Noah Hendrickson on 4/9/2020
 */
public class HelpCommand extends Command {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(String[] args, MessageReceivedEvent event) {
        StringBuilder message = new StringBuilder("__**Available Commands**__\n\n");

        for (ICommand command : manager.getCommands()) {
            message.append("**").append(command.getName()).append("** - `").append(command.getAliases().get(0))
                    .append("` - ").append(command.getDescription()).append("\n");
        }

        super.sendMessage(message.toString(), event);
    }

    @Override
    public List<String> getAliases() {
        return super.asList("!help", "!elefant", "!helpme", "<@!564543483789049876>", "<@!564543483789049876> help");
    }

    @Override
    public String getDescription() {
        return "Get help with the bot.";
    }

    @Override
    public String getName() {
        return "Help";
    }

    @Override
    public List<String> getUsageInstructions() {
        return super.asList("");
    }
}
