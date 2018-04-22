package me.richdev.TheWatcher.Utils;

import java.util.Random;

public class Randomizer {
    private final static Random random = new Random();

    public static boolean percentage(double perce) {
        return random.nextInt(100) <= perce;
    }

    public static int between(int max, int min) {
        return (random.nextInt((max-min < 1) ? 1 : max-min) + min);
    }

    public static double betweenDoubles(double mx, double mn) {
        return mn + (random.nextDouble() * (mx-mn));
    }
}
