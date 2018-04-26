package me.richdev.TheWatcher.Commands.Music;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandInfo(aliases = { "list", "playlist" }, permissionGroup = "", fromPrivateChat = true, descriptionID = "test")
public class List extends Command {

	@Override
	public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender chat, GuildInfo guildInfo) {

	}

}
