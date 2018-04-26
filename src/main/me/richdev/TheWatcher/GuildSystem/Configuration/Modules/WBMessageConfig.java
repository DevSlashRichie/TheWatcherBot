package me.richdev.TheWatcher.GuildSystem.Configuration.Modules;

import me.richdev.TheWatcher.GuildSystem.Configuration.Config;

public class WBMessageConfig extends Config {

    public WBMessageConfig() {
        super("WBM");
    }

    public String getLogChannel() {
        return getConfig("wb_msg_channel_data", String.class);
    }

    public boolean isWelcomeMessageActive() {
        return getConfig("wb_message_active", boolean.class);
    }

    public String getWelcomeMessageData() {
        return getConfig("wb_msg_data", String.class);
    }

    public boolean isByeMsgActive() {
        return getConfig("bye_msg_active", boolean.class);
    }

    public String getByeMessageData() {
        return getConfig("bye_msg_data", String.class);
    }

    public boolean isPrivateWelcomeMessageActive() {
        return getConfig("private_wb_msg_active", boolean.class);
    }

    public String getPrivateWelcomeMessageData() {
        return getConfig("private_wb_msg_data", String.class);
    }

    public boolean isPrivateByeMsgActive() {
        return getConfig("private_bye_msg_active", boolean.class);
    }

    public String getPrivateByeMessageData() {
        return getConfig("private_bye_msg_data", String.class);
    }

    @Override
    public void defaults() {
        setConfig("wb_msg_channel_data", "432409065239609364"); // TODO: EDIT THIS | IS JUST FOR TEST

        setConfig("wb_msg_active", true);
        setConfig("wb_msg_data","Welcome {0} to {1}! Have fun! Any question ask {2}!");

        setConfig("bye_msg_active", true);
        setConfig("bye_msg_data", "Bye {0}!");

        setConfig("private_wb_msg_active", true);
        setConfig("private_wb_msg_data", "Welcome {0} to {1} HAVE A NICE DAY!");

        setConfig("private_bye_msg_active",true);
        setConfig("private_bye_msg_data", "See you {0}");
    }
}
