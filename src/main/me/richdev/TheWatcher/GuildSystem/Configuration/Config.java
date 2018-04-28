package me.richdev.TheWatcher.GuildSystem.Configuration;

import java.util.LinkedHashMap;

public abstract class Config {

    private LinkedHashMap<String, ConfigObject> configurations;
    private String callID;

    public Config(String callID) {
        configurations = new LinkedHashMap<>();
        this.callID = callID;
    }

    public abstract void defaults();

    public void setConfig(String ID, ConfigObject object) {
        this.configurations.put(ID, object);
    }

    public ConfigObject getConfig(String ID) {
        return this.configurations.get(ID);
    }

    public LinkedHashMap<String, ConfigObject> getConfigurations() {
        return configurations;
    }

    public String getCallID() {
        return callID;
    }
}
