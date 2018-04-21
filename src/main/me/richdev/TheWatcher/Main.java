package me.richdev.TheWatcher;

import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.GuildSystem.GuildGeneralListener;
import me.richdev.TheWatcher.GuildSystem.GuildHandler;
import me.richdev.TheWatcher.RankingSystem.Handling.RankingListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Main {

    private GuildHandler guildsHandler;
    private static Main instance;
    private JDA jda;

    public static void main(String[] args) {
        instance = new Main();
        instance.setup();
    }

    private void setup() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken("NDMxNzA3OTgwNzAyOTQxMTk0.DairIA.KNKFz_X25FQBrlzJ6FPCz0CsmUE") // THE WATCHER
                    .setGame(Game.playing(">help || By RichDev"))
                    .buildAsync();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        setupListener();

        instance = this;
        guildsHandler = new GuildHandler();
    }

    private void setupListener() {
        jda.addEventListener(new CommandHandler());
        // jda.addEventListener(new Listener());

        jda.addEventListener(new GuildGeneralListener());
        jda.addEventListener(new RankingListener());
    }

    public static Main getInstance() {
        return instance;
    }

    public GuildHandler getGuildsHandler() {
        return guildsHandler;
    }
}
