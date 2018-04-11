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

        GuildConfiguration requied = null;

        for (GuildConfiguration guild : guilds) {
            if (guild.getID().equals(ID)) {
                requied = guild;
                break;
            }
        }

        if(requied == null) {
            requied = new GuildConfiguration(ID);
            registerGuild(requied);
        }
        return requied;
    }

    public Set<GuildConfiguration> getGuilds() {
        return guilds;
    }
}
