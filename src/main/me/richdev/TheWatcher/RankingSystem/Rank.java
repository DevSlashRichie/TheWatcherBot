package me.richdev.TheWatcher.RankingSystem;

import net.dv8tion.jda.core.entities.Role;

import java.util.HashSet;
import java.util.Set;

public class Rank {

    private String name;
    private Role role;

    // GOLD MODIFING
    private int minMinedGold;
    private int maxMinedGold;


    // MODIFIERS
    private double probModifier;
    private int maxModifier;

    private Set<RankModifier> modifiers;

    public Rank(String name, Role role) {
        this.name = name;
        this.role = role;

        this.modifiers = new HashSet<>();

        // DEFAULTS
        this.maxModifier = 3;
        this.probModifier = 2;

        this.minMinedGold = 1;
        this.maxMinedGold = 30;
    }


    public void addModifier(RankModifier modifier) {
        this.modifiers.add(modifier);
    }

    public Set<RankModifier> getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public double getProbModifier() {
        return probModifier;
    }

    public int getMaxModifier() {
        return maxModifier;
    }

    public void setMaxMinedGold(int maxMinedGold) {
        this.maxMinedGold =  maxMinedGold;
    }

    public void setMinMinedGold(int minMinedGold) {
        this.minMinedGold = minMinedGold;
    }

    public int getMaxMinedGold() {
        return maxMinedGold;
    }

    public int getMinMinedGold() {
        return minMinedGold;
    }

    public Role getRole() {
        return role;
    }
}
