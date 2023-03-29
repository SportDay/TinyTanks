package fr.mardiH.controller;

import fr.mardiH.model.Enum.FileType;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.AchievementsMenuPanel;
import fr.mardiH.view.panel.CreateMenuPanel;
import fr.mardiH.view.panel.LevelMenuPanel;
import fr.mardiH.view.panel.SettingsMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class MainMenuController {


    public static Container curent;

    public MainMenuController() {
    }

    /**
     * C'est un observer qui enregistre le fichier lorsqu'il est notifié
     */
    public class MainMenuObserver implements Observer {

        private final FileType type;

        public MainMenuObserver(FileType type) {
            this.type = type;
        }

        @Override
        public void update(Observable o, Object arg) {
            if (type == FileType.settings) {
                MainMenu.Instance.sec.saveFile(type, false);
            } else if (type == FileType.campagne) {
                MainMenu.Instance.sec.saveCrypt(type, false);
            } else if (type == FileType.achievements) {
                MainMenu.Instance.sec.saveCrypt(type, false);
            }
        }
    }

    /**
     * C'est une classe qui étend la classe KeyAdapter et qui nous permet d'utiliser le clavier pour actioner
     * le ecran plein et echap pour revenir en arriere
     */
    public class Key extends KeyAdapter {
        private boolean not_pressed = true;
        private boolean fullscreen = false;
        private Container current;

        public void setCurrent(Container current) {
            this.current = current;
            curent = ((ViewPanels) current).getParents();
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            if (not_pressed) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    fullscreen(!fullscreen);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (current != null && ((ViewPanels) current).getParents() != null) {
                        // stop la musique quand on quitte avec la touche échap
                        if (MainMenu.Instance.getSound().getClip() != null) {
                            if (!(MainMenu.Instance.getContentPane() instanceof LevelMenuPanel) && !(MainMenu.Instance.getContentPane() instanceof CreateMenuPanel) &&
                                    !(MainMenu.Instance.getContentPane() instanceof AchievementsMenuPanel) && !(MainMenu.Instance.getContentPane() instanceof SettingsMenuPanel)) {
                                MainMenu.Instance.getSound().getClip().stop();
                                MainMenu.Instance.getSound().getClip().close();
                            }
                        }
                        MainMenu.Instance.setPreviusPanel(((ViewPanels) current).getParents());
                    }
                }
                not_pressed = false;
            }
        }

        @Override
        public void keyReleased(final KeyEvent e) {
            not_pressed = true;
        }

        private void fullscreen(boolean full) {
            MainMenu.Instance.dispose();
            MainMenu.Instance.setExtendedState(full ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
            if (!full) {
                MainMenu.Instance.setSize(MainMenu.Instance.getMinWidth(), MainMenu.Instance.getMinHeight());
            }
            MainMenu.Instance.setUndecorated(full);
            MainMenu.Instance.setVisible(true);
            fullscreen = full;
        }
    }
}
