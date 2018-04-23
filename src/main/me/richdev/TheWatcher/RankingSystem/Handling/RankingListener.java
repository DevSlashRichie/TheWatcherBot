package me.richdev.TheWatcher.RankingSystem.Handling;

import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import me.richdev.TheWatcher.GuildSystem.VirtualUser;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.Utils.Randomizer;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class RankingListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot()) return;
        if(CommandHandler.isCMD(Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId()), e.getMessage())) return;

        GuildInfo info = Main.getInstance().getGuildsHandler().getGuild(e.getGuild().getId());
        VirtualUser virtualUser = info.getUserForced(e.getAuthor().getId());

        if (Randomizer.percentage(80 * virtualUser.getRank().getProbModifier())) {

            int l = e.getMessage().getContentRaw().length();

            double mx = (l * 0.5) + virtualUser.getRank().getMinMinedGold();
            mx = (mx > virtualUser.getRank().getMaxMinedGold()) ? virtualUser.getRank().getMaxMinedGold() : mx;
            double mn = virtualUser.getRank().getMinMinedGold();
            double add = Randomizer.betweenDoubles(mx, mn);

            virtualUser.addGold(add);

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            // e.getChannel().sendMessage(e.getAuthor().getAsMention() + " **|** You found **" + df.format(add) + "** of gold!").queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
            e.getChannel().sendMessage(info.translate("ranking.events.getgold", e.getAuthor().getAsMention(), df.format(add))).queue(msg -> msg.delete().queueAfter(10, TimeUnit.SECONDS));
        }

    }

}
