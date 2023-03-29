package fr.mardiH.controller.panel;

import fr.mardiH.controller.MainMenuController;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.AchievementsMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class AchievementsMenuController {
    public AchievementsMenuController() {

    }


    /**
     * Cette classe est utilisée pour gérer les entrées de la souris dans le jeu
     */
    public class MouseInputs extends MouseAdapter {

        private final JPanel parent;

        public MouseInputs(JPanel parent) {
            this.parent = parent;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MainMenu.Instance.getSound().btnClick();
            if (parent instanceof AchievementsMenuPanel) {
                if (e.getSource() == ((AchievementsMenuPanel) parent).getBackBTN()) {
                    MainMenu.Instance.setPreviusPanel(MainMenuController.curent);
                }
            }
        }
    }

    /**
     * C'est un SwingWorker qui met à jour la taille des JPanels dans l'achiList
     */
    public class UpdateIcon extends SwingWorker {

        private final LinkedList<JPanel> achiList;

        public UpdateIcon(LinkedList<JPanel> achiList) {
            this.achiList = achiList;
        }

        @Override
        protected Object doInBackground() {
            if (achiList != null) {
                for (JPanel entry : achiList) {
                    int width = (275 * MainMenu.Instance.getWidth()) / MainMenu.Instance.getMinWidth();
                    int height = (100 * MainMenu.Instance.getHeight()) / MainMenu.Instance.getMinHeight();
                    entry.setPreferredSize(new Dimension(width, height));
                    entry.revalidate();
                }
            }
            return null;
        }
    }


    /**
     * C'est une classe qui étend ComponentAdapter et remplace la méthode componentResized
     */
    public class Resize extends ComponentAdapter {

        private final JPanel panel;

        public Resize(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (panel instanceof AchievementsMenuPanel) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(((AchievementsMenuPanel) panel).getImageBG(), ((AchievementsMenuPanel) panel).getBackgroundIMG(), panel.getWidth(), panel.getHeight());
                updateBackgroundImageWorker.execute();

                UpdateIcon updateIcon = new UpdateIcon(((AchievementsMenuPanel) panel).getAchiList());
                updateIcon.execute();
            }
        }
    }
}
