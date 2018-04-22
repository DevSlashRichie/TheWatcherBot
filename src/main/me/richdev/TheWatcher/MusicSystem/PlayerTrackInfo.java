package me.richdev.TheWatcher.MusicSystem;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.User;

public class PlayerTrackInfo {

	private User addedBy;
	private AudioTrack audioTrack;
	private long addTimestamp;

	public PlayerTrackInfo(AudioTrack track, User addedBy) {
		this.addedBy = addedBy;
		this.audioTrack = track;
		this.addTimestamp = System.currentTimeMillis();
	}

	public User getWhoAdded() {
		return addedBy;
	}

	public AudioTrack getAudioTrack() {
		return audioTrack;
	}

	public long getAddTimestamp() {
		return addTimestamp;
	}
}
