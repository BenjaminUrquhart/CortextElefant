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

class KotlinCommand : Command() {

    override fun execute(args: Array<out String>?, event: MessageReceivedEvent?) {
        super.sendMessage("Check Out Kotlin! <https://kotlinlang.org/>\n\n" +
                "```kotlin\n" + "fun main() {\n" + "    println(\"Hello World\")\n" + "}\n" + "```", event)
    }

    override fun getAliases(): MutableList<String> {
        return mutableListOf("!kotlin")
    }

    override fun getName(): String {
        return "Kotlin"
    }

    override fun getDescription(): String {
        return "A simple command advertising kotlin."
    }

    override fun getUsageInstructions(): MutableList<String> {
        return mutableListOf("")
    }
}

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
