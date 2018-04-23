package me.richdev.TheWatcher.StringTranslator;

import java.text.MessageFormat;
import java.util.Objects;

public enum Language {
    SPANISH("es"),
    ENGLISH("en");

    private String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getFileString() {
        return code + ".json";
    }

    public String translate(String ID, Object... replacements) {
        return MessageFormat.format(Objects.requireNonNull(SpecialString.getTranslation(this, ID)), replacements);
    }

    public static Language getLanguagueByCode(String code) {
        for (Language language : values()) {
            if (language.getCode().equals(code)) {
                return language;
            }
        }
        return null;
    }
}
