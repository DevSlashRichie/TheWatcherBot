package me.richdev.TheWatcher.MusicSystem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class GuildTrackScheduler extends AudioEventAdapter {

	private Guild guild;
	private Queue<PlayerTrackInfo> playerQueue = new LinkedList<>();

	protected GuildTrackScheduler(Guild guild) {
		this.guild = guild;
	}

	public Guild getGuild() {
		return guild;
	}

	public Collection<PlayerTrackInfo> getQueue() {
		return playerQueue;
	}

	public void addToQueue(PlayerTrackInfo audio) {
		playerQueue.add(audio);
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

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {

	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {

	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {

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
}
