package fr.mardiH.controller.panel;

import fr.mardiH.controller.MainMenuController;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.VictoryLosePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static fr.mardiH.Main.logger;

public class VictoryLosePanelController {

    private final VictoryLosePanel source;

    public VictoryLosePanelController(VictoryLosePanel source) {
        this.source = source;
    }

    public class MouseInputs extends MouseAdapter {

        private final int currentPrivateLevel;
        private boolean fromMainMenu;

        public MouseInputs(int currentPrivateLevel, boolean fromMainMenu) {
            this.currentPrivateLevel = currentPrivateLevel;
            this.fromMainMenu = fromMainMenu;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            MainMenu.Instance.getSound().btnClick();
            if (e.getSource() == source.getEndBTN()) {
                MainMenu.Instance.setPreviusPanel(MainMenuController.curent);
            } else if (e.getSource() == source.getRetryBTN()) {
                MainMenu.Instance.setLevelPanel(source.getLevelName(), source.isCustom(), fromMainMenu);
            } else if (e.getSource() == source.getNextBTN()) {
                logger.debug("Next level id: " + (currentPrivateLevel + 1));
                MainMenu.Instance.setLevelPanel("Campagne " + (currentPrivateLevel + 1) + ".yaml", false, fromMainMenu);
            }

        }
    }

    public class UpdateIcon extends SwingWorker {


        public UpdateIcon() {
        }

        @Override
        protected Object doInBackground() {
            int caseSize = 0;
            if (MainMenu.Instance != null) {
                int newSize = (90 * source.getWidth()) / MainMenu.Instance.getMinWidth();
                int newSize1 = (90 * source.getHeight()) / MainMenu.Instance.getMinHeight();
                caseSize = Integer.min(newSize, newSize1);

                source.getEndBTN().setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(source.getEndBTN().getName())).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_SMOOTH)));
                source.getRetryBTN().setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(source.getRetryBTN().getName())).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_SMOOTH)));
                source.getNextBTN().setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(source.getNextBTN().getName())).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_SMOOTH)));

            }
            return null;
        }
    }

    public class Resize extends ComponentAdapter {

        private final JPanel panel;

        public Resize(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (panel instanceof VictoryLosePanel) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(((VictoryLosePanel) panel).getImageBG(), ((VictoryLosePanel) panel).getBackgroundIMG(), panel.getWidth(), panel.getHeight());
                updateBackgroundImageWorker.execute();

                UpdateIcon updateIcon = new UpdateIcon();
                updateIcon.execute();
            }
        }
    }
}
