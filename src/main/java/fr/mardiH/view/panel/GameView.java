package fr.mardiH.view.panel;

import fr.mardiH.controller.VuePlateauController;
import fr.mardiH.controller.jeux.BossControlleur;
import fr.mardiH.controller.jeux.BotControlleur;
import fr.mardiH.controller.jeux.ControllerManette;
import fr.mardiH.controller.jeux.JoueurController;
import fr.mardiH.controller.panel.*;
import fr.mardiH.model.MissileModel;
import fr.mardiH.model.Tank.IA.Bot;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.TankModel;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.StringUtils;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static fr.mardiH.Main.logger;

public class GameView extends JLayeredPane implements ViewPanels {
    public static BarLifeBoss barLifeBoss;
    public static short vies = 3;
    private final int DELAY = 10;
    private final String levelName;
    private final boolean custom;
    private final GameViewController controller;
    private final GameViewController.ActionCont coAction;
    private final long startTime = System.currentTimeMillis();
    private final boolean fromMainMenu;
    VuePlateau plateau;
    TankView tv;
    MissileView missileView;
    BombView bombView;
    AirplaneView airplaneView;
    BossLifeView bosslifeView;
    LinkedList<Explosion> explosions = new LinkedList<Explosion>();
    int currentPrivateLevel = 0;
    private final Timer timer;
    private final ControllerManette gamePadController;
    private final JPanel loadingPanel;
    private final JoueurController tc;

    public GameView(String levelName, boolean custom, boolean fromMainMenu) {

        this.levelName = levelName;
        this.custom = custom;
        if (!custom) {
            String[] tmp = StringUtils.removeExtension(levelName, "", ".yml", ".yaml").split(" ");
            currentPrivateLevel = Integer.parseInt(tmp[1]);
        }
        plateau = new VuePlateau(levelName, custom);
        plateau.init();
        // Musique du niveau
        MainMenu.Instance.getSound().level(plateau.getPlat().getBoss());

        TankView tv = new TankView(plateau.getPlat().getTank(), plateau.getPlat().getIa());
        this.tv = tv;
        this.missileView = new MissileView(plateau.getPlat().getMissile());
        this.bombView = new BombView();
        this.airplaneView = new AirplaneView();
        this.bosslifeView = new BossLifeView();
        barLifeBoss = new BarLifeBoss(plateau.getPlat().getModeleBoss());
        AirPlaneController airplaneController = new AirPlaneController(airplaneView);
        BossLifeController bossLifeController = new BossLifeController(bosslifeView);
        BarLifeBossController barLifeBossController = new BarLifeBossController(barLifeBoss);
        Timer tm = new Timer(20, null);
        MainMenu.timerList.add(tm);


        tc = new JoueurController(this, plateau.getPlat(), plateau.getPlat().getTank());
        JoueurController.KeyAdapter tcKey = tc.new KeyAdapter();
        gamePadController = new ControllerManette(this, plateau.getPlat(), plateau.getPlat().getTank());
        ControllerManette.StickListener stick = gamePadController.new StickListener(tm);
        tm.addActionListener(stick);
        tm.start();
        plateau.getPlat().getTank().setController(tc);
        plateau.getPlat().getTank().setGamePadTimer(tm);
        plateau.getPlat().getTank().setGamePadController(gamePadController);
        for (TankModel tank : plateau.getPlat().getIa()) {
            if (tank instanceof Bot) {
                tank.setController(new BotControlleur(this, this.plateau.getPlat(), tank));
            } else {
                tank.setController(new BossControlleur(this, this.plateau.getPlat(), tank));
            }
        }
        this.addMouseListener(tc.new MouseAdapter());
        this.addMouseMotionListener(tc.new MouseAdapter());
        this.addKeyListener(tcKey);
        this.addKeyListener(MainMenu.controlKey);
        this.addComponentListener(new VuePlateauController().new resize(plateau));
        this.addComponentListener(tc);
        this.addComponentListener(airplaneController.new resizeController());
        this.addComponentListener(bossLifeController.new resizeController());
        this.addComponentListener(barLifeBossController.new resizeController());
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.fromMainMenu = fromMainMenu;
        setLayout(null);

        loadingPanel = new LoadingPanel();
        loadingPanel.setBounds(plateau.getBounds());
        LoadingPanelController controller = new LoadingPanelController(loadingPanel);
        addComponentListener(controller.new Resize(plateau));

        add(plateau);
        add(tv);
        add(missileView);
        add(bombView);
        add(airplaneView);
        add(loadingPanel);
        add(bosslifeView);
        add(barLifeBoss);

        setLayer(plateau, 1);
        setLayer(tv, 2);
        setLayer(missileView, 3);
        setLayer(bombView, 4);
        setLayer(airplaneView, 5);
        if (plateau.getPlat().getBoss()) {
            setLayer(barLifeBoss, 6);
            setLayer(bosslifeView, 7);
        }
        setLayer(loadingPanel, 8);
        ajoutMissile();


        this.controller = new GameViewController(this);
        logger.info("FromMainMenu GameView: " + fromMainMenu);

        this.coAction = this.controller.new ActionCont(levelName, custom, fromMainMenu, currentPrivateLevel, startTime);
        timer = new Timer(DELAY, coAction);
        timer.setInitialDelay(1);
        MainMenu.timerList.add(timer);
        timer.start();
    }


    public void updateMissiles() { //Mise à jour de l'affichage de tout les missiles
        LinkedList<MissileModel> missiles = missileView.missiles;
        for (int i = 0; i < missiles.size(); i++) { // For qui prend les vues une par une
            MissileModel m = missiles.get(i);
            if (!m.isVisible()) {
                // Ajoute une explosion quand les missiles disparaissent
                Explosion e = new Explosion((int) m.getEndX(), (int) m.getEndY());
                explosions.add(e);
                add(e);
                setLayer(e, 3);

                missiles.get(i).getTimer().stop();
                missiles.remove(i);
            }
        }
        // Fonction qui met à j les coordonnées des missiles
        missileView.updateMissiles();
        repaint();
    }


    public void ajoutMissile() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Ia ia : plateau.plat.getIa()) {
                        ia.setBalles(3);
                    }
                    plateau.tank.setBalles(3);
                    try {
                        Thread.sleep(1750);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }


    public ControllerManette getGamePadController() {
        return gamePadController;
    }

    public TankView getTv() {
        return tv;
    }

    public MissileView getMissileView() {
        return missileView;
    }

    public BombView getBombView() {
        return bombView;
    }

    @Override
    public Container getParents() {
        return fromMainMenu ? new MainMenuPanel() : new LevelMenuPanel();
    }

    @Override
    public String getTitles() {
        return "";
    }

    public VuePlateau getPlateau() {
        return plateau;
    }

    public JPanel getLoadingPanel() {
        return loadingPanel;
    }

    public LinkedList<Explosion> getExplosions() {
        return explosions;
    }

    public AirplaneView getAirplaneView() {
        return airplaneView;
    }

    public BossLifeView getBossLifeView() {
        return bosslifeView;
    }

}
