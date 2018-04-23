package me.richdev.TheWatcher.Utils;

public class Validate {

    public static void isEmpty(String str, String msg) {
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isNull(Object object, String msg) {
        if(object == null) {
            throw new NullPointerException(msg);
        }
    }

}
