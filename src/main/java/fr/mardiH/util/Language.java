package fr.mardiH.util;

import fr.mardiH.view.MainMenu;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Language {

    private static final String BUNDLE_NAME = "language.lang";
    private static ResourceBundle RESOURCE_BUNDLE = loadBundle();
    private Language() {
    }

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME, MainMenu.Instance.getSettingsModel().getLang());
    }

    public static void updateResourceBundle() {
        RESOURCE_BUNDLE = loadBundle();
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
