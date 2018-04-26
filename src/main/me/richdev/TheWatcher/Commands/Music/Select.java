package me.richdev.TheWatcher.Commands.Music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.GuildSystem.GuildInfo;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.MusicSystem.PlayerManager;
import me.richdev.TheWatcher.MusicSystem.PlayerTrackInfo;
import me.richdev.TheWatcher.Utils.BotUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

@CommandInfo(aliases = { "select", "sel" }, permissionGroup = "", fromPrivateChat = true, descriptionID = "test")
public class Select extends Command {

	@Override
	public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender sender, GuildInfo guildInfo) {
		if(args.length == 0) {
			sender.sendBeautifulMessage(
					new EmbedBuilder()
							.setTitle("Uso incorrecto.")
							.setDescription("Especifica un número.\n\n**Uso:**\n>select <#>")
							.build()
			);
		} else {
			PlayerManager playerManager = Main.getInstance().getPlayerManager();
			User author = e.getAuthor();

			if(!playerManager.hasStoredSearchResult(author.getId())) {
				sender.sendBeautifulMessage(
						new EmbedBuilder()
								.setTitle("Haz una búsqueda primero.")
								.setDescription("Realiza una búsqueda con el comando `>play` antes de usar este comando.")
								.build()
				);
				return;
			}

			List<AudioTrack> lastSearch = playerManager.getStoredSearchResult(author.getId());

			try {
				int number = Integer.parseInt(args[0]) - 1;
				if(number < 0 || number >= lastSearch.size()) {
					sender.sendBeautifulMessage(
							new EmbedBuilder()
									.setTitle("Fuera de rango.")
									.setDescription("El numero que especificaste `" + number + "` está fuera de rango. Específica un número entre 0 y " + lastSearch.size() + " para seleccionar una canción/video de la lista.")
									.build()
					);
				} else {
					AudioTrack selectedTrack = lastSearch.get(number);
					playerManager.getStoredSearchMessage(author.getId()).delete().queue();
					playerManager.removeStoredSearchResult(author.getId());

					playerManager.getTrackScheduler(e.getGuild()).addToQueue(new PlayerTrackInfo(selectedTrack, author));

					if(selectedTrack.getInfo().isStream) {
						sender.sendBeautifulMessage(
								new EmbedBuilder()
										.setTitle("Stream añadido a la cola")
										.setDescription("**Título:** `" + selectedTrack.getInfo().title + "`\n**Streamer:** `" + selectedTrack.getInfo().author + "`\n**Enlace:** <" + selectedTrack.getInfo().uri + ">")
										.setColor(BotUtil.resolveColorFromSourceManager(selectedTrack.getSourceManager().getSourceName()))
										.build()
						);
					} else {
						sender.sendBeautifulMessage(
								new EmbedBuilder()
										.setTitle("Añadido a la cola")
										.setDescription("**Canción/Video:** `" + selectedTrack.getInfo().title + "`\n**Duración:** `" + BotUtil.millisToDurationFormat(selectedTrack.getDuration()) + "`\n**Publicado por:** `" + selectedTrack.getInfo().author + "`\n**Enlace:** <" + selectedTrack.getInfo().uri + ">")
										.setColor(BotUtil.resolveColorFromSourceManager(selectedTrack.getSourceManager().getSourceName()))
										.build()
						);
					}
				}
			} catch (NumberFormatException ex){
				sender.sendBeautifulMessage(
						new EmbedBuilder()
								.setTitle("Uso incorrecto.")
								.setDescription("Especifica un número.\n\n**Uso:**\n>select <#>")
								.build()
				);
			}
		}
	}

}
