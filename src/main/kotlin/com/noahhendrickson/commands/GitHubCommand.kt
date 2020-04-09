package com.noahhendrickson.commands

import com.noahhendrickson.elefant.commands.Command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class GitHubCommand : Command() {

    override fun execute(args: Array<out String>?, event: MessageReceivedEvent?) {
        super.sendMessage("The GitHub page for this bot is: " +
                "<https://github.com/NoahH99/CortextElefant>", event)
    }

    override fun getAliases(): MutableList<String> {
        return mutableListOf("!github")
    }

    override fun getName(): String {
        return "GitHub"
    }

    override fun getDescription(): String {
        return "What is the bots GitHub page."
    }

    override fun getUsageInstructions(): MutableList<String> {
        return mutableListOf("")
    }
}