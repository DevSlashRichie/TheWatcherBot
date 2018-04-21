package me.richdev.TheWatcher.GuildSystem;

import java.util.HashSet;
import java.util.Set;

public class GuildInfo {

    private String ID;

    // GENERAL CONFIGURATIONS
    private String prefixCommand;

    // CONFIGURATION MODULES

    // USER STORAGE
    private Set<VirtualUser> userStorage;


    public GuildInfo(String ID) {
        this.ID = ID;
        this.prefixCommand = ">";
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



}
