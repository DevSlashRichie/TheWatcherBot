package me.richdev.TheWatcher.GuildSystem;

import java.util.HashSet;
import java.util.Set;

public class GuildHandler {

    private Set<GuildInfo> guilds;

    public GuildHandler() {
        guilds = new HashSet<>();g
    }

    public void registerGuild(GuildInfo guildInfo) {
        this.guilds.add(guildInfo);
        System.out.print("Guild with ID: " + guildInfo.getID() + " was added to databse.");
    }

    public void unregisterGuild(GuildInfo guildInfo) {
        this.guilds.remove(guildInfo);
        System.out.println("Guild with ID: " + guildInfo.getID() + " was removed from database.");
    }

    public GuildInfo getGuild(String ID) {
        return guilds.stream().filter(g -> g.getID().equals(ID)).findAny().orElseGet(() -> {
            GuildInfo newConfiguration = new GuildInfo(ID);
            registerGuild(newConfiguration);
            return newConfiguration;
        });
    }

    public Set<GuildInfo> getGuilds() {
        return guilds;
    }
}
