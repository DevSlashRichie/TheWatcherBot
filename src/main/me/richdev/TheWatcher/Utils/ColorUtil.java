package me.richdev.TheWatcher.Utils;

import java.awt.*;
import java.util.Random;

public class ColorUtil {

	private static Random colorGlobalRandom = new Random();

	public static Color getLightestColor() {
		return new Color(Color.HSBtoRGB(colorGlobalRandom.nextFloat(), 1F, 0.7F));
	}

	public static Color getNormalColor() {
		return new Color(Color.HSBtoRGB(colorGlobalRandom.nextFloat(), 1F, 0.4F));
	}

	public static Color getDarkerColor() {
		return new Color(Color.HSBtoRGB(colorGlobalRandom.nextFloat(), 1F, 0.3F));
	}

}
