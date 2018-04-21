package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.Main;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        GuildInfo guildInfo = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        if(guildInfo == null) return;

        guildInfo.removeUser(e.getMember().getUser().getId());
    }

}
