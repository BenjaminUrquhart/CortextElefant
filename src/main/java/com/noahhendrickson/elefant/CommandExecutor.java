package com.noahhendrickson.elefant;

import com.noahhendrickson.elefant.logging.BaseLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Noah Hendrickson on 4/11/2020
 */
public class CommandExecutor extends ListenerAdapter {

    private final Map<String, ICommand> commandMap;
    private final Set<ICommand> commands;
    private final BaseLogger logger;

    public CommandExecutor(BaseLogger logger) {
        this.commandMap = new HashMap<>();
        this.commands = new HashSet<>();
        this.logger = logger;
        
        try {
        	ICommand command = null;
        	Reflections reflections = new Reflections("com.noahhendrickson.elefant.commands");
        	for(Class<? extends ICommand> clazz : reflections.getSubTypesOf(ICommand.class)) {
        		if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
        			System.err.println("Skipping non-concrete class " + clazz.getSimpleName());
        			continue;
        		}
        		try {
        			command = null;
        			for (Constructor<?> c : clazz.getConstructors()) {
        				if (!Modifier.isPublic(c.getModifiers())) {
        					continue;
        				}
        				if (c.getParameterCount() == 0) {
        					command = (ICommand) c.newInstance();
        				}
        				else if (c.getParameterCount() == 1 && c.getParameterTypes()[0].equals(CommandExecutor.class)) {
        					command = (ICommand) c.newInstance(this);
        				}
        				if (command != null) {
        					registerCommand(command);
        					break;
        				}
        			}
        			if (command == null) {
        				System.err.println("No valid constructors found for command class " + clazz.getSimpleName());
        			}
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot() || author.isFake()) return;

        String contentRaw = event.getMessage().getContentRaw();
        
        String[] raw = contentRaw.split(" ");
        String cmd = raw[0].substring(Elefant.PREFIX.length());

        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        
        ICommand command;

        if (contentRaw.startsWith(Elefant.PREFIX)) {
        	command = commandMap.get(cmd.toLowerCase());
        	if (command == null) {
        		return;
        	}
            if (channel.getTopic() != null) {
                if (channel.getTopic().contains("{-" + cmd + "}")) {
                    channel.sendMessage("This command is unavailable in this channel!").queue();
                    return;
                }
            }

            if (event.getMember() != null && !event.getMember().hasPermission(command.getRequiredPermissions())) {
                channel.sendMessage("You do not have permission to use that command! `" +
                        command.getRequiredPermissions() + "`").queue();
                return;
            }

            if (!guild.getSelfMember().hasPermission(channel,
                    command.getRequiredBotPermissions())) {
                channel.sendMessage("I do not have the correct permissions to execute the " +
                        "command! `" + command.getRequiredPermissions() + "`").queue();
                return;
            }

            try {
                command.execute(new CommandBundle(event, command));
            } catch (Exception e) {
                final String errorCode = getErrorCode();
                event.getChannel().sendMessage("<:error:697249583427747900> " +
                        "Something went wrong! Your error code is `" + errorCode + "`").queue();

                TextChannel errorChannel = event.getJDA().getTextChannelById(699015681303248896L);
                if (errorChannel != null)
                    errorChannel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(new Color(255, 85, 85))
                                    .setTitle("Command Error: " + command.getCommand())
                                    .setDescription("```\n" + e.getLocalizedMessage() + "\n```")
                                    .addField("Server", "( " + guild.getName() + ") `" +
                                            guild.getId() + "`", true)
                                    .addField("Error Code", errorCode, true)
                                    .addField("Exact Command Ran", contentRaw, false)
                                    .addField("Author", "( " + author.getAsTag() + ") `" +
                                            author.getId() + "`", true)
                                    .addField("Channel", "( " + channel.getName() + ") `" +
                                            channel.getId() + "`", true)
                                    .build()
                    ).queue();
                e.printStackTrace();
            }
        }
    }

    public void registerCommand(@NotNull ICommand command) {
        if (commands.add(command)) {
            commandMap.put(command.getCommand().toLowerCase(), command);
            command.getAliases().forEach(alias -> commandMap.put(alias.toLowerCase(), command));
            System.out.println("Registering the '" + command.getCommand() + "' command with Elefant.");
        }
    }

    @Contract(" -> new")
    public final @NotNull Set<ICommand> getCommands() {
        return new HashSet<>(commands);
    }
    
    public final @Nullable ICommand getCommand(@NotNull String command) {
    	return commandMap.get(command.toLowerCase());
    }

    private String getErrorCode() {
        final Random random = new Random();
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        return IntStream.range(0, 10 + random.nextInt(5)).mapToObj(i -> String.valueOf(characters
                .charAt(random.nextInt(characters.length())))).collect(Collectors.joining());
    }
}
