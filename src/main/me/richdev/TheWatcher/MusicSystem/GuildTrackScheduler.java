package me.richdev.TheWatcher.MusicSystem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.richdev.TheWatcher.Main;
import me.richdev.TheWatcher.Utils.BotUtil;
import me.richdev.TheWatcher.Utils.ColorUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GuildTrackScheduler extends AudioEventAdapter implements ConnectionListener {

	private Guild guild;
	private Queue<PlayerTrackInfo> playerQueue = new LinkedList<>();
	private PlayerTrackInfo lastTrack = null;
	private Function<Void,Void> playingMessageRemoveFunction = null;
	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	GuildTrackScheduler(Guild guild) {
		this.guild = guild;
	}

	private TextChannel findAppropiateChannelForMessages() {
		//TODO: Configuraciones para configurar el canal de música.
		List<TextChannel> channels = guild.getTextChannels();
		return channels.stream().filter(t -> t.getName().toLowerCase().contains("music") || t.getName().toLowerCase().contains("musica")).findAny().orElseGet(() -> {
			if(!channels.isEmpty()) {
				if(!guild.getPublicRole().hasPermission(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)) {
					return channels.get(0);
				}

				return channels.stream().filter(t -> {
					PermissionOverride override = t.getPermissionOverride(guild.getPublicRole());

					if(override == null) {
						return true;
					}

					return override.getAllowed().contains(Permission.MESSAGE_READ) && override.getAllowed().contains(Permission.MESSAGE_WRITE);
				}).findAny().orElseGet(() -> channels.get(0));
			}

			return null;
		});
	}

	public Guild getGuild() {
		return guild;
	}

	public Collection<PlayerTrackInfo> getQueue() {
		return playerQueue;
	}

	public void addToQueue(PlayerTrackInfo audio) {
		AudioPlayer audioPlayer = Main.getInstance().getPlayerManager().getAudioPlayer(guild);

		PlayerTrackInfo backup = lastTrack;
		lastTrack = audio;

		if(!audioPlayer.startTrack(audio.getAudioTrack(), true)) {
			playerQueue.add(audio);
			lastTrack = backup;
		}
	}

	private void startNext(AudioPlayer audioPlayer) {
		if(!playerQueue.isEmpty()) {
			PlayerTrackInfo nextTrack = playerQueue.remove();

			lastTrack = nextTrack;
			audioPlayer.startTrack(nextTrack.getAudioTrack(), false);
		}
	}

	public boolean removeFromQueue(PlayerTrackInfo audio) {
		return playerQueue.remove(audio);
	}

	public Optional<PlayerTrackInfo> takeFromQueue() {
		return playerQueue.isEmpty() ? Optional.empty() : Optional.of(playerQueue.remove());
	}

	public void shuffleQueue() {
		//playerQueue is a queue of type LinkedList, Collections.shuffle(List<?>> can be applied.
		Collections.shuffle((LinkedList) playerQueue);
	}

	public List<PlayerTrackInfo> getTracksAddedBy(User user) {
		return playerQueue.stream().filter(i -> i.getWhoAdded().getId().equals(user.getId())).collect(Collectors.toList());
	}

	public void removeTracksAddedBy(User user) {
		playerQueue.removeIf(i -> i.getWhoAdded().getId().equals(user.getId()));
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		TextChannel channel = findAppropiateChannelForMessages();

		if(channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Reproductor pausado.");
			builder.setDescription("_Se ha pausado el reproductor de Audio..._\n\nUsa `>resume` para continuar escuchando.");
			builder.setColor(ColorUtil.getLightestColor());
			channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
		}
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		TextChannel channel = findAppropiateChannelForMessages();

		if(channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Reproductor resumido.");
			builder.setDescription("_Se ha resumido el reproductor de Audio..._");
			builder.setColor(ColorUtil.getLightestColor());
			channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
		}
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(playingMessageRemoveFunction != null) {
			playingMessageRemoveFunction.apply(null);
		}

		playingMessageRemoveFunction = null;

		if(endReason.mayStartNext) {
			startNext(player);
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		if(playingMessageRemoveFunction != null) {
			playingMessageRemoveFunction.apply(null);
		}

		playingMessageRemoveFunction = null;

		TextChannel channel = findAppropiateChannelForMessages();

		if(channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Error en la reproducción del Audio.");
			builder.setDescription("Error al reproducir el Audio '" + track.getInfo().title + "'. Mensaje:\n\n```" + exception.getLocalizedMessage() + "```");
			builder.setColor(Color.YELLOW);
			channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		TextChannel channel = findAppropiateChannelForMessages();

		if(channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			User whoAdded = lastTrack.getWhoAdded();
			Member member = guild.getMember(whoAdded);
			String userName = member == null ? whoAdded.getName() : member.getEffectiveName();

			if(lastTrack != null) {
				builder.setFooter("Añadido por " + userName, whoAdded.getEffectiveAvatarUrl());
			}

			builder.setTitle("Escuchando:");
			builder.setDescription("**Titulo:** `" + track.getInfo().title + "`\n**Duración:** `" + BotUtil.millisToDurationFormat(track.getDuration()) + "`\n**Publicado por:** `" + track.getInfo().author + "`\n**Enlace:** <" + track.getInfo().uri + ">\n\n{PROGRESS_BAR}");
			builder.setColor(ColorUtil.getLightestColor());
			MessageEmbed embed = builder.build();

			EmbedBuilder buildToPublish = new EmbedBuilder(embed);
			buildToPublish.setDescription(embed.getDescription().replace("{PROGRESS_BAR}", "```MARKDOWN\n[▷▷▷▷▷▷▷▷▷▷][00:00/" + BotUtil.millisToDurationFormat(track.getDuration()) + "]\n```"));

			channel.sendMessage(buildToPublish.build()).queue(m -> {
				ScheduledFuture future = scheduledExecutorService.scheduleWithFixedDelay(() -> {
					try {
						long barTimeValue = track.getDuration() / 10L;
						long totalBars = track.getPosition() / barTimeValue;

						StringBuilder barReplacement = new StringBuilder();

						for(int i = 0; i < 10; i++) {
							if(i < totalBars) {
								barReplacement.append("▶");

							} else {
								barReplacement.append("▷");
							}
						}

						EmbedBuilder buildToPublish2 = new EmbedBuilder(embed);
						buildToPublish2.setDescription(embed.getDescription().replace("{PROGRESS_BAR}", "```MARKDOWN\n[" + barReplacement.toString() + "][" + BotUtil.millisToDurationFormat(track.getPosition()) + "/" + BotUtil.millisToDurationFormat(track.getDuration()) + "]\n```"));
						m.editMessage(buildToPublish2.build()).queue();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, 20, 20, TimeUnit.SECONDS);

				playingMessageRemoveFunction = x -> {
					future.cancel(true);
					m.delete().queue();
					return null;
				};
			});
		}
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		if(playingMessageRemoveFunction != null) {
			playingMessageRemoveFunction.apply(null);
		}

		playingMessageRemoveFunction = null;

		TextChannel channel = findAppropiateChannelForMessages();

		if(channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No se pudo continuar reproduciendo el Audio.");
			builder.setDescription("_Ocurrió un problema al reproducir el Audio '" + track.getInfo().title + "', saltando al siguiente audio en la cola ..._");
			builder.setColor(Color.YELLOW);
			channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(20, TimeUnit.SECONDS));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(guild.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GuildTrackScheduler) {
			return ((GuildTrackScheduler) obj).guild.getId().equals(guild.getId());
		}

		return super.equals(obj);
	}

	@Override
	public void onPing(long ping) {

	}

	@Override
	public void onStatusChange(ConnectionStatus status) {

	}

	@Override
	public void onUserSpeaking(User user, boolean speaking) {

	}
}
