package me.richdev.TheWatcher.Commands.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.richdev.TheWatcher.Commands.Command;
import me.richdev.TheWatcher.Commands.CommandHandler;
import me.richdev.TheWatcher.Commands.CommandInfo;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.MusicSystem.GuildTrackScheduler;
import me.richdev.TheWatcher.MusicSystem.PlayerTrackInfo;
import me.richdev.TheWatcher.Utils.BotUtil;
import me.richdev.TheWatcher.Utils.ColorUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

@CommandInfo(aliases = { "play" }, permissionGroup = "", fromPrivateChat = true, descriptionID = "")
public class Play extends Command {

	private Pattern trustedHostsPattern = Pattern.compile("(?:www.)?(?:youtube.com|youtu.be|soundcloud.com|twitch.tv|twitch.com|bandcamp.com|vimeo.com|beam.pro|mixer.com)");

	@Override
	public void execute(String cmd, String[] args, MessageReceivedEvent e, CommandHandler.ChatSender sender) {
		if(args.length == 0) {
			sender.sendBeautifulMessage(
					new EmbedBuilder()
							.setTitle("Uso incorrecto.")
							.setDescription("Especifica un link (URL) o un término de búsqueda.\n\n**Uso:**\n`1. /play <URL>`\n`2. /play <Búsqueda>`")
							.build()
			);
		} else {
			try {
				//try with direct URL
				URL url = new URL(BotUtil.arrayToString(args, 0));
				String host = url.getHost();

				Matcher matcher = trustedHostsPattern.matcher(host);
				if(!matcher.matches()) {
					sender.sendBeautifulMessage(
							new EmbedBuilder()
									.setTitle("Enlace no aceptado.")
									.setDescription("Por seguridad sólo puedes usar enlaces de YouTube, SoundCloud, Twitch, BandCamp, Vimeo, Mixer para reproducir música, un vídeo o un stream.")
									.setColor(Color.RED)
									.build()
					);
					return;
				}

				Main main = Main.getInstance();
				main.getPlayerManager().getLavaPlayerManager().loadItem(url.toString(), new AudioLoadResultHandler() {
					@Override
					public void trackLoaded(AudioTrack track) {
						main.getPlayerManager().getTrackScheduler(e.getGuild()).addToQueue(new PlayerTrackInfo(track, sender.getAsUser()));

						if(track.getInfo().isStream) {
							sender.sendBeautifulMessage(
									new EmbedBuilder()
											.setTitle("Stream añadido a la cola")
											.setDescription("**Título:** `" + track.getInfo().title + "`\n**Streamer:** `" + track.getInfo().author + "`\n**Enlace:** <" + track.getInfo().uri + ">")
											.setColor(BotUtil.resolveColorFromSourceManager(track.getSourceManager().getSourceName()))
											.build()
							);
						} else {
							sender.sendBeautifulMessage(
									new EmbedBuilder()
											.setTitle("Añadido a la cola")
											.setDescription("**Canción/Video:** `" + track.getInfo().title + "`\n**Duración:** `" + BotUtil.millisToDurationFormat(track.getDuration()) + "`\n**Publicado por:** `" + track.getInfo().author + "`\n**Enlace:** <" + track.getInfo().uri + ">")
											.setColor(BotUtil.resolveColorFromSourceManager(track.getSourceManager().getSourceName()))
											.build()
							);
						}
					}

					@Override
					public void playlistLoaded(AudioPlaylist playlist) {

						GuildTrackScheduler trackScheduler = main.getPlayerManager().getTrackScheduler(e.getGuild());
						List<AudioTrack> tracks = playlist.getTracks();

						final long[] totalMillis = {0L};

						tracks.forEach(t -> {
							trackScheduler.addToQueue(new PlayerTrackInfo(t, sender.getAsUser()));
							totalMillis[0] += t.getDuration();
						});

						sender.sendBeautifulMessage(
								new EmbedBuilder()
										.setTitle("Añadido a la cola")
										.setDescription(tracks.size() + " canciones añadidas a la cola.\n**Duración de PlayList:** `" + BotUtil.millisToDurationFormat(totalMillis[0]) + "`")
										.build()
						);
					}

					@Override
					public void noMatches() {
						sender.sendBeautifulMessage(
								new EmbedBuilder()
										.setTitle("Sin resultados")
										.setDescription("No se encontró ningún resultado con el enlace proporcionado. Si crees que es un error contacta a un desarrollador.")
										.build()
						);
					}

					@Override
					public void loadFailed(FriendlyException exception) {
						sender.sendBeautifulMessage(
								new EmbedBuilder()
										.setTitle("No se pudo cargar")
										.setDescription("Ocurrió un error inesperado al cargar el enlace proporcionado.\n\n**Mensaje:**\n````" + exception.getLocalizedMessage() + "```")
										.build()
						);
					}
				});
			} catch (MalformedURLException ignore) {
				//try with a search query
				sender.sendBeautifulMessage(
						new EmbedBuilder()
								.setTitle("Buscando...")
								.setDescription("_Buscando resultados en YouTube y SoundCloud ..._")
								.build()
					,
				-1,
					m -> {
						Main main = Main.getInstance();
						String query = BotUtil.arrayToString(args, 0);
						long timeStart = System.currentTimeMillis();

						main.getPlayerManager().getLavaPlayerManager().loadItem("ytsearch:" + query, new AudioLoadResultHandler() {
							@Override
							public void trackLoaded(AudioTrack track) {
								makeSoundCloudSearch(Collections.singletonList(track));
							}

							@Override
							public void playlistLoaded(AudioPlaylist playlist) {
								makeSoundCloudSearch(playlist.getTracks());
							}

							@Override
							public void noMatches() {
								makeSoundCloudSearch(Collections.emptyList());
							}

							@Override
							public void loadFailed(FriendlyException exception) {
								makeSoundCloudSearch(Collections.emptyList());
							}

							private void makeSoundCloudSearch(List<AudioTrack> ytResults) {
								main.getPlayerManager().getLavaPlayerManager().loadItem("scsearch:" + query, new AudioLoadResultHandler() {
									@Override
									public void trackLoaded(AudioTrack track) {
										publishResults(Collections.singletonList(track));
									}

									@Override
									public void playlistLoaded(AudioPlaylist playlist) {
										publishResults(playlist.getTracks());
									}

									@Override
									public void noMatches() {
										publishResults(Collections.emptyList());
									}

									@Override
									public void loadFailed(FriendlyException exception) {
										publishResults(Collections.emptyList());
									}

									private void publishResults(List<AudioTrack> scResults) {
										long totalTime = System.currentTimeMillis() - timeStart;

										EmbedBuilder builder = new EmbedBuilder();

										builder.setTitle("Resultados de búsqueda (" + (ytResults.size() + scResults.size()) + " resultados | " + totalTime + "ms):");

										StringBuilder descSB = new StringBuilder();

										descSB.append("**Resultados de YouTube:**\n");

										int i = 1;

										if(ytResults.isEmpty()) {
											descSB.append("_Sin resultados._\n");
										} else {
											for(AudioTrack ytTrack : ytResults) {
												if(i == 11) {
													break; //10 results max.
												}

												descSB.append("**").append(i).append("** `[").append(BotUtil.millisToDurationFormat(ytTrack.getDuration())).append("]` `").append(ytTrack.getInfo().title).append("`\n");
												i++;
											}
										}

										descSB.append("\n**Resultados de SounCloud:**\n");

										if(scResults.isEmpty()) {
											descSB.append("_Sin resultados._\n");
										} else {
											int j = 0;
											for(AudioTrack scTrack : scResults) {
												if(j == 11) {
													break; //10 results max.
												}

												descSB.append("**").append(i).append("** `[").append(BotUtil.millisToDurationFormat(scTrack.getDuration())).append("]` `").append(scTrack.getInfo().title).append("`\n");
												i++;
												j++;
											}
										}

										descSB.append("\nUsa `>select <#>` para seleccionar un elemento de la lista.");

										builder.setDescription(descSB.toString());
										builder.setColor(ColorUtil.getLightestColor());

										m.editMessage(builder.build()).queue(s -> {
											if(main.getPlayerManager().hasStoredSearchResult(e.getAuthor().getId())) {
												main.getPlayerManager().getStoredSearchMessage(e.getAuthor().getId()).delete().queue();
												main.getPlayerManager().removeStoredSearchResult(e.getAuthor().getId());
											}

											ytResults.addAll(scResults);
											main.getPlayerManager().storeSearchResult(e.getAuthor().getId(), s, ytResults);
										});
									}
								});
							}
						});
					}, t -> Main.getLogger().error("Exception while sending a message, guild: " + e.getGuild().getId() + ", channel: " + e.getTextChannel().getId(), t)
				);
			}
		}
	}

}
