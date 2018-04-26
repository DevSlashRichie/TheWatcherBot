package me.richdev.TheWatcher.GuildSystem.Configuration;

import java.util.LinkedHashMap;

public abstract class Config {

    private LinkedHashMap<String, Object> configurations;
    private String callID;

    public Config(String callID) {
        configurations = new LinkedHashMap<>();
        this.callID = callID;
    }

    public abstract void defaults();

    public void setConfig(String ID, Object object) {
        this.configurations.put(ID, object);
    }

    public <T> T getConfig(String ID, Class<T> type) {
        return (T) this.configurations.get(ID);
    }

    public LinkedHashMap<String, Object> getConfigurations() {
        return configurations;
    }

    public String getCallID() {
        return callID;
    }
}
