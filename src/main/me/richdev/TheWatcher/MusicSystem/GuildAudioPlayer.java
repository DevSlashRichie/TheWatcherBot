package me.richdev.TheWatcher.MusicSystem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import java.util.Objects;

public class GuildAudioPlayer {

	private String guildId;
	private AudioPlayer audioPlayer;

	public GuildAudioPlayer(String guildId, AudioPlayer player) {
		this.guildId = guildId;
		this.audioPlayer = player;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public String getGuildId() {
		return guildId;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(guildId);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GuildAudioPlayer) {
			return ((GuildAudioPlayer) obj).guildId.equals(guildId);
		}

		return super.equals(obj);
	}
}
