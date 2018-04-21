package me.richdev.TheWatcher.Commands.List;

import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import me.richdev.TheWatcher.GuildSystem.VirtualUser;
import me.richdev.TheWatcher.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;

@CommandInfo(aliases = { "gold" }, permissionGroup = "", fromPrivateChat = false, descriptionID = "null")
public class GetMyGold extends Command {

    @Override
    public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender chat) {
        GuildInfo info = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        VirtualUser user = info.getUserForced(e.getAuthor().getId());


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        chat.sendMessage(e.getAuthor().getAsMention() + " | You have **" + df.format(user.getGold()) + "** of gold.");
    }

}
