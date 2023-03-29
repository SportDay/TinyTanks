package fr.mardiH.util;

public class StringUtils {

    /**
     * Il supprime l'extension d'un nom de fichier
     *
     * @param txt Texte à modifier.
     * @param by  La chaîne par laquelle remplacer l'extension.
     * @return La méthode renvoie une chaîne.
     */
    public static String removeExtension(String txt, String by, String... extension) {
        for (String t : extension) {
            txt = txt.replaceAll(t, by);
        }
        return txt;
    }
}
