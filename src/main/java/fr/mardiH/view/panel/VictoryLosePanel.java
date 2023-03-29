package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.VictoryLosePanelController;
import fr.mardiH.model.Enum.FileType;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VictoryLosePanel extends JPanel implements ViewPanels {

    private final VictoryLosePanelController controller = new VictoryLosePanelController(this);
    private final VictoryLosePanelController.MouseInputs controlMouse;
    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private JLabel endBTN;
    private JLabel retryBTN;
    private JLabel nextBTN;
    private final JLabel imageBG = new JLabel();
    private String levelName;
    private final boolean custom;
    private final boolean win;
    private final boolean fromMainMenu;
    private final int currentPrivateLevel;

    public VictoryLosePanel(boolean win, String levelName, boolean custom, boolean fromMainMenu, int currentPrivateLevel, long startTime) {
        this.win = win;
        this.levelName = levelName;
        this.fromMainMenu = fromMainMenu;
        controlMouse = controller.new MouseInputs(currentPrivateLevel,fromMainMenu);
        this.custom = custom;
        this.currentPrivateLevel = currentPrivateLevel;

        long playTime = System.currentTimeMillis() - startTime;
        MainMenu.Instance.getAchievements().getPlayTime().addCurrent(Double.parseDouble(new SimpleDateFormat("mm.ss").format(new Date(playTime))));

        init();
    }

    public ImageIcon getBackgroundIMG() {
        return background;
    }

    public String getLevelName() {
        return levelName;
    }

    public boolean isCustom() {
        return custom;
    }

    public JLabel getImageBG() {
        return imageBG;
    }

    public JLabel getEndBTN() {
        return endBTN;
    }

    public JLabel getRetryBTN() {
        return retryBTN;
    }

    public JLabel getNextBTN() {
        return nextBTN;
    }

    private void init() {

        addComponentListener(controller.new Resize(this));


        imageBG.setLayout(new BorderLayout(0, 0));
        add(imageBG);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        imageBG.add(panel_1);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

        Component glue_1 = Box.createGlue();
        panel_1.add(glue_1);

        JLabel lblNewLabel = new JLabel();
        if (win) {
            lblNewLabel.setText(Language.getString("menu.win.text"));
            lblNewLabel.setForeground(Color.ORANGE);
        } else {
            lblNewLabel.setText(Language.getString("menu.lose.text"));
            lblNewLabel.setForeground(Color.RED);
        }
        panel_1.add(lblNewLabel);
        lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 124));

        if (MainMenu.Instance.getCampagne().getLife() <= 0 && !win && !custom) {
            lblNewLabel.setText(Language.getString("menu.gameover.title"));
            JLabel tmp = new JLabel(Language.getString("menu.gameover.text"));
            tmp.setAlignmentX(Component.CENTER_ALIGNMENT);
            tmp.setForeground(Color.ORANGE);
            tmp.setFont(new Font("Arial", Font.PLAIN, 20));
            panel_1.add(tmp);
            MainMenu.Instance.sec.saveCrypt(FileType.campagne, true);
            MainMenu.Instance.getCampagne().setlife(5);
            MainMenu.setLife(5);
            levelName = "Campagne 0.yaml";
        }


        Component verticalStrut = Box.createVerticalStrut(20);
        panel_1.add(verticalStrut);

        JPanel panel_3 = new JPanel();
        panel_3.setOpaque(false);
        panel_1.add(panel_3);
        panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel_2 = new JPanel();
        panel_2.setOpaque(false);
        panel_3.add(panel_2);

        if (custom || !win || currentPrivateLevel == MainMenu.totalPrivateLevel || !fromMainMenu) {
            panel_2.setLayout(new GridLayout(0, 2, 50, 0));
        } else {
            panel_2.setLayout(new GridLayout(0, 3, 50, 0));
        }

        endBTN = new JLabel("");
        endBTN.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/cCancel.png")));
        endBTN.setName("Images/menu/cCancel.png");
        endBTN.setToolTipText(Language.getString("btn.quit.text"));
        endBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endBTN.addMouseListener(controlMouse);
        panel_2.add(endBTN);

        retryBTN = new JLabel("");
        retryBTN.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/cRefresh.png")));
        retryBTN.setName("Images/menu/cRefresh.png");
        retryBTN.setToolTipText(Language.getString("btn.retry.text"));
        retryBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        retryBTN.addMouseListener(controlMouse);
        panel_2.add(retryBTN);

        if (!custom && win && currentPrivateLevel <= MainMenu.Instance.getCampagne().getLevelNum() && currentPrivateLevel != MainMenu.totalPrivateLevel && fromMainMenu) {
            nextBTN = new JLabel("");
            nextBTN.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/cNext.png")));
            nextBTN.setName("Images/menu/cNext.png");
            nextBTN.setToolTipText(Language.getString("btn.next.text"));
            nextBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            nextBTN.addMouseListener(controlMouse);
            panel_2.add(nextBTN);
        }

        Component glue = Box.createGlue();
        panel_1.add(glue);


        if (win) {
            MainMenu.Instance.getSound().win();
        } else {
            MainMenu.Instance.getSound().lose();
        }

    }

    @Override
    public Container getParents() {
        return fromMainMenu ? new MainMenuPanel() : new LevelMenuPanel();
    }

    @Override
    public String getTitles() {
        return win ? "Tiny Tanks - " + Language.getString("menu.win.title") : "Tiny Tanks - " + Language.getString("menu.lose.title");
    }
}
