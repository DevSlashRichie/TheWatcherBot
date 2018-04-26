package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.Main;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.MessageFormat;

public class GuildGeneralListener extends ListenerAdapter {

    // LISTENING FOR GUILD CHANGES

    public void onGuildJoin(GuildJoinEvent e) {
        Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
    }

    public void onGuildLeave(GuildLeaveEvent e) {
        GuildInfo gf = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        if(gf != null) {
            Main.getInstance().getGuildsHandler().unregisterGuild(gf);
        }
    }


    // LISTENING FOR USER CHANGES

    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        GuildInfo guildInfo = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        if(guildInfo == null) return;

        guildInfo.registerUser(e.getMember().getUser().getId());

        // CONFIGURATION FOR WEBMSG
        TextChannel textChannel = e.getGuild().getTextChannelById(guildInfo.getConfigData("wb_msg_channel_data", String.class));
        //CommandHandler.ChatSender chatSender = new CommandHandler.ChatSender(textChannel, e.getMember());
        if (guildInfo.getConfigData("wb_msg_active", Boolean.class)) {
            textChannel.sendMessage(rep(guildInfo.getConfigData("wb_msg_data", String.class), e.getUser().getName(), e.getGuild().getName(), e.getGuild().getOwner().getEffectiveName())).queue();
        }

        if (guildInfo.getConfigData("private_wb_msg_active", Boolean.class)) {
            textChannel.sendMessage(rep(guildInfo.getConfigData("private_wb_msg_data", String.class), e.getUser().getName(), e.getGuild().getName(), e.getGuild().getOwner().getEffectiveName())).queue();
        }
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        GuildInfo guildInfo = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        if(guildInfo == null) return;

        guildInfo.removeUser(e.getMember().getUser().getId());

        // CONFIGURATION FOR WEBMSG
        TextChannel textChannel = e.getGuild().getTextChannelById(guildInfo.getConfigData("wb_msg_channel_data", String.class));
        if (guildInfo.getConfigData("bye_msg_active", Boolean.class)) {
            textChannel.sendMessage(rep(guildInfo.getConfigData("bye_msg_data", String.class), e.getUser().getName(), e.getGuild().getName(), e.getGuild().getOwner().getEffectiveName())).queue();
        }

        if (guildInfo.getConfigData("private_bye_msg_active", Boolean.class)) {
            textChannel.sendMessage(rep(guildInfo.getConfigData("private_bye_msg_data", String.class), e.getUser().getName(), e.getGuild().getName(), e.getGuild().getOwner().getEffectiveName())).queue();
        }
    }

    private String rep(String s, Object... o) {
        return MessageFormat.format(s, o);
    }

}
