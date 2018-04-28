package me.richdev.TheWatcher.GuildSystem.Configuration;

public class ConfigObject {

    private Object data;
    private Class type;

    public ConfigObject(Object data) {
        this.data = data;
        this.type = data.getClass();
    }

    public boolean setData(Object data) {
        if(!data.getClass().equals(type))
            return false;

        this.data = data;
        return true;
    }

    public Object getData() {
        return data;
    }

    public Class getType() {
        return type;
    }
}
