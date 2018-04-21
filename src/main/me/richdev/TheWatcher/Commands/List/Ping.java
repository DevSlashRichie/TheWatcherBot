package me.richdev.TheWatcher.Commands.List;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandInfo(aliases = { "ping", "p" }, permissionGroup = "", fromPrivateChat = true, descriptionID = "")
public class Ping extends Command {

    @Override
    public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender chat) {
        int time = 10;
        if(e.isFromType(ChannelType.PRIVATE))
            time = -1;
        chat.sendNormalMessage("Pong!", time);
    }

}
