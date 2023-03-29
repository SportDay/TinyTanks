package fr.mardiH.view.panel;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import fr.mardiH.controller.panel.SettingsMenuController;
import fr.mardiH.model.BTNModel;
import fr.mardiH.model.GamePadByName;
import fr.mardiH.model.LocalByName;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.util.StringUtils;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.ui.CustomScrollBarUI;
import fr.mardiH.view.ui.CustomSliderUI;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import static fr.mardiH.Main.logger;

public class SettingsMenuPanel extends JPanel implements ViewPanels {

    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private final JButton effectBTN = new BTNModel(Language.getString("btn.musictest.effect.text"), 15);
    private final JButton musicBTN = new BTNModel(Language.getString("btn.musictest.music.text"), 15);
    private final JButton otherBTN = new BTNModel(Language.getString("btn.musictest.other.text"), 15);
    private final JButton creditsBTN = new BTNModel(Language.getString("btn.credits.text"), 15);
    private final JButton resetAchievBTN = new BTNModel(Language.getString("btn.deleteachievements.text"), 15);
    private final JButton openMapsFolder = new BTNModel(Language.getString("btn.openfolder.text"), 15);
    private final JButton saveBTN = new BTNModel(Language.getString("btn.save.text"), 15);
    private final JButton saveLangBTN = new BTNModel(Language.getString("btn.save.text"), 15);
    private final JButton updateBTN = new BTNModel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/cRefresh.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
    private JButton BTN_RIGHT;
    private JButton BTN_DOWN;
    private JButton BTN_LEFT;
    private JButton BTN_UP;
    private final JComboBox<GamePadByName> comboBox = new JComboBox<>();
    private final JComboBox<LocalByName> langComboBox = new JComboBox<>();
    private final GridBagLayout gridLayout = new GridBagLayout();
    private final JLabel msgLabel = new JLabel(Language.getString("label.askkey.text"));
    private final JSlider menuSlider = new JSlider();
    private final JSlider musicSlider = new JSlider();
    private final JSlider effectSlider = new JSlider();
    private final JPanel menuPanel = new JPanel();
    private final JPanel musicPanel = new JPanel();
    private final JPanel effectPanel = new JPanel();
    private final JPanel manettePanel = new JPanel();
    private final JPanel langPanel = new JPanel();
    private final JPanel moveBTNPanel = new JPanel();
    private final JLabel imageBG = new JLabel();
    private final JLabel backBTN = new JLabel();
    private final LinkedList<JLabel> tanksList = new LinkedList<>();
    private final LinkedList<Integer> keyList = new LinkedList<>();

    public SettingsMenuPanel() {
        init();
    }

    public JLabel getBackBTN() {
        return backBTN;
    }

    public LinkedList<JLabel> getTanksList() {
        return tanksList;
    }

    public JButton getOpenMapsFolder() {
        return openMapsFolder;
    }

    public JButton getEffectBTN() {
        return effectBTN;
    }

    public JComboBox<GamePadByName> getComboBox() {
        return comboBox;
    }

    public JButton getMusicBTN() {
        return musicBTN;
    }

    public JButton getOtherBTN() {
        return otherBTN;
    }

    public JPanel getMoveBTNPanel() {
        return moveBTNPanel;
    }

