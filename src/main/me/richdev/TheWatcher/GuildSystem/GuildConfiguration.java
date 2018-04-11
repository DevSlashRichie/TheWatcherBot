package me.richdev.TheWatcher.GuildSystem;

public class GuildConfiguration {

    private String ID;

    private String welcomeMessage;

    public GuildConfiguration(String ID) {
        this.ID = ID;
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
}
