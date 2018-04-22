package me.richdev.TheWatcher;

import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.GuildSystem.GuildGeneralListener;
import me.richdev.TheWatcher.GuildSystem.GuildHandler;
import me.richdev.TheWatcher.MusicSystem.PlayerManager;
import me.richdev.TheWatcher.RankingSystem.Handling.RankingListener;
import me.richdev.TheWatcher.StringTranslator.SpecialString;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Main {

    private GuildHandler guildsHandler;
    private static Main instance;
    private ShardManager shardManager;
    private PlayerManager playerManager;
    private static Logger logger = LoggerFactory.getLogger("TWMain");

    public static void main(String[] args) {
        instance = new Main();
        SpecialString.getTranslation("");
        //instance.setup();
    }

    public void setup() {
        playerManager = new PlayerManager();
        try {
            DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
            builder.setShardsTotal(2);
            builder.setToken("NDMxNzA3OTgwNzAyOTQxMTk0.DairIA.KNKFz_X25FQBrlzJ6FPCz0CsmUE");
            builder.addEventListeners(new Listener(), new CommandHandler(), new RankingListener(), new GuildGeneralListener());
            builder.setGameProvider(shardId -> Game.of(Game.GameType.DEFAULT, ">help | Shard #" + shardId));
            shardManager = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        instance = this;
        guildsHandler = new GuildHandler();
    }

    public static Main getInstance() {
        return instance;
    }

    public GuildHandler getGuildsHandler() {
        return guildsHandler;
    }

    /**
     * Retrieve the default shard manager provided by JDA.
     * @return ShardManager provided by JDA.
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    /**
     * Retrieve the PlayerManager used for the MusicSystem.
     * @return PlayerManager
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public User getSelfUser() {
        return shardManager.getShards().get(0).getSelfUser();
    }

    public static Logger getLogger() {
        return logger;
    }
}
