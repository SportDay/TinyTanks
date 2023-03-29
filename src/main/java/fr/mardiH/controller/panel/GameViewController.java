package fr.mardiH.controller.panel;


import fr.mardiH.controller.jeux.IaControlleur;
import fr.mardiH.model.*;
import fr.mardiH.model.Tank.IA.Boss;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.Explosion;
import fr.mardiH.view.panel.GameView;
import fr.mardiH.view.panel.Smoke;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import static fr.mardiH.Main.logger;

public class GameViewController {

    private final GameView source;

    public GameViewController(GameView source) {
        this.source = source;
    }

    public double orientationRaid(Coordonnees c1, Coordonnees c2) {
        double x, y;
        x = c2.getX();
        y = c1.getY();
        double distAdjacent = x - c1.getX();
        double distHypot = Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
        double angle = Math.acos(distAdjacent / distHypot);
        if (c1.getY() < c2.getY()) angle = -angle;

        return angle;


    }

    public class ActionCont implements ActionListener {
        private final int NOMBRE_BOMBE_RAID = 5;
        public boolean raid = false;
        int bombExplosionCount = 0;
        private int count = 0;
        private final String levelName;
        private final boolean custom;
        private final boolean fromMainMenu;
        private final int currentPrivateLevel;
        private final long startTime;
        private long gameTime = 0;
        private boolean raidEnCours = false;


        public ActionCont(String levelName, boolean custom, boolean fromMainMenu, int currentPrivateLevel, long startTime) {
            this.levelName = levelName;
            this.custom = custom;
            this.fromMainMenu = fromMainMenu;
            this.currentPrivateLevel = currentPrivateLevel;
            this.startTime = startTime;
            raid = source.getPlateau().getPlat().isRaid();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (count == 3) {
                source.remove(source.getLoadingPanel());
                MainMenu.tir = true;
            }
            if (count >= 4) {
                step();
                checkCollisionMissile();
                checkCollisionTankMissile();
                source.updateMissiles();
                updateSmokes();
                updateExplosions();
            }
            count++;
        }

        private void step() { // Toutes les actions qui doivent être appelées de manière réccurentes
            for (Ia ia : source.getPlateau().getPlat().getIa()) {
                ((IaControlleur) ia.controller).nextMove();
            }
            source.getTv().updateSpeed();
            gameTime = (System.currentTimeMillis() - startTime) / 1000;
            if (gameTime > 10 && raid && !raidEnCours) {
                randomBomb();
            }
            updateBombs();
            source.getBombView().repaint();
            source.getBombView().repaint();
            source.getTv().updateCanon();
            source.getAirplaneView().repaint();
            source.getBossLifeView().repaint();
            source.repaint();
            source.revalidate();
        }

        public void updateBombs() { // Mise à jour des état des bombes
            LinkedList<Bomb> delete = new LinkedList<Bomb>();
            for (Bomb b : source.getBombView().getBombs()) {
                if (b.chute()) {
                    explosion(b.getCoordonnees().getX(), b.getCoordonnees().getY(), 34);
                    Explosion e = new Explosion((int) b.getCoordonnees().getX(), (int) b.getCoordonnees().getY());
                    source.getExplosions().add(e);
                    source.add(e);
                    source.setLayer(e, 3);
                    delete.add(b);
                }
            }
            source.getBombView().getBombs().removeAll(delete);
        }

        public void randomBomb() { // Fonction qui décide aléatoirement si un raid doit être déclenché
            int r = new Random().nextInt(4000);
            if (r < 1) {
                MainMenu.Instance.getSound().raid();
                raidAerien();
            }

        }

        public Coordonnees generationCoordonnesBomb() { // Génération de bombe sur une case libre aléatoire
            LinkedList<Coordonnees> casesLibres = source.getPlateau().getPlat().listePasObstacle();
            if (casesLibres.size() > 0) {
                int r = new Random().nextInt(casesLibres.size());
                Coordonnees c = casesLibres.get(r);

                return c;
            }
            return null;
        }

