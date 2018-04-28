package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.RankingSystem.Rank;

public class VirtualUser {

    private String ID;
    private GuildInfo guild;
    private Rank rank = new Rank("Test Rank", null); // TODO: Remove This.

    // GOLD STORAGE
    private double gold;

    public VirtualUser(String ID, GuildInfo guild) {
        this.ID = ID;
        this.guild = guild;
    }

    public String getID() {
        return ID;
    }

    public void addGold(double amount) {
        this.gold += amount;
    }

    public void removeGold(double amount) {
        this.gold -= amount;
    }

    public void setGold(long amount) {
        this.gold = amount;
    }

    public double getGold() {
        return gold;
    }

    public Rank getRank() {
        return rank;
    }

    public GuildInfo getGuild() {
        return guild;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
