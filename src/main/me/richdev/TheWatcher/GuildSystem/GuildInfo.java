package me.richdev.TheWatcher.GuildSystem;

import me.richdev.TheWatcher.GuildSystem.Configuration.Config;
import me.richdev.TheWatcher.GuildSystem.Configuration.Modules.WBMessageConfig;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.RankingSystem.GuildRanksHandler;
import me.richdev.TheWatcher.StringTranslator.Language;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashSet;
import java.util.Set;

public class GuildInfo {

    private String ID;
    private Guild guild;

    // GENERAL CONFIGURATIONS
    private String prefixCommand;
    private Language language;
    private String musicChannelID;

    // CONFIGURATION MODULES
    private Set<Config> configModules;

    private WBMessageConfig logWelcomeConfigurationModule;

    // USER STORAGE
    private Set<VirtualUser> userStorage;

    // RANKING CONFIGURATION
    private GuildRanksHandler guildRanksHandler;

    public GuildInfo(String ID) {
        this.ID = ID;
        this.guild = Main.getInstance().getShardManager().getGuildById(ID);
        this.guildRanksHandler = new GuildRanksHandler(this);

        this.prefixCommand = ">";
        this.language = Language.SPANISH; // TODO: Change to English.
        this.musicChannelID = "438165587277905940"; // TODO: Just for testing, DONT LEAVE IT.

        this.userStorage = new HashSet<>();

        // CONFIGURATION INITIALIZATION
        this.configModules = new HashSet<>();

        this.logWelcomeConfigurationModule = new WBMessageConfig();

        configModules.add(logWelcomeConfigurationModule);
    }

    public GuildInfo(String ID, boolean defaults) {
        this(ID);
        if(defaults)
            loadConfigDefaults();
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
            user = new VirtualUser(ID, this);
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

    public Guild getGuild() {
        return guild;
    }

    public GuildRanksHandler getGuildRanksHandler() {
        return guildRanksHandler;
    }

    // CONFIG ----------------------------------------------------------------------------------------------------------

    private void loadConfigDefaults() {

    }

    public Set<Config> getConfigModules() {
        return configModules;
    }

    public <T> T getConfigModule(Class<T> configClass) {
        for (Config configModule : configModules) {
            if(configModule.getClass().equals(configClass))
                return (T) configModule;
        }
        return null;
    }

    public Config getConfigModule(String ID) {
        for (Config configModule : configModules) {
            if(configModule.getCallID().equals(ID)) {
                return configModule;
            }
        }
        return null;
    }

    public WBMessageConfig getLogWelcomeConfigurationModule() {
        return logWelcomeConfigurationModule;
    }

}
