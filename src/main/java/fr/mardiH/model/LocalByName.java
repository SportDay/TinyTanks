package fr.mardiH.model;

import java.util.Locale;

public class LocalByName {
    private final Locale locale;

    public LocalByName(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return locale.getDisplayLanguage() + " " + locale.getDisplayCountry();
    }
}
