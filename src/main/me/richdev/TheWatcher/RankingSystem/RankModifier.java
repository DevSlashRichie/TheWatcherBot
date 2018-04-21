package me.richdev.TheWatcher.RankingSystem;

import me.richdev.TheWatcher.StringTranslator.SpecialString;

public abstract class RankModifier {

    private String name;
    private SpecialString displayName;

    public RankModifier(String name, SpecialString displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public SpecialString getDisplayName() {
        return displayName;
    }

    public abstract void modified();

}
