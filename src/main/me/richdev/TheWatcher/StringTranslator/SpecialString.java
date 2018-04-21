package me.richdev.TheWatcher.StringTranslator;

import java.util.HashMap;

public class SpecialString {

    private String ID;
    private HashMap<Lenguage, String> translations;

    public SpecialString(String ID) {
        this.ID = ID;
        translations = new HashMap<>();
    }

    public void setTranslation(Lenguage l, String display, boolean forceUpdate) {
        if (translations.containsKey(l) && !forceUpdate) {
            return;
        }

        translations.put(l, display);
    }

    public String getID() {
        return ID;
    }
}
