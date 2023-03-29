package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.MainMenuPanelController;
import fr.mardiH.controller.panel.UpdateBackgroundImageWorker;
import fr.mardiH.model.BTNModel;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainMenuPanel extends JPanel implements ViewPanels {


    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private final ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("Images/menu/logo.png"));
    private final JLabel LogoLabel = new JLabel();
    private final JLabel imageBG = new JLabel();
    private final JButton campagneBTN = new BTNModel(Language.getString("btn.campaign.text"), 21);
    private final JButton restartBTN = new BTNModel(Language.getString("btn.restart.text"), 21);
    private final JButton levelBTN = new BTNModel(Language.getString("btn.level.text"), 21);
    private final JButton paramBTN = new BTNModel(Language.getString("btn.settings.text"), 21);
    private final JButton achivBTN = new BTNModel(Language.getString("btn.achievements.text"), 21);
    private final JButton quitBTn = new BTNModel(Language.getString("btn.exit.text"), 21);
    private final MainMenuPanelController controller = new MainMenuPanelController(this);

    public MainMenuPanel() {
        init();
    }

    public JLabel getLogoLabel() {
        return LogoLabel;
    }

    public JButton getAchivBTN() {
        return achivBTN;
    }

    public JLabel getImageBG() {
        return imageBG;
    }

    public JButton getCampagneBTN() {
        return campagneBTN;
    }

    public JButton getLevelBTN() {
        return levelBTN;
    }

    public ImageIcon getLogo() {
        return logo;
    }

    public JButton getRestartBTN() {
        return restartBTN;
    }

    private JPanel getThis() {
        return this;
    }

    public JButton getParamBTN() {
        return paramBTN;
    }

    public JButton getQuitBTn() {
        return quitBTn;
    }

    @Override
    public Container getParents() {
        return null;
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.mainmenu.title");
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        imageBG.setLayout(new BorderLayout(0, 0));

        MainMenuPanelController.MouseInputs mouseController = controller.new MouseInputs();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(imageBG, background, getWidth(), getHeight());
                updateBackgroundImageWorker.execute();

                MainMenuPanelController.UpdateIcon updateLogoSize = new MainMenuPanelController((MainMenuPanel) getThis()).new UpdateIcon();
                updateLogoSize.execute();
            }
        });

        add(imageBG);

        JPanel mainMenuPanel = new JPanel();
        imageBG.add(mainMenuPanel, BorderLayout.CENTER);
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));

        mainMenuPanel.add(LogoLabel);

        LogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Component verticalGlue_0 = Box.createVerticalGlue();
        mainMenuPanel.add(verticalGlue_0);

        JPanel BTNPanel = new JPanel();
        BTNPanel.setOpaque(false);
        mainMenuPanel.add(BTNPanel);
        BTNPanel.setLayout(new BoxLayout(BTNPanel, BoxLayout.X_AXIS));

        Component horizontalGlue = Box.createHorizontalGlue();
        BTNPanel.add(horizontalGlue);


        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        BTNPanel.add(panel_1);
        panel_1.setMaximumSize(new Dimension(10000, 10000));
        panel_1.setLayout(new GridLayout(0, 1, 1, 10));


        if (MainMenu.Instance.getCampagne().getLevelNum() > 0) {

            JPanel panel2 = new JPanel();
            panel2.setOpaque(false);
            panel_1.add(panel2);
            panel2.setLayout(new GridLayout(0, 2, 10, 0));

            panel2.add(campagneBTN);

            panel2.add(restartBTN);
            restartBTN.addActionListener(mouseController);
        } else {
            panel_1.add(campagneBTN);
        }

        campagneBTN.addActionListener(mouseController);
        MainMenu.Instance.getRootPane().setDefaultButton(campagneBTN);

        panel_1.add(levelBTN);
        levelBTN.addActionListener(mouseController);

        panel_1.add(achivBTN);
        achivBTN.addActionListener(mouseController);


        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel_1.add(panel);
        panel.setLayout(new GridLayout(0, 2, 10, 0));

        panel.add(paramBTN);
        paramBTN.addActionListener(mouseController);

        panel.add(quitBTn);
        quitBTn.addActionListener(mouseController);

        Component horizontalGlue_1 = Box.createHorizontalGlue();
        BTNPanel.add(horizontalGlue_1);

        Component verticalGlue_1 = Box.createVerticalGlue();
        mainMenuPanel.add(verticalGlue_1);


    }
}
