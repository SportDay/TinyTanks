package fr.mardiH.view;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import fr.mardiH.controller.MainMenuController;
import fr.mardiH.model.Achievements;
import fr.mardiH.model.Campagne;
import fr.mardiH.model.Enum.BonusType;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.Enum.FileType;
import fr.mardiH.model.SettingsModel;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.util.Security;
import fr.mardiH.util.Sound;
import fr.mardiH.util.StringUtils;
import fr.mardiH.view.panel.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import static fr.mardiH.Main.logger;

public class MainMenu extends JFrame {

    public final static ControllerManager gamePadManagerInstance = new ControllerManager();
    public static MainMenuController.Key controlKey;
    public static ControllerIndex gamePadInstance = null;
    public static boolean tir = false;
    public static MainMenu Instance;
    public static ArrayList<Timer> timerList;
    public static int totalPrivateLevel = 0;
    private static int tankType = 1;
    public final Sound Sound = new Sound(this);
    public final Security sec = new Security();
    private final int minWidth = 900, minHeight = 800;
    private final MainMenuController controller = new MainMenuController();
    private SettingsModel settingsModel;
    private Campagne campagne;
    private Achievements achievements;
    private int life = 5;

    public MainMenu() {
        gamePadManagerInstance.initSDLGamepad();
        if (gamePadManagerInstance.getNumControllers() >= 1) {
            gamePadInstance = gamePadManagerInstance.getControllerIndex(0);
        }
        timerList = new ArrayList<>();
        Instance = this;
        controlKey = controller.new Key();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tiny Tanks");
        setBounds(100, 100, 0, 0);
        setMinimumSize(new Dimension(minWidth, minHeight));
        setPreferredSize(new Dimension(minWidth, minHeight));
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("Images/menu/icon2.png")).getImage());

        sec.openSerial(FileType.settings);
        sec.openCrypt(FileType.campagne);
        sec.openCrypt(FileType.achievements);

        if (settingsModel == null) {
            sec.saveFile(FileType.settings, true);
        }
        if (campagne == null) {
            sec.saveCrypt(FileType.campagne, true);
        }
        if (achievements == null) {
            sec.saveCrypt(FileType.achievements, true);
        }

        ClassLoader cl = getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:/Maps/*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalPrivateLevel = 0;
        for (Resource resource : resources) {
            if (resource.getFilename().endsWith(".yaml") || resource.getFilename().endsWith(".yml")) {
                totalPrivateLevel++;
            }
        }
        MainMenu.totalPrivateLevel = totalPrivateLevel;

        addKeyListener(controlKey);
        setFocusable(true);
        requestFocusInWindow();

        if (settingsModel != null) {
            settingsModel.addObserver(controller.new MainMenuObserver(FileType.settings));
            tankType = settingsModel.getTankModel();
        }
        if (campagne != null) {
            campagne.addObserver(controller.new MainMenuObserver(FileType.campagne));
            life = campagne.getLife();
        }
        if (achievements != null) {
            achievements.addObserver(controller.new MainMenuObserver(FileType.achievements));
        }

        if (!CaseType.enumCorrect() || !BonusType.enumCorrect()) {
            System.exit(-1);
        }

        setMainMenu();
    }

    public static int getTankType() {
        return tankType;
    }

    public static void setTankType(int tankType) {
        MainMenu.tankType = tankType;
    }

    public static void stopTimer() {

        if (timerList != null) {
            for (Timer timer : timerList) {
                timer.setRepeats(false);
                timer.stop();
            }
        } else {
            logger.debug("Timers NUll");
        }
        timerList = new ArrayList<>();
        System.gc();


        if (gamePadInstance != null && gamePadInstance.isConnected()) {
            try {
                gamePadInstance.doVibration(0f, 0f, 2);
            } catch (ControllerUnpluggedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void increaseLife() {
        MainMenu.Instance.life++;
    }

    public static void decreaseLife() {
        MainMenu.Instance.life--;
    }

    public static void setLife(int life) {
        MainMenu.Instance.life = life;
    }

    public static void updateLife() {
        MainMenu.Instance.getCampagne().setlife(MainMenu.Instance.life);
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public fr.mardiH.util.Sound getSound() {
        return Sound;
    }

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    public void setSettingsModel(SettingsModel settingsModel) {
        this.settingsModel = settingsModel;
    }

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }

    public Campagne getCampagne() {
        return campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }

    public void setPreviusPanel(Container comp) {
        stopTimer();
        if (comp instanceof ViewPanels) {
            repaint();
            getContentPane().removeAll();

            if ((MainMenu.Instance.getContentPane() instanceof LevelMenuPanel && comp instanceof LevelMenuPanel) ||
                    (MainMenu.Instance.getContentPane() instanceof LevelMenuPanel && comp instanceof MainMenuPanel)) {
                if (!MainMenu.Instance.getSound().getClip().isRunning()) {
                    MainMenu.Instance.getSound().menu(true);
                }
            } else if (!(MainMenu.Instance.getContentPane() instanceof LevelMenuPanel) && !(MainMenu.Instance.getContentPane() instanceof AchievementsMenuPanel) &&
                    !(MainMenu.Instance.getContentPane() instanceof CreateMenuPanel)) {
                if (MainMenu.Instance.getSound().getClip() != null) {
                    MainMenu.Instance.getSound().getClip().stop();
                }
                MainMenu.Instance.getSound().menu(true);
            }

            setTitle(((ViewPanels) comp).getTitles());
            MainMenu.tir = false;

            controlKey.setCurrent(comp);
            setContentPane(comp);

            getContentPane().revalidate();
            getContentPane().repaint();
        } else {
            logger.debug("NO ViewPanels");
        }
    }

    public void setMainMenu() {
        stopTimer();
        repaint();
        getContentPane().removeAll();
        setTitle("Tiny Tanks - " + Language.getString("menu.mainmenu.title"));

        if (MainMenu.Instance.getSound().getClip() == null || !MainMenu.Instance.getSound().getClip().isRunning()) {
            MainMenu.Instance.getSound().menu(true);
        }

        JPanel mainMenuPanel = new MainMenuPanel();
        setContentPane(mainMenuPanel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setLevelPanel() {
        stopTimer();
        MainMenu.tir = false;

        repaint();
        getContentPane().removeAll();

        if (!(MainMenu.Instance.getContentPane() instanceof MainMenuPanel) && !(MainMenu.Instance.getContentPane() instanceof CreateMenuPanel)) {
            if (MainMenu.Instance.getSound().getClip() != null) {
                MainMenu.Instance.getSound().getClip().stop();
                MainMenu.Instance.getSound().getClip().close();
            }
            MainMenu.Instance.getSound().menu(true);
        }

        setTitle("Tiny Tanks - " + Language.getString("menu.choicelevel.title"));

        JPanel panel = new LevelMenuPanel();
        setContentPane(panel);
        controlKey.setCurrent(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setCreatePanel() {

        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.editor.title"));

        JPanel panel = new CreateMenuPanel();
        controlKey.setCurrent(panel);
        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setCustomCreatePanel(String path) {

        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.editor.title"));

        JPanel panel = new CreateMenuPanel(path);
        controlKey.setCurrent(panel);
        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setSettingsPanel() {
        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.settings.title"));

        if (MainMenu.Instance.getSound().getClip() != null) {
            MainMenu.Instance.getSound().pause();
        }

        JPanel panel = new SettingsMenuPanel();
        controlKey.setCurrent(panel);
        setContentPane(panel);
        panel.setFocusable(true);
        panel.requestFocusInWindow();

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setAchivementPanel() {
        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.achi.title"));

        JPanel panel = new AchievementsMenuPanel();
        controlKey.setCurrent(panel);

        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setVictoryLosePanel(boolean win, String name, boolean custom, boolean fromMainMenu, int currentPrivateLevel, long startTime) {
        stopTimer();

        repaint();
        getContentPane().removeAll();

        if (win) {
            setTitle("Tiny Tanks - " + Language.getString("menu.win.title"));
        } else {
            setTitle("Tiny Tanks - " + Language.getString("menu.lose.title"));
        }
        if (MainMenu.Instance.getSound().getClip() != null) {
            MainMenu.Instance.getSound().getClip().stop();
            MainMenu.Instance.getSound().getClip().close();
        }

        JPanel panel = new VictoryLosePanel(win, name, custom, fromMainMenu, currentPrivateLevel, startTime);
        controlKey.setCurrent(panel);

        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setLevelPanel(String name, boolean custom, boolean fromMainMenu) {
        stopTimer();
        repaint();
        getContentPane().removeAll();
        String title = StringUtils.removeExtension(name, " ", ".yml", ".yaml");
        if (!custom) {
            title = title.replaceAll(";", " ");
        }
        setTitle("Tiny Tanks - " + Language.getString("menu.ingame.title") + " " + title);

        LinkedList<Ia> ia = new LinkedList<Ia>();

        logger.debug(name + " " + title);
        logger.info("Life: " + campagne.getLife());
        if (MainMenu.Instance.getSound().getClip() != null) {
            MainMenu.Instance.getSound().pause();
        }

        GameView gv = new GameView(name, custom, fromMainMenu);
        setContentPane(gv);
        gv.setFocusable(true);
        gv.requestFocusInWindow();
        // panel.setLayout(null);
        // getContentPane().add(tankView);
        //IMPORTANT intitialisation à faire avant création du controller

        getContentPane().revalidate();
        getContentPane().repaint();


        controlKey.setCurrent(gv);

    }

    public void setCreditsPanel() {
        stopTimer();

        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.credits.title"));

        JPanel panel = new CreditsPanel();
        controlKey.setCurrent(panel);

        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

    public void setEndPanel() {
        stopTimer();

        repaint();
        getContentPane().removeAll();

        setTitle("Tiny Tanks - " + Language.getString("menu.end.title"));

        JPanel panel = new EndPanel();
        controlKey.setCurrent(panel);

        setContentPane(panel);

        getContentPane().revalidate();
        getContentPane().repaint();
    }

}

