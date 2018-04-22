package me.richdev.TheWatcher.MusicSystem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.*;

public class PlayerManager {

	private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
	private Set<GuildTrackScheduler> trackSchedulers = new HashSet<>();
	private Set<GuildAudioPlayer> players = new HashSet<>();
	private Map<String, List<AudioTrack>> savedSearchResult = new HashMap<>();
	private Map<String, Message> savedSearchMessage = new HashMap<>();

	public PlayerManager() {
		audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
		audioPlayerManager.registerSourceManager(new SoundCloudAudioSourceManager());
		audioPlayerManager.registerSourceManager(new BandcampAudioSourceManager());
		audioPlayerManager.registerSourceManager(new VimeoAudioSourceManager());
		audioPlayerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		audioPlayerManager.registerSourceManager(new BeamAudioSourceManager());
	}

	/**
	 * Get TrackScheduler corresponding to the specified Guild.
	 * @param guild The Guild to get TrackScheduler.
	 * @return never-null TrackScheduler corresponding to the specified Guild.
	 */
	public GuildTrackScheduler getTrackScheduler(Guild guild) {
		return trackSchedulers.stream().filter(g -> g.getGuild().getId().equals(guild.getId())).findAny().orElseGet(() -> {
			GuildTrackScheduler newScheduler = new GuildTrackScheduler(guild);
			trackSchedulers.add(newScheduler);
			return newScheduler;
		});
	}

	/**
	 * Get AudioPlayer corresponding to the specified Guild.
	 * @param guild The Guild to get TrackScheduler.
	 * @return never-null AudioPlayer corresponding to the specified Guild.
	 */
	public AudioPlayer getAudioPlayer(Guild guild) {
		return players.stream().filter(p -> p.getGuildId().equals(guild.getId())).map(GuildAudioPlayer::getAudioPlayer).findAny().orElseGet(() -> {
			AudioPlayer player = audioPlayerManager.createPlayer();
			player.addListener(getTrackScheduler(guild));
			GuildAudioPlayer guildAudioPlayer = new GuildAudioPlayer(guild.getId(), player);
			players.add(guildAudioPlayer);
			return player;
		});
	}

	/**
	 * Connects to the specified voice channel. Use this method to make an audio connection.
	 * @param channel Voice Channel to connect.
	 */
	public void connectToVoiceChannel(VoiceChannel channel) {
		AudioManager guildAudioManager = channel.getGuild().getAudioManager();
		guildAudioManager.setAutoReconnect(true);
		if(guildAudioManager.getSendingHandler() == null) {
			guildAudioManager.setSendingHandler(new AudioPlayerSendHandler(getAudioPlayer(channel.getGuild())));
		}
		if(guildAudioManager.getConnectionListener() == null) {
			guildAudioManager.setConnectionListener(getTrackScheduler(channel.getGuild()));
		}
		guildAudioManager.openAudioConnection(channel);
	}

	public AudioPlayerManager getLavaPlayerManager() {
		return audioPlayerManager;
	}

	public boolean hasStoredSearchResult(String userId) {
		return savedSearchResult.containsKey(userId);
	}

	public Message getStoredSearchMessage(String userId) {
		return savedSearchMessage.get(userId);
	}

	public List<AudioTrack> getStoredSearchResult(String userId) {
		return savedSearchResult.get(userId);
	}

	public void removeStoredSearchResult(String userId) {
		savedSearchResult.remove(userId);
		savedSearchMessage.remove(userId);
	}

	public void storeSearchResult(String userId, Message message, List<AudioTrack> results) {
		savedSearchResult.put(userId, results);
		savedSearchMessage.put(userId, message);
	}
}