        //Déclenchement d'une pluie de bombes sur le terrain
        public void raidAerien() {
            raidEnCours = true;
            PlateauModel p = source.getPlateau().getPlat();
            Coordonnees cj = p.getTank().getTank().getCentre();

            //Sélectionne une case libre à minimum 10 cases du joueur de manière aléatoire
            Coordonnees c = generationCoordonnesBomb();

            if (c != null) {
                //Lancement
                source.getAirplaneView().setEnCours(true);
                source.getAirplaneView().setOrientation(orientationRaid(c, cj));
                source.getAirplaneView().repaint();

                //Le but est qu'à partir de la coordonnée "c" (x,y) on fasse tomber 5 bombes en direction du joueur, la dernière étant à la position exacte du joueur lors du déclenchement
                double stepX = (cj.getX() - c.getX()) / (NOMBRE_BOMBE_RAID - 1);
                double stepY = (cj.getY() - c.getY()) / (NOMBRE_BOMBE_RAID - 1);
                //Coordoonnes pour l'affichage de l'ombre de l'avion
                double xDepart = c.getX();
                double yDepart = c.getY();
                int dep = 50;
                while (!((yDepart < 0 - dep || yDepart > 544 + dep) || (xDepart < 0 - dep || xDepart > 782 + dep))) {
                    xDepart -= stepX;
                    yDepart -= stepY;
                }

                source.getAirplaneView().setCoordonnees(xDepart, yDepart);
                source.getAirplaneView().setVitesse(new Vitesse(stepX / 8, stepY / 8));

                Thread t = new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        int i = 0;
                        //Boucle qui fait pleuvoir NOMBRE_BOMBE_RAID bombes
                        while (i < NOMBRE_BOMBE_RAID) {
                            double cX = c.getX() + i * stepX;
                            double cY = c.getY() + i * stepY;
                            // On choisit d'abord la case, si elle est occupée par un obstacle alors on prend une des neufs cases libres autour de la case originale. Si aucune case n'est libre, aucune bombe n'est lancée.
                            CaseModelNew c = p.getCaseForeground((int) (cX / 34), (int) (cY / 34));
                            if (c != null && c.isObstacle()) {
                                LinkedList<Coordonnees> casesLibres = new LinkedList<Coordonnees>();
                                for (int j = -1; i < 2; i++) {
                                    for (int k = -1; k < 2; k++) {
                                        c = p.getCaseForeground((int) (cX / 34 + j), (int) (cY / 34 + k));
                                        if (c != null && !c.isObstacle()) {
                                            casesLibres.add(new Coordonnees(cX + 34 * j, cY + 34 * k));
                                        }
                                    }
                                }
                                if (casesLibres.size() > 0) {
                                    int r = new Random().nextInt(casesLibres.size());
                                    Coordonnees coord = casesLibres.get(r);
                                    cX = coord.getX();
                                    cY = coord.getY();
                                } else {
                                    cX = -1;
                                    cY = -1;
                                }

                            }
                            if (cX != -1 && cY != -1) {
                                source.getBombView().addBombs(new Bomb(System.currentTimeMillis(), cX, cY));
                                //bombSpawnCount++;
                            }
                            i++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {

                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        this.interrupt();
                        raidEnCours = false;
                        source.getAirplaneView().setEnCours(false);
                    }
                };
                t.start();
            }

        }

        public void updateSmokes() {
            LinkedList<Smoke> smokes = source.getMissileView().getSmokes();
            for (int i = 0; i < smokes.size(); i++) {
                Smoke s = smokes.get(i);
                s.updateSmoke();
                if (!s.isVisible()) {
                    smokes.remove(i);
                    source.remove(s);
                }
            }
        }

        public void updateExplosions() {
            for (int i = 0; i < source.getExplosions().size(); i++) {
                Explosion e = source.getExplosions().get(i);
                e.updateExplosion();
                if (!e.isVisible()) {
                    source.getExplosions().remove(i);
                    source.remove(e);
                }
            }
        }

        public void checkCollisionMissile() {
            LinkedList<MissileModel> missiles = source.getMissileView().getMissiles();
            // les missiles entre eux
            for (int i = 0; i < missiles.size(); i++) {
                MissileModel missile = missiles.get(i);
                Rectangle r1 = missile.getBounds();
                for (int j = 0; j < missiles.size(); j++) {
                    Rectangle r2 = missiles.get(j).getBounds();
                    if (i != j && r1.intersects(r2)) {
                        // alors les 2 explosent
                        missile.getTimer().stop(); // stop les timers des deux missiles
                        missiles.get(j).getTimer().stop();
                        missile.setVisible(false); // model
                        missiles.get(j).setVisible(false);
                    }
                }

                CaseModelNew[][] p = source.getPlateau().getPlat().getForeground();
                /** Ici on va calculer approximativement les 2 points
                 * posX,posY correspond au point le plus proche du centre vertical de l'écran
                 * posX2,posY2 correspond au 2e point de collision possible, celui plus proche du centre horizontal
                 */

                double cosM = Math.cos(missile.getOrientation() - Math.toRadians(90));
                double sinM = Math.sin(missile.getOrientation() + Math.toRadians(90));
                int posX = 0;
                int posX2 = 0;
                int posY = 0;
                int posY2 = 0;
                if (cosM > 0) {
                    posX = (int) ((missile.getEndX() + (missile.getWidth() / 2)) / 34);
                } else {
                    posX = (int) ((missile.getEndX() - (missile.getWidth() / 2)) / 34);
                }
                if (sinM > 0) {
                    posY = (int) ((missile.getEndY() - (missile.getHeight() / 2)) / 34);

                } else {
                    posY = (int) ((missile.getEndY() + (missile.getHeight() / 2)) / 34);
                }
                posX2 = (int) (missile.getEndX() / 34);
                posY2 = (int) (missile.getEndY() / 34);

                if (posX < 23 && posY < 16 && p[posY][posX].isAWall()) {
                    missile.checkCollision(p[posY][posX], posX, posY, source.getPlateau().getPlat());
                } else if (posX2 < 23 && posY2 < 16 && p[posY2][posX2].isAWall()) {
                    missile.checkCollision(p[posY2][posX2], posX2, posY2, source.getPlateau().getPlat());
                }
            }
        }

