package me.richdev.TheWatcher.Commands.Music;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandInfo(aliases = { "select", "sel" }, permissionGroup = "", fromPrivateChat = true)
public class Select extends Command {

	@Override
	public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender chat) {

	}

}