    public JButton getCreditsBTN() {
        return creditsBTN;
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    public JPanel getMusicPanel() {
        return musicPanel;
    }

    public GridBagLayout getGridLayout() {
        return gridLayout;
    }

    public JPanel getEffectPanel() {
        return effectPanel;
    }

    public JSlider getMenuSlider() {
        return menuSlider;
    }

    public JSlider getMusicSlider() {
        return musicSlider;
    }

    public JSlider getEffectSlider() {
        return effectSlider;
    }

    public JLabel getImageBG() {
        return imageBG;
    }

    public JPanel getManettePanel() {
        return manettePanel;
    }

    public LinkedList<Integer> getKeyList() {
        return keyList;
    }

    public JButton getSaveBTN() {
        return saveBTN;
    }

    public JButton getResetAchievBTN() {
        return resetAchievBTN;
    }

    public JButton getUpdateBTN() {
        return updateBTN;
    }

    public ImageIcon getBackgroundImg() {
        return background;
    }

    public JLabel getMsgLabel() {
        return msgLabel;
    }

    public JButton getBTN_RIGHT() {
        return BTN_RIGHT;
    }

    public JButton getBTN_DOWN() {
        return BTN_DOWN;
    }

    public JButton getBTN_LEFT() {
        return BTN_LEFT;
    }

    public JButton getBTN_UP() {
        return BTN_UP;
    }

    public JButton getSaveLangBTN() {
        return saveLangBTN;
    }

    public JComboBox<LocalByName> getLangComboBox() {
        return langComboBox;
    }

    public JPanel getLangPanel() {
        return langPanel;
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        imageBG.setLayout(new BorderLayout(0, 0));

        SettingsMenuController controller = new SettingsMenuController(this);
        SettingsMenuController.Action btnAction = controller.new Action();

        addComponentListener(controller.new Resize());
        addKeyListener(controller.new KeyAdapter());
        addKeyListener(MainMenu.controlKey);
        setFocusable(true);
        requestFocusInWindow();

        add(imageBG);


        JPanel panel = new JPanel();
        panel.setOpaque(false);
        imageBG.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel_1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(panel_1, BorderLayout.NORTH);

        backBTN.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/closeBtn.png"))).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        backBTN.addMouseListener(controller.new MouseInputs());
        backBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBTN.setToolTipText(Language.getString("btn.back.text"));
        panel_1.add(backBTN);

        JPanel panel_2 = new JPanel();
        panel_2.setOpaque(false);
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(15);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(new TitledBorder(new LineBorder(Color.white, 4, true), Language.getString("label.settings.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.WHITE));
        scrollPane.setViewportView(panel_2);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setOpaque(false);

        Component verticalGlue_1 = Box.createVerticalGlue();
        panel_2.add(verticalGlue_1);

        JPanel tanksPanel = new JPanel();
        tanksPanel.setMaximumSize(new Dimension(32767, 100));
        tanksPanel.setOpaque(false);
        tanksPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0)), Language.getString("label.tankchoice.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        panel_2.add(tanksPanel);
        tanksPanel.setLayout(new BoxLayout(tanksPanel, BoxLayout.X_AXIS));

        JPanel panel_9 = new JPanel();
        panel_9.setOpaque(false);
        tanksPanel.add(panel_9);
        panel_9.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));


        for (int i = 0; i < 4; i++) {
            JLabel tankIMG = new JLabel();
            if (MainMenu.Instance.getSettingsModel().getTankModel() == i) {
                tankIMG.setBorder(new LineBorder(Color.GREEN, 2));
            } else {
                tankIMG.setBorder(new LineBorder(Color.WHITE, 2));
            }

            tankIMG.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tank_" + i + ".png")).getImage().getScaledInstance(64, 64, Image.SCALE_DEFAULT)));
            tankIMG.setForeground(Color.WHITE);
            tankIMG.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tankIMG.setName(i + "");
            tankIMG.addMouseListener(controller.new MouseInputs(tankIMG));
            panel_9.add(tankIMG);
            tanksList.add(tankIMG);
        }

        JPanel panel_5 = new JPanel();
        panel_5.setOpaque(false);
        panel_5.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.soundvolume.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        panel_2.add(panel_5);
        panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.X_AXIS));

        Component horizontalGlue = Box.createHorizontalGlue();
        panel_5.add(horizontalGlue);

