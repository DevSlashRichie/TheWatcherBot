package me.richdev.TheWatcher.Commands;

import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public abstract class Command {

    public abstract void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender sender, GuildInfo guildInfo);

}