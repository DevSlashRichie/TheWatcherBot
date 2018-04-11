package me.richdev.TheWatcher.MusicSystem;

import net.dv8tion.jda.core.entities.Guild;

import java.util.HashSet;
import java.util.Set;

public class PlayerManager {

	private Set<GuildTrackScheduler> trackSchedulers = new HashSet<>();

	public GuildTrackScheduler getTrackScheduler(Guild guild) {
		return trackSchedulers.stream().filter(g -> g.getGuild().getId().equals(guild.getId())).findAny().orElseGet(() -> {
			GuildTrackScheduler newScheduler = new GuildTrackScheduler(guild);
			trackSchedulers.add(newScheduler);
			return newScheduler;
		});
	}

}
