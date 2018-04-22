package me.richdev.TheWatcher.Utils;

import java.awt.*;

public class BotUtil {

	public static String arrayToString(String[] array, int startAt) {
		StringBuilder toReturn = new StringBuilder();

		for(int i = startAt; i < array.length; i++) {
			toReturn.append(array[i]).append(" ");
		}

		return toReturn.length() > 0 ? toReturn.substring(0, toReturn.length() - 1) : toReturn.toString();
	}

	public static Color resolveColorFromSourceManager(String sourcename) {
		if(sourcename.equalsIgnoreCase("youtube")) {
			return new Color(255, 0, 0);
		} else if(sourcename.equalsIgnoreCase("soundcloud")) {
			return Color.decode("#ff5500");
		} else if(sourcename.equalsIgnoreCase("bandcamp")) {
			return Color.decode("#1da0c3");
		} else if(sourcename.equalsIgnoreCase("vimeo")) {
			return new Color(26, 183, 234);
		} else if(sourcename.equalsIgnoreCase("twitch")) {
			return new Color(100, 65, 164);
		} else if(sourcename.equalsIgnoreCase("beam.pro")) {
			return new Color(255, 255, 255);
		}

		return ColorUtil.getLightestColor();
	}

	public static String millisToDurationFormat(long millis) {
		long totalMinutes = millis / 60_000L;
		millis -= totalMinutes * 60_000L;
		long totalSeconds = millis / 1_000L;

		return (totalMinutes < 10 ? "0" + totalMinutes : totalMinutes) + ":" + (totalSeconds < 10 ? "0" + totalSeconds : totalSeconds);
	}

}