        JPanel panel_6 = new JPanel();
        panel_6.setOpaque(false);
        panel_5.add(panel_6);
        panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));

        effectPanel.setOpaque(false);
        panel_6.add(effectPanel);
        effectPanel.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.effect.text") + " 0%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        effectSlider.addChangeListener(controller.new SliderChange());
        effectSlider.setFocusable(false);
        effectSlider.setPaintLabels(true);
        effectSlider.setPaintTicks(true);
        effectSlider.setMinorTickSpacing(5);
        effectSlider.setMajorTickSpacing(25);
        effectSlider.setUI(new CustomSliderUI(effectSlider));
        effectSlider.setOpaque(false);
        effectPanel.setLayout(new BoxLayout(effectPanel, BoxLayout.X_AXIS));
        effectSlider.setValue(MainMenu.Instance.getSettingsModel().getEffectVolume());
        effectPanel.add(effectSlider);

        Component verticalStrut_1 = Box.createVerticalStrut(20);
        panel_6.add(verticalStrut_1);

        musicPanel.setOpaque(false);
        panel_6.add(musicPanel);
        musicPanel.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.music.text") + " 0%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.X_AXIS));

        musicSlider.addChangeListener(controller.new SliderChange());
        musicSlider.setOpaque(false);
        musicSlider.setFocusable(false);
        musicSlider.setPaintLabels(true);
        musicSlider.setPaintTicks(true);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setMajorTickSpacing(25);
        musicSlider.setUI(new CustomSliderUI(musicSlider));
        musicSlider.setValue(MainMenu.Instance.getSettingsModel().getMusicVolume());

        musicPanel.add(musicSlider);

        Component verticalStrut_2 = Box.createVerticalStrut(20);
        panel_6.add(verticalStrut_2);

        menuPanel.setOpaque(false);
        panel_6.add(menuPanel);
        menuPanel.setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.other.text") + " 0%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

        menuSlider.addChangeListener(controller.new SliderChange());
        menuSlider.setOpaque(false);
        menuSlider.setFocusable(false);
        menuSlider.setPaintLabels(true);
        menuSlider.setPaintTicks(true);
        menuSlider.setMinorTickSpacing(5);
        menuSlider.setMajorTickSpacing(25);
        menuSlider.setMajorTickSpacing(25);
        menuSlider.setUI(new CustomSliderUI(musicSlider));
        menuSlider.setValue(MainMenu.Instance.getSettingsModel().getMenuVolume());
        menuPanel.add(menuSlider);

        Component horizontalGlue_1 = Box.createHorizontalGlue();
        panel_5.add(horizontalGlue_1);

        JPanel panel_4 = new JPanel();
        panel_4.setOpaque(false);
        panel_4.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.soundtest.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        panel_2.add(panel_4);
        panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

        Component horizontalGlue_5 = Box.createHorizontalGlue();
        panel_4.add(horizontalGlue_5);

        JPanel panel_7 = new JPanel();
        panel_7.setOpaque(false);
        panel_4.add(panel_7);
        panel_7.setMaximumSize(new Dimension(32767, 5000));
        panel_7.setLayout(new GridLayout(0, 3, 10, 0));

        effectBTN.addActionListener(btnAction);
        effectBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel_7.add(effectBTN);

        musicBTN.addActionListener(btnAction);
        musicBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel_7.add(musicBTN);

        otherBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        otherBTN.addActionListener(btnAction);
        panel_7.add(otherBTN);

        Component horizontalGlue_4 = Box.createHorizontalGlue();
        panel_4.add(horizontalGlue_4);

        JPanel panel_10 = new JPanel();
        panel_10.setOpaque(false);
        panel_10.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.other.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        panel_2.add(panel_10);
        panel_10.setLayout(new BoxLayout(panel_10, BoxLayout.X_AXIS));

        Component horizontalGlue_8 = Box.createHorizontalGlue();
        panel_10.add(horizontalGlue_8);

        JPanel panel_11 = new JPanel();
        panel_11.setOpaque(false);
        panel_10.add(panel_11);
        panel_11.setMaximumSize(new Dimension(32767, 5000));
        panel_11.setLayout(new GridLayout(0, 3, 10, 0));

        openMapsFolder.setAlignmentX(Component.CENTER_ALIGNMENT);
        openMapsFolder.addActionListener(btnAction);
        panel_11.add(openMapsFolder);

        creditsBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditsBTN.addActionListener(btnAction);
        panel_11.add(creditsBTN);

        resetAchievBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetAchievBTN.addActionListener(btnAction);
        panel_11.add(resetAchievBTN);

        Component horizontalGlue_9 = Box.createHorizontalGlue();
        panel_10.add(horizontalGlue_9);


        JPanel panel_17 = new JPanel();
        panel_17.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.changekey.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        panel_17.setOpaque(false);
        panel_2.add(panel_17);

        JPanel panel_19 = new JPanel();
        panel_19.setOpaque(false);
        panel_17.add(panel_19);
        panel_19.setLayout(new BoxLayout(panel_19, BoxLayout.Y_AXIS));

        msgLabel.setForeground(Color.ORANGE);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        msgLabel.setVisible(false);
        panel_19.add(msgLabel);

        Component verticalStrut_4 = Box.createVerticalStrut(10);
        panel_19.add(verticalStrut_4);

        moveBTNPanel.setOpaque(false);
        panel_19.add(moveBTNPanel);

        int caseSize = 48;
        gridLayout.columnWidths = new int[]{caseSize, 5, caseSize, 5, caseSize};
        gridLayout.rowHeights = new int[]{caseSize, 5, caseSize};
        moveBTNPanel.setLayout(gridLayout);


        BTN_UP = new BTNModel(KeyEvent.getKeyText(MainMenu.Instance.getSettingsModel().getBtnUP()), 15);
        BTN_UP.setActionCommand(MainMenu.Instance.getSettingsModel().getBtnUP() + "");
        keyList.add(MainMenu.Instance.getSettingsModel().getBtnUP());
        BTN_UP.addActionListener(btnAction);
        GridBagConstraints gbc_BTN_UP = new GridBagConstraints();
        gbc_BTN_UP.fill = GridBagConstraints.BOTH;
        gbc_BTN_UP.gridx = 2;
        gbc_BTN_UP.gridy = 0;
        moveBTNPanel.add(BTN_UP, gbc_BTN_UP);

        BTN_LEFT = new BTNModel(KeyEvent.getKeyText(MainMenu.Instance.getSettingsModel().getBtnLEFT()), 15);
        BTN_LEFT.setActionCommand(MainMenu.Instance.getSettingsModel().getBtnLEFT() + "");
        keyList.add(MainMenu.Instance.getSettingsModel().getBtnLEFT());
        BTN_LEFT.addActionListener(btnAction);
        GridBagConstraints gbc_BTN_LEFT = new GridBagConstraints();
        gbc_BTN_LEFT.fill = GridBagConstraints.BOTH;
        gbc_BTN_LEFT.gridx = 0;
        gbc_BTN_LEFT.gridy = 2;
        moveBTNPanel.add(BTN_LEFT, gbc_BTN_LEFT);


        BTN_DOWN = new BTNModel(KeyEvent.getKeyText(MainMenu.Instance.getSettingsModel().getBtnDOWN()), 15);
        BTN_DOWN.setActionCommand(MainMenu.Instance.getSettingsModel().getBtnDOWN() + "");
        keyList.add(MainMenu.Instance.getSettingsModel().getBtnDOWN());
        BTN_DOWN.addActionListener(btnAction);
        GridBagConstraints gbc_BTN_DOWN = new GridBagConstraints();
        gbc_BTN_DOWN.fill = GridBagConstraints.BOTH;
        gbc_BTN_DOWN.gridx = 2;
        gbc_BTN_DOWN.gridy = 2;
        moveBTNPanel.add(BTN_DOWN, gbc_BTN_DOWN);

        BTN_RIGHT = new BTNModel(KeyEvent.getKeyText(MainMenu.Instance.getSettingsModel().getBtnRIGHT()), 15);
        BTN_RIGHT.setActionCommand(MainMenu.Instance.getSettingsModel().getBtnRIGHT() + "");
        keyList.add(MainMenu.Instance.getSettingsModel().getBtnRIGHT());
        BTN_RIGHT.addActionListener(btnAction);
        GridBagConstraints gbc_BTN_RIGHT = new GridBagConstraints();
        gbc_BTN_RIGHT.fill = GridBagConstraints.BOTH;
        gbc_BTN_RIGHT.gridx = 4;
        gbc_BTN_RIGHT.gridy = 2;
        moveBTNPanel.add(BTN_RIGHT, gbc_BTN_RIGHT);

        if (MainMenu.gamePadInstance == null || !MainMenu.gamePadInstance.isConnected()) {
            manettePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.joystick.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        } else if (MainMenu.gamePadInstance.isConnected()) {
            try {
                manettePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.joystick.text") + ": " + MainMenu.gamePadInstance.getName() + " (" + MainMenu.gamePadInstance.getIndex() + ")", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
            } catch (ControllerUnpluggedException e) {
                e.printStackTrace();
            }
        }
        manettePanel.setOpaque(false);
        panel_2.add(manettePanel);
        manettePanel.setLayout(new BoxLayout(manettePanel, BoxLayout.X_AXIS));

        Component horizontalGlue_3 = Box.createHorizontalGlue();
        manettePanel.add(horizontalGlue_3);

        JPanel panel_13 = new JPanel();
        panel_13.setOpaque(false);
        manettePanel.add(panel_13);
        panel_13.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel_14 = new JPanel();
        panel_13.add(panel_14);
        panel_14.setOpaque(false);
        panel_14.setLayout(new BoxLayout(panel_14, BoxLayout.Y_AXIS));

        JPanel panel_15 = new JPanel();
        panel_15.setOpaque(false);
        panel_14.add(panel_15);
        GridBagLayout gbl_panel_15 = new GridBagLayout();
        gbl_panel_15.columnWidths = new int[]{200};
        gbl_panel_15.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gbl_panel_15.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        panel_15.setLayout(gbl_panel_15);


        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.fill = GridBagConstraints.BOTH;
        gbc_comboBox.insets = new Insets(0, 0, 0, 5);
        gbc_comboBox.gridx = 0;
        gbc_comboBox.gridy = 0;
        panel_15.add(comboBox, gbc_comboBox);

        MainMenu.gamePadManagerInstance.update();
        if (MainMenu.gamePadManagerInstance.getNumControllers() != 0) {
            for (int i = 0; i < MainMenu.gamePadManagerInstance.getNumControllers(); i++) {
                ControllerIndex gamePad = MainMenu.gamePadManagerInstance.getControllerIndex(i);
                try {
                    comboBox.addItem(new GamePadByName(gamePad, gamePad.getName(), i));
                } catch (ControllerUnpluggedException e) {
                    e.printStackTrace();
                }
            }
            if (MainMenu.gamePadInstance == null) {
                MainMenu.gamePadInstance = ((GamePadByName) comboBox.getSelectedItem()).getController();
            } else if (MainMenu.gamePadInstance.isConnected()) {
                comboBox.setSelectedIndex(MainMenu.gamePadInstance.getIndex());
            }
        } else {
            MainMenu.gamePadInstance = null;
            comboBox.setModel(new DefaultComboBoxModel(new String[]{Language.getString("label.nojoystick.text")}));
            saveBTN.setEnabled(false);
            comboBox.setEnabled(false);
        }
        comboBox.setFocusable(false);
        comboBox.setFont(new Font("Arial", Font.BOLD, 12));


        GridBagConstraints gbc_updateBTN = new GridBagConstraints();
        gbc_updateBTN.fill = GridBagConstraints.BOTH;
        gbc_updateBTN.insets = new Insets(0, 5, 0, 0);
        gbc_updateBTN.gridx = 1;
        gbc_updateBTN.gridy = 0;

        updateBTN.setToolTipText(Language.getString("btn.reload.hover"));
        updateBTN.addActionListener(btnAction);
        panel_15.add(updateBTN, gbc_updateBTN);

        Component verticalStrut_3 = Box.createVerticalStrut(10);
        panel_14.add(verticalStrut_3);

        JPanel panel_12 = new JPanel();
        panel_12.setOpaque(false);
        panel_14.add(panel_12);
        panel_12.setLayout(new BoxLayout(panel_12, BoxLayout.X_AXIS));

        Component horizontalGlue_6 = Box.createHorizontalGlue();
        panel_12.add(horizontalGlue_6);

        JPanel panel_16 = new JPanel();
        panel_16.setOpaque(false);
        panel_12.add(panel_16);
        panel_16.setLayout(new GridLayout(0, 1, 0, 0));

        saveBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBTN.addActionListener(btnAction);
        panel_16.add(saveBTN);

        Component horizontalGlue_7 = Box.createHorizontalGlue();
        panel_12.add(horizontalGlue_7);

        Component horizontalGlue_2 = Box.createHorizontalGlue();
        manettePanel.add(horizontalGlue_2);


        langPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("text.langchange.title"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));

        langPanel.setOpaque(false);
        panel_2.add(langPanel);
        langPanel.setLayout(new BoxLayout(langPanel, BoxLayout.X_AXIS));

        Component horizontalGlue_33 = Box.createHorizontalGlue();
        langPanel.add(horizontalGlue_33);

        JPanel panel_133 = new JPanel();
        panel_133.setOpaque(false);
        langPanel.add(panel_133);
        panel_133.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel_144 = new JPanel();
        panel_133.add(panel_144);
        panel_144.setOpaque(false);
        panel_144.setLayout(new BoxLayout(panel_144, BoxLayout.Y_AXIS));

        JPanel panel_155 = new JPanel();
        panel_155.setOpaque(false);
        panel_144.add(panel_155);
        panel_155.setLayout(new GridLayout(0, 1, 0, 0));


        ClassLoader cl = getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:/language/*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Resource resource : resources) {
            if (resource.getFilename().contains("lang_") && resource.getFilename().endsWith(".properties")) {
                Locale lang = Locale.forLanguageTag(StringUtils.removeExtension(resource.getFilename(), "", ".properties", "lang_"));
                LocalByName local = new LocalByName(lang);
                langComboBox.addItem(local);
                if (lang.equals(MainMenu.Instance.getSettingsModel().getLang())) {
                    logger.info("setLang");
                    langComboBox.setSelectedItem(local);
                }
            }
        }
        langComboBox.setFocusable(false);
        panel_155.add(langComboBox);

        langComboBox.setFont(new Font("Arial", Font.BOLD, 12));

        Component verticalStrut_33 = Box.createVerticalStrut(10);
        panel_144.add(verticalStrut_33);

        JPanel panel_122 = new JPanel();
        panel_122.setOpaque(false);
        panel_144.add(panel_122);
        panel_122.setLayout(new BoxLayout(panel_122, BoxLayout.X_AXIS));

        Component horizontalGlue_66 = Box.createHorizontalGlue();
        panel_122.add(horizontalGlue_66);

        JPanel panel_166 = new JPanel();
        panel_166.setOpaque(false);
        panel_122.add(panel_166);
        panel_166.setLayout(new GridLayout(0, 1, 0, 0));

        saveLangBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveLangBTN.addActionListener(btnAction);
        panel_166.add(saveLangBTN);

        Component horizontalGlue_77 = Box.createHorizontalGlue();
        panel_122.add(horizontalGlue_77);

        Component horizontalGlue_22 = Box.createHorizontalGlue();
        langPanel.add(horizontalGlue_22);


        Component verticalGlue = Box.createVerticalGlue();
        panel_2.add(verticalGlue);

        Component verticalStrut = Box.createVerticalStrut(20);
        panel_2.add(verticalStrut);

    }

    @Override
    public Container getParents() {
        return new MainMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.settings.title");
    }
}