package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.CreditsPanelController;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class CreditsPanel extends JPanel implements ViewPanels {

    private int y;
    private final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private final JTextPane textPane = new JTextPane();
    private final JPanel panel = new JPanel(); // panel permet de garder un fond en dessous de textPane

    // police d'écriture
    private final int TITRE = 28;
    private final int SOUS_TITRE_1 = 22;
    private final int SOUS_TITRE_2 = 20;
    private final int CORPS = 16;
    private final int SAUT_LIGNE = 18;

    public CreditsPanel() {
        initPanel();
    }

    private void initPanel() {
        y = MainMenu.Instance.getHeight() + 200;

        CreditsPanelController controller = new CreditsPanelController(this);
        addComponentListener(controller);

        setLayout(new BorderLayout(0, 0));

        textPane.setEditable(false);
        textPane.setHighlighter(null);
        textPane.setOpaque(false);
        textPane.setVisible(false);
        panel.add(textPane);
        panel.setBackground(new Color(0, 0, 0));
        add(panel);

        StyleConstants.setFontSize(attributeSet, TITRE);
        StyleConstants.setFontFamily(attributeSet, "Ink Free");
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.WHITE);
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
        textPane.setCharacterAttributes(attributeSet, true); // attribues liés aux caractères, ex: écrire en gras
        textPane.setParagraphAttributes(attributeSet, true); // attribues liés au paragraphe, ex: centrer le texte
        textPane.setText("\n" + Language.getString("credits.title") + "\n");

        // Ajouter du texte dans le textPane, ici le texte est toujours ajouté à la suite de ce qui existe déjà (doc.getLength ajoute à la fin du texte)
        StyledDocument doc = (StyledDocument) textPane.getDocument();
        Style style = doc.addStyle("StyleName", null); // pour insérer les images
        try {
            /* ========== DEVELOPPEURS ========== */
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.dev"), attributeSet);
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n", attributeSet);
            // NOM PRENOM
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "--------------------------------\n", attributeSet);

            /* ========== DESIGNERS ========== */
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.designer"), attributeSet);
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n", attributeSet);
            // NOM PRENOM
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "Yanis Rozier\n", attributeSet);

            /* ========== MUSIQUES ========== */
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.musics"), attributeSet);
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n", attributeSet);
            // AUTEUR - TITRE - ALBUM D'ORIGINE
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.musics_menu"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nMotoi Sakuraba - Force On The Move - Force of Light : 08\n", attributeSet);
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.musics_level"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nShinobu Tanaka, Ryo Nagamatsu - Tank - Variation 1 - Wii Play : 053\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 2 - Wii Play : 056\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 3 - Wii Play : 057\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 4 - Wii Play : 059\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 5 - Wii Play : 060\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 6 - Wii Play : 061\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 7 - Wii Play : 062\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 8 - Wii Play : 063\n", attributeSet);
            doc.insertString(doc.getLength(), "Shinobu Tanaka, Ryo Nagamatsu - Tank - Variation 9 - Wii Play : 065\n", attributeSet);
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.musics_boss"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nToby Fox - Battle Against A True Hero - Undertale Soundtrack : 098\n", attributeSet);
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.musics_credits"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nToby Fox - Undertale - Undertale Soundtrack : 071\n", attributeSet);

            /* ========== SONS ========== */
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.sfx"), attributeSet);
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n", attributeSet);
            // TITRE (si il y en a un) - ALBUM D'ORIGINE (si il y en a un)
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.sfx_explosion"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nBomberman Touch: The Legend of Mystic Bomb\n", attributeSet);
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.sfx_raid"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nPartners In Rhyme - Airplane jetpass - Free sound effects\n", attributeSet);
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_2);
            doc.insertString(doc.getLength(), "\n" + Language.getString("credits.sfx_end"), attributeSet);
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "\nFiftysounds - Sfx Piano, Short Fantasia\n", attributeSet);

            /* ========== SUJET PROPOSE PAR ========== */
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.subject"), attributeSet);
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n", attributeSet);
            // NOM PRENOM
            StyleConstants.setFontSize(attributeSet, CORPS);
            doc.insertString(doc.getLength(), "Yan Jurski\n", attributeSet);

            /* ========== COPYRIGHT ========== */
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, SAUT_LIGNE);
            doc.insertString(doc.getLength(), "\n\n", attributeSet);
            // COPYRIGHT
            StyleConstants.setFontSize(attributeSet, SOUS_TITRE_1);
            doc.insertString(doc.getLength(), "\n\n" + Language.getString("credits.copyright"), attributeSet);

            /* ========== LOGO DU JEU ========== */
            // SAUT DE LIGNE
            StyleConstants.setFontSize(attributeSet, 50);
            doc.insertString(doc.getLength(), "\n\n", attributeSet);
            // IMAGE DU LOGO
            StyleConstants.setIcon(style, new ImageIcon(ClassLoader.getSystemResource("Images/menu/logoCredits.png")));
            doc.insertString(doc.getLength(), "logoCredits.png\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        if (MainMenu.Instance.getSound().getClip() != null) {
            MainMenu.Instance.getSound().getClip().stop();
            MainMenu.Instance.getSound().getClip().close();
        }
        MainMenu.Instance.getSound().credits();
    }

    public void update() {
        textPane.setVisible(true);
        // x constant et y incrementé de -1, permet de remonter le texte
        textPane.setLocation(getWidth() / 2 - textPane.getWidth() / 2, y--);
    }

    @Override
    public Container getParents() {
        return new MainMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.credits.title");
    }

    public int getBordDuBas() {
        return y;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

}
