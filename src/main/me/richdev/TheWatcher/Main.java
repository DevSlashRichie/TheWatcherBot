package me.richdev.TheWatcher;

import com.google.gson.GsonBuilder;
import me.richdev.TheWatcher.Commands.Commands.GeneralCommands;
import me.richdev.TheWatcher.GuildSystem.GCHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Main {

    private GCHandler guildsHandler;
    private static Main instance;
    private JDA jda;

    public static void main(String[] args) {
        instance = new Main();
        instance.setup();
    }

    public void setup() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken("NDMxNzA3OTgwNzAyOTQxMTk0.DairIA.KNKFz_X25FQBrlzJ6FPCz0CsmUE") // THE WATCHER
                    //.setToken("Mzk5OTg2ODUyMzU2MTYxNTM2.Dak7hA.acy6XUCqdHnvt8n-V8xzI4XtSQ4") // APRIL
                    //.setToken("mfa.obZCiAGA8rwXF5jQgK0rXGdaJAPOwiciG7u2IASI77nj2pUHFmV19vnOSgyM4Kpm-OA0rYGV7OrIbkbxHODH") // SELF bOT
                    .setGame(Game.playing(">help || By RichDev"))
                    .buildAsync();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        jda.addEventListener(new GeneralCommands());
        jda.addEventListener(new Listener());

        instance = this;
        guildsHandler = new GCHandler();
    }

    public static Main getInstance() {
        return instance;
    }

    public GCHandler getGuildsHandler() {
        return guildsHandler;
    }
}
