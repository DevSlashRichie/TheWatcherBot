package me.richdev.TheWatcher.GuildSystem;

public class GuildConfiguration {

    private String ID;

    // GENERAL CONFIGURATIONS
    private String prefixCommand;

    // MORE CONFIG
    private String welcomeMessage;


    public GuildConfiguration(String ID) {
        this.ID = ID;
        this.prefixCommand = ">";
    }

    public String getID() {
        return ID;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getPrefixCommand() {
        return prefixCommand;
    }

    public void setPrefixCommand(String prefixCommand) {
        this.prefixCommand = prefixCommand;
    }
}
