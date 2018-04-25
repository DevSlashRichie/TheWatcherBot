package me.richdev.TheWatcher.GuildSystem.Configuration.Modules;

import me.richdev.TheWatcher.GuildSystem.Configuration.Config;

public class WBMessage implements Config {
    private boolean welcome_msg;
    private boolean welcome_private_msg;

    private boolean bye_msg;
    private boolean bye_private_msg;

    @Override
    public String getID() {
        return "wb_msg";
    }
}
