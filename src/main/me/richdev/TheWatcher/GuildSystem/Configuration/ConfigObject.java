package me.richdev.TheWatcher.GuildSystem.Configuration;

public class ConfigObject<T> {

    private T object;

    public ConfigObject(T object) {
        this.object = object;

    }

    public void setObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public Class<T> getType() {
        return (Class<T>) object.getClass();
    }

}
