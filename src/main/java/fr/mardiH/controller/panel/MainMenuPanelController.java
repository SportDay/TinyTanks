package fr.mardiH.controller.panel;

import fr.mardiH.model.Enum.FileType;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.MainMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanelController {

    private final MainMenuPanel source;

    public MainMenuPanelController(MainMenuPanel source) {
        this.source = source;
    }

    public class UpdateIcon extends SwingWorker {

        @Override
        protected Image doInBackground() {
            Timer timer = new Timer(50, e -> {
                Image t = source.getLogo().getImage().getScaledInstance(550, source.getCampagneBTN().getHeight() + 300, Image.SCALE_DEFAULT);
                source.getLogoLabel().setIcon(new ImageIcon(t));
            });
            timer.setRepeats(false);
            timer.start();
            return null;
        }
    }

    public class MouseInputs implements ActionListener {

        public MouseInputs() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu.Instance.getSound().btnClick();
            if (e.getSource() == source.getLevelBTN()) {
                MainMenu.Instance.setLevelPanel();

            } else if (e.getSource() == source.getAchivBTN()) {
                MainMenu.Instance.setAchivementPanel();

            } else if (e.getSource() == source.getParamBTN()) {
                MainMenu.Instance.setSettingsPanel();

            } else if (e.getSource() == source.getCampagneBTN()) {
                MainMenu.setLife(MainMenu.Instance.getCampagne().getLife());
                MainMenu.Instance.setLevelPanel("Campagne " + MainMenu.Instance.getCampagne().getLevelNum() + ".yaml", false, true);

            } else if (e.getSource() == source.getRestartBTN()) {
                Object[] options = {Language.getString("btn.restart.text"), Language.getString("btn.cancel.text")};
                int r = JOptionPane.showOptionDialog(MainMenu.Instance, Language.getString("text.campaignremove.text"), Language.getString("text.campaignremove.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (r == 0) {
                    MainMenu.Instance.sec.saveCrypt(FileType.campagne, true);
                    MainMenu.Instance.setMainMenu();
                }
            } else if (e.getSource() == source.getQuitBTn()) {
                System.exit(0);
            }
        }
    }
}
