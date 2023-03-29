package fr.mardiH.controller.panel;

import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.CreditsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class CreditsPanelController implements ActionListener, ComponentListener {

    private final CreditsPanel panel;
    private final Timer timer = new Timer(18, this);

    public CreditsPanelController(CreditsPanel panel) {
        this.panel = panel;
        MainMenu.timerList.add(timer);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.update();

        // Si le texte arrive en haut de la page, alors on arrête le timer et on retourne au menu du jeu
        if (panel.getBordDuBas() + 200 == -panel.getTextPane().getHeight()) {
            timer.stop();
            MainMenu.Instance.getAchievements().getViewCredits().setUnlocked(true);
            MainMenu.Instance.getSound().getClip().stop();
            MainMenu.Instance.getSound().getClip().close();
            MainMenu.Instance.setMainMenu();
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

}
