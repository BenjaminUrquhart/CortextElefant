package com.noahhendrickson.commands

import com.noahhendrickson.elefant.commands.Command
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class JerCommand : Command() {

    override fun execute(args: Array<out String>?, event: MessageReceivedEvent?) {
        event?.message?.delete()?.queue()
        super.sendMessage("Maybe you should learn java fist before making Minecraft plugins...", event)
    }

    override fun getAliases(): MutableList<String> {
        return mutableListOf("!jer", "!learnjava")
    }

    override fun getName(): String {
        return "Learn Java"
    }

    override fun getDescription(): String {
        return "Learn Java first."
    }

    override fun getUsageInstructions(): MutableList<String> {
        return mutableListOf("")
    }
}