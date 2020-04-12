package com.noahhendrickson.elefant.commands;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.List;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class CodeCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        bundle.sendMessage(
                new EmbedBuilder()
                        .setColor(bundle.getGuild().getSelfMember().getColor())
                        .setTitle("Code Blocks")
                        .setDescription("Put your code in code blocks!\n\n**Example:**\n\n\\`\\`\\`java\n" +
                                "public static void main(String[] args) {\n" +
                                "    System.out.println(\"Hello World!\");\n}\n\\`\\`\\`\n\n**Becomes:**\n\n" +
                                "```java\n" +
                                "public static void main(String[] args) {\n" +
                                "    System.out.println(\"Hello World!\");\n}\n```\n\n" +
                                "Use [Hastebin](https://hastebin.com/) for larger segments of code!")
        );
    }

    @Override
    public String getCommand() {
        return "code";
    }

    @Override
    public String getDescription() {
        return "Sends a helpful message about sending code.";
    }

    @Override
    public List<Permission> getRequiredBotPermissions() {
        return Collections.singletonList(Permission.MESSAGE_EMBED_LINKS);
    }
}
