package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.GuildSystem.Configuration.ConfigObject;
import me.richdev.TheWatcher.StringTranslator.Language;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class GuildInfo {

    private String ID;

    // GENERAL CONFIGURATIONS
    private String prefixCommand;
    private Language language;
    private String musicChannelID;

    // CONFIGURATION MODULES
    private LinkedHashMap<String, ConfigObject> configWithids; // SELECT ONE

    // USER STORAGE
    private Set<VirtualUser> userStorage;

    // RANKING CONFIGURATION


    public GuildInfo(String ID) {
        this.ID = ID;
        this.prefixCommand = ">";
        this.language = Language.SPANISH; // TODO: Change to English.
        this.musicChannelID = "438165587277905940"; // TODO: Just for testing, DONT LEAVE IT.

        this.userStorage = new HashSet<>();

        this.configWithids = new LinkedHashMap<>();
        defaultConfig();
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



    // CONFIG ----------------------------------------------------------------------------------------------------------

    public HashMap<String, ConfigObject> getConfigWithIds() {
        return configWithids;
    }

    public void setingConfig(String ID, ConfigObject object) {
        configWithids.put(ID, object);
    }

    public ConfigObject getConfigData(String ID) {
        return configWithids.get(ID);
    }

    public <T> T getConfigData(String ID, Class<T> type) {
        return (T) configWithids.get(ID);
    }

    public void defaultConfig() {
        setingConfig("wb_msg_channel_data", new ConfigObject<>("432409065239609364")); // TODO: EDIT THIS | IS JUST FOR TEST
        setingConfig("wb_msg_active", new ConfigObject<>(true));
        setingConfig("wb_msg_data", new ConfigObject<>("Welcome {0} to {1}! Have fun! Any question ask {2}!"));
        setingConfig("bye_msg_active", new ConfigObject<>(true));
        setingConfig("bye_msg_data", new ConfigObject<>("Bye {0}!"));
        setingConfig("private_wb_msg_active", new ConfigObject<>(true));
        setingConfig("private_wb_msg_data", new ConfigObject<>("Welcome {0} to {1} HAVE A NICE DAY!"));
        setingConfig("private_bye_msg_active", new ConfigObject<>(true));
        setingConfig("private_bye_msg_data", new ConfigObject<>("See you {0}"));
    }

}
