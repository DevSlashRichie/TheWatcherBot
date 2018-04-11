package me.richdev.TheWatcher.GuildSystem;

import java.util.HashSet;
import java.util.Set;

public class GCHandler {

    private Set<GuildConfiguration> guilds;

    public GCHandler() {
        guilds = new HashSet<>();
    }

    public void registerGuild(GuildConfiguration guildConfiguration) {
        this.guilds.add(guildConfiguration);
    }

    public GuildConfiguration getGuild(String ID) {
        return guilds.stream().filter(g -> g.getID().equals(ID)).findAny().orElseGet(() -> {
            GuildConfiguration newConfiguration = new GuildConfiguration(ID);
            registerGuild(newConfiguration);
            return newConfiguration;
        });
    }

    public Set<GuildConfiguration> getGuilds() {
        return guilds;
    }
}
