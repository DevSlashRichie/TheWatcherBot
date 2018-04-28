package me.richdev.TheWatcher.RankingSystem;

import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedHashSet;

public class GuildRanksHandler {

    private GuildInfo guild;
    private LinkedHashSet<Rank> ranks;

    public GuildRanksHandler(GuildInfo guild) {
        this.guild = guild;

        this.ranks = new LinkedHashSet<>();
    }

    public boolean addRank(String name) {
        Role role = guild.getGuild().getRolesByName(name, true).get(0);

        if (role.getName().equalsIgnoreCase(name)) {
            ranks.add(new Rank(role.getName(), role));
            return true;
        }

        return false;
    }

    public boolean removeRank(String name) {
        Rank rank = getRankFromName(name);
        if(rank == null) return false;
        ranks.remove(rank);
        return true;
    }

    public Rank getRankFromName(String name) {
        for (Rank rank : ranks) {
            if(rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    // TODO: THIS
    public void loadExampleRanks() {

    }

}
