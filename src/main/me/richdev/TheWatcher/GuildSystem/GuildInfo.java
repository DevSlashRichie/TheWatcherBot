package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.StringTranslator.Language;

import java.util.HashSet;
import java.util.Set;

public class GuildInfo {

    private String ID;

    // GENERAL CONFIGURATIONS
    private String prefixCommand;
    private Language language;
    private String musicChannelID;

    // CONFIGURATION MODULES

    // USER STORAGE
    private Set<VirtualUser> userStorage;


    public GuildInfo(String ID) {
        this.ID = ID;
        this.prefixCommand = ">";
        this.language = Language.SPANISH; // TODO: Change to English.
        this.musicChannelID = "438165587277905940"; // TODO: Just for testing, DONT LEAVE IT.

        this.userStorage = new HashSet<>();
    }

    public String getID() {
        return ID;
    }

    public String getPrefixCommand() {
        return prefixCommand;
    }

    public void setPrefixCommand(String prefixCommand) {
        this.prefixCommand = prefixCommand;
    }

    public Set<VirtualUser> getUserStorage() {
        return userStorage;
    }

    public VirtualUser getUserForced(String ID) {
        VirtualUser user = getUser(ID);
        if(user == null)
            user = registerUser(ID);
        return user;
    }

    public VirtualUser getUser(String ID) {
        for (VirtualUser user : userStorage) {
            if (user.getID().equals(ID)) {
                return user;
            }
        }
        return null;
    }

    public VirtualUser registerUser(String ID) {
        VirtualUser user = getUser(ID);
        if(user == null) {
            user = new VirtualUser(ID);
            userStorage.add(user);
        }
        return user;
    }

    public void removeUser(String ID) {
        VirtualUser user = getUser(ID);
        if(user != null)
            userStorage.remove(user);
    }

    public Language getLanguage() {
        return language;
    }

    public String translate(String ID, Object... rep) {
        return language.translate(ID, rep);
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getMusicChannelID() {
        return musicChannelID;
    }
}
