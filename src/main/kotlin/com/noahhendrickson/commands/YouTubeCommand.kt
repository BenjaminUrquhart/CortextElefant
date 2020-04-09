package com.noahhendrickson.commands

import com.noahhendrickson.elefant.commands.Command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class YouTubeCommand : Command() {

    override fun execute(args: Array<out String>?, event: MessageReceivedEvent?) {
        super.sendMessage("Kody's YouTube Channel is: " +
                "<https://www.youtube.com/channel/UC_LtbK9pzAEI-4yVprLOcyA>", event)
    }

    override fun getAliases(): MutableList<String> {
        return mutableListOf("!youtube", "!yt")
    }

    override fun getName(): String {
        return "Kody's YouTube"
    }

    override fun getDescription(): String {
        return "What is Kody's YouTube channel."
    }

    override fun getUsageInstructions(): MutableList<String> {
        return mutableListOf("")
    }
}