        public void checkCollisionTankMissile() {

            LinkedList<MissileModel> missiles = source.getMissileView().getMissiles();
            for (int i = 0; i < missiles.size(); i++) {

                Rectangle r1 = (Rectangle) missiles.get(i).getMissile().getHitbox();
                LinkedList<TankModel> tanks = source.getTv().getTanks();
                for (int j = 0; j < tanks.size(); j++) {
                    TankModel tank = tanks.get(j);
                    if (missiles.get(i).estJoueur() || !(tank instanceof Boss)) {
                        if (!(missiles.get(i).isBoss() && tank instanceof Ia)) {
                            Rectangle r2 = (Rectangle) tank.getTank().getHitbox();
                            if (missiles.get(i).getTime() + 170 < System.currentTimeMillis()) {
                                if (r1.intersects(r2)) {
                                    if (j != 0) {
                                        if (missiles.get(i).estJoueur()) {
                                            MainMenu.Instance.getAchievements().getNbrKill().addCurrent();
                                        } else if (!MainMenu.Instance.getAchievements().getBotKillBot().isUnlocked()) {
                                            MainMenu.Instance.getAchievements().getBotKillBot().setUnlocked(true);
                                        }
                                    }
                                    missiles.get(i).getTimer().stop(); // stop le timer du missile
                                    missiles.get(i).setVisible(false); // model
                                    tankHit(tank);
                                }
                            }
                        }
                    }

                }
            }
        }

        public void explosion(double x, double y, int radius) { // Fonction de dégâts des bombes
            LinkedList<TankModel> tanks = source.getTv().getTanks();
            bombExplosionCount++;
            for (TankModel tank : tanks) {
                if (tank.getTank().getHitbox().intersects(new Rectangle((int) (x - radius / 2), (int) (y - radius / 2), radius, radius))) {
                    tankHit(tank);
                }
            }
        }

        public void tankHit(TankModel tank) {
            boolean deadOrWin = false;
            LinkedList<TankModel> tanks = source.getTv().getTanks();
            boolean rem = false;
            if (tank.isShielded()) {
                tank.setShield(false);

            } else {
                if (tank instanceof Joueur && !deadOrWin) {
                    rem = true;
                    deadOrWin = true;
                    if (!custom && fromMainMenu) {
                        MainMenu.decreaseLife();
                        MainMenu.updateLife();
                    }


                    logger.info("fromMainMenu: " + fromMainMenu);
                    logger.info("Custum: " + custom);
                    logger.info("fromMainMenu2: " + (!custom && fromMainMenu));
                    source.getGamePadController().stopGamepad(false);

                    MainMenu.Instance.getAchievements().getNbrDie().addCurrent();

                    MainMenu.Instance.setVictoryLosePanel(false, levelName, custom, fromMainMenu, currentPrivateLevel, startTime);
                } else {
                    if (tank instanceof Ia && source.getPlateau().getPlat().getIa().size() > 0) {
                        if (tank instanceof Boss) {
                            ((Boss) tank).hit(1);
                        }
                        if (!(tank instanceof Boss) || ((Boss) tank).healthPoints <= 0) {
                            rem = true;
                            source.getPlateau().getPlat().getIa().remove(tank); // model
                        }
                    }
                    if (source.getPlateau().getPlat().getIa().size() == 0 && !deadOrWin) {
                        deadOrWin = true;
                        source.getGamePadController().stopGamepad(false);
                        if (!custom && fromMainMenu) {
                            MainMenu.updateLife();
                        }
                        if (bombExplosionCount > 0 && !MainMenu.Instance.getAchievements().getPlaneSurv().isUnlocked()) {
                            MainMenu.Instance.getAchievements().getPlaneSurv().setUnlocked(true);
                        }

                        if (MainMenu.totalPrivateLevel == currentPrivateLevel && !MainMenu.Instance.getAchievements().getEndGame().isUnlocked()) {
                            MainMenu.Instance.setEndPanel();
                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            MainMenu.Instance.getAchievements().getEndGame().setUnlocked(true);
                        } else {
                            MainMenu.Instance.setVictoryLosePanel(true, levelName, custom, fromMainMenu, currentPrivateLevel, startTime);
                            if (fromMainMenu && !custom && MainMenu.totalPrivateLevel + 1 >= MainMenu.Instance.getCampagne().getLevelNum() && currentPrivateLevel == MainMenu.Instance.getCampagne().getLevelNum()) {
                                MainMenu.Instance.getCampagne().increaseLevel();
                                MainMenu.Instance.getAchievements().getCampagne().addCurrent();
                            }
                        }
                    }
                }
                if (rem) {
                    tanks.remove(tank); // vue
                }
            }
        }

    }
}
