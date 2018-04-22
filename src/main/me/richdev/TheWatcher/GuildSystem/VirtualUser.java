package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.RankingSystem.Rank;

public class VirtualUser {

    private String ID;
    private Rank rank = new Rank("Test Rank"); // TODO: Remove This.

    // GOLD STORAGE
    private double gold;

    public VirtualUser(String ID) {
        this.ID = ID;
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

    public void upgradeRank() {

    }
}
