package fr.mardiH.model;

import fr.mardiH.Error.InvalidMapException;
import fr.mardiH.controller.jeux.BotControlleur;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.Tank.IA.Boss;
import fr.mardiH.model.Tank.IA.Bot;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.util.Constants;
import fr.mardiH.view.panel.CreateMenuPanel;
import fr.mardiH.view.panel.ShowCreate;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static fr.mardiH.Main.logger;


public class PlateauModel {
    private static final String PATH = Constants.PATH;
    CaseModelNew[][] background = new CaseModelNew[16][23];
    CaseModelNew[][] foreground = new CaseModelNew[16][23];
    LinkedList<Ia> ia = new LinkedList<Ia>();
    Joueur tank;
    LinkedList<MissileModel> missiles = new LinkedList<MissileModel>();
    private InputStream inputStream;
    private boolean boss = false;
    private int nbDeBot = 0;
    private boolean raid = false;

    public PlateauModel(String path, boolean custom) throws InvalidMapException, IOException {
        if (path.contains("!")) {
            throw new InvalidMapException();
        }
        if (custom) {
            if (new File(PATH + "Maps" + File.separator + path + File.separator + "Map.yaml").exists()) {
                inputStream = new BufferedInputStream(new FileInputStream(PATH + "Maps" + File.separator + path + File.separator + "Map.yaml"));
            } else if (new File(PATH + "Maps" + File.separator + path + File.separator + "Map.yml").exists()) {
                inputStream = new BufferedInputStream(new FileInputStream(PATH + "Maps" + File.separator + path + File.separator + "Map.yml"));
            } else {
                throw new InvalidMapException();
            }
        } else {
            inputStream = getClass().getResourceAsStream("/Maps/" + path);
        }
        init();
    }

    public PlateauModel(CreateMenuPanel m) {
        JPanel pan = (JPanel) m.getImageBG().getComponent(0);
        ShowCreate c = (ShowCreate) pan.getComponent(0);
        LinkedList<JLabel> bgLabel = c.getBgLabel();
        LinkedList<JLabel> fgLabel = c.getFgLabel();
        for (int i = 0; i < bgLabel.size(); i++) {
            try {
                CaseType type = CaseType.parse(bgLabel.get(i).getName().charAt(0));
                if (type == null) {
                    logger.debug(1);
                    throw new InvalidMapException();
                } else {
                    background[i / 23][i % 23] = new CaseModelNew(type, i % 23, i / 23);
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        for (int i = 0; i < fgLabel.size(); i++) {
            try {
                if (fgLabel.get(i).getName() != null) {
                    CaseType type = CaseType.parse(fgLabel.get(i).getName().charAt(0));
                    if (type == null) {
                        logger.debug(1);
                        throw new InvalidMapException();
                    } else {
                        foreground[i / 23][i % 23] = new CaseModelNew(type, i % 23, i / 23);
                    }
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
        }

    }

    /**
     * Il supprime un répertoire
     *
     * @param path Chemin d'accès à la carte que vous souhaitez supprimer.
     */
    public static void remove(String path) {
        SwingWorker work = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (new File(PATH + "Maps" + File.separator + path).exists()) {
                    FileUtils.deleteDirectory(new File(PATH + "Maps" + File.separator + path));
                }
                return null;
            }
        };
        work.execute();
    }

    /**
     * Il lit les fichiers dans le répertoire "Maps" et renvoie une liste des noms des fichiers qui sont
     * des cartes valides
     *
     * @return Une liste de chaînes.
     */
    public static LinkedList<String> mapDispo() { // A DEPLACER DANS LE MENU DE SELECTION DE NIVEAU
        LinkedList<String> map = new LinkedList<>();
        File[] files = new File(PATH + "Maps").listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        for (File f : files) {
            try {
                new PlateauModel(f.getName(), true);
                map.add(f.getName());
            } catch (Exception e) {

            }
        }
        return map;
    }

    public boolean isRaid() {
        return raid;
    }

    public void addMissile(MissileModel missile) {
        missiles.add(missile);
    }

    private void init() throws IOException, InvalidMapException {
        if (inputStream == null) return;
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(inputStream);
        try {
            raid = (boolean) map.get("raid");
        } catch (Exception e) {
            throw new InvalidMapException();
        }
        Scanner sc = new Scanner((String) map.get("bg"));
        String s;
        for (int i = 0; i < 18; i++) {
            s = sc.nextLine();
            for (int j = 0; j < 25; j++) {
                if (i == 0 || i == 17) {
                    if (s.charAt(j) != '#') {
                        logger.error(1);
                        sc.close();
                        throw new InvalidMapException();
                    }
                } else {
                    if (j == 0 || j == 24) {
                        if (s.charAt(j) != '#') {
                            logger.error(1);
                            sc.close();
                            throw new InvalidMapException();
                        }
                    } else {
                        CaseType type = CaseType.parse(s.charAt(j));
                        if (type == null) {
                            logger.error(1 + "." + s.charAt(j));
                            sc.close();
                            throw new InvalidMapException();
                        } else {
                            background[i - 1][j - 1] = new CaseModelNew(type, j - 1, i - 1);
                        }
                    }
                }
            }
        }
        sc.close();
        sc = new Scanner((String) map.get("fg"));
        boolean haveABot = false;
        boolean haveAPlayer = false;
        for (int i = 0; i < 18; i++) {
            s = sc.nextLine();
            for (int j = 0; j < 25; j++) {
                if (i == 0 || i == 17) {
                    if (s.charAt(j) != '#') {
                        logger.error(1);
                        sc.close();
                        throw new InvalidMapException();
                    }
                } else {
                    if (j == 0 || j == 24) {
                        if (s.charAt(j) != '#') {
                            logger.error(1);
                            sc.close();
                            throw new InvalidMapException();
                        }
                    } else {
                        CaseType type = CaseType.parse(s.charAt(j));
                        if (type == null) {
                            logger.error(1);
                            sc.close();
                            throw new InvalidMapException();
                        } else {
                            if (type.lettre == 'J') {
                                if (tank == null) {
                                    tank = new Joueur((j - 1) * 34 + 17, (i - 1) * 34 + 17, 0, 0, 0);
                                    haveAPlayer = true;
                                } else {
                                    sc.close();
                                    throw new InvalidMapException();
                                }
                            } else if (type.lettre == 'R') {
                                nbDeBot++;
                                ia.add(new Bot((j - 1) * 34 + 17, (i - 1) * 34 + 17, 0, 0, 1, this, tank, missiles));
                                haveABot = true;
                            } else if (type.lettre == 'Z') {
                                nbDeBot++;
                                for (Ia e : ia) {
                                    if (e instanceof Boss) {
                                        sc.close();
                                        throw new InvalidMapException();
                                    }
                                }
                                ia.add(new Boss((j - 1) * 34 + 17, (i - 1) * 34 + 17, 0, 0, 2, this, tank, missiles));
                                boss = true;
                                haveABot = true;
                            }
                            foreground[i - 1][j - 1] = new CaseModelNew(type, j - 1, i - 1);
                        }
                    }
                }
            }
        }
        if (!haveABot || !haveAPlayer) {
            sc.close();
            throw new InvalidMapException();
        }
        for (Ia i : ia) {
            i.setJoueur(tank);
        }
        inputStream.close();
        sc.close();
        tank.setPlateau(this);
    }

    /**
     * Il renvoie une liste de tous les mouvements possibles que le bot peut effectuer à partir d'une
     * position donnée
     *
     * @param n le nœud actuel
     * @param b le robot
     * @return Une liste de nœuds.
     */
    public LinkedList<NoeudOuvert> getVoisin(NoeudOuvert n, Bot b) {
        double xj = tank.getTank().getCentre().getX() / 34;
        double yj = tank.getTank().getCentre().getY() / 34;
        LinkedList<NoeudOuvert> result = new LinkedList<NoeudOuvert>();
        if (n.getX() - 1 >= 0 && !getCaseForeground(n.getX() - 1, n.getY()).isObstacle()) {
            result.add(new NoeudOuvert(n, n.getX() - 1, n.getY(), BotControlleur.distance(n.getX() - 1, n.getY(), xj, yj), n.getCoutG() + 1));
            if (n.getY() - 1 >= 0 && !getCaseForeground(n.getX(), n.getY() - 1).isObstacle()) {
                if (!getCaseForeground(n.getX() - 1, n.getY() - 1).isObstacle()) {
                    result.add(new NoeudOuvert(n, n.getX() - 1, n.getY() - 1, BotControlleur.distance(n.getX() - 1, n.getY() - 1, xj, yj), Math.pow(n.getCoutG() + 2, 2)));
                }
            }
            if (n.getY() + 1 < 16 && !getCaseForeground(n.getX(), n.getY() + 1).isObstacle()) {
                if (!getCaseForeground(n.getX() - 1, n.getY() + 1).isObstacle()) {
                    result.add(new NoeudOuvert(n, n.getX() - 1, n.getY() + 1, BotControlleur.distance(n.getX() - 1, n.getY() + 1, xj, yj), Math.pow(n.getCoutG() + 2, 2)));
                }
            }
        }
        if (n.getX() + 1 < 23 && !getCaseForeground(n.getX() + 1, n.getY()).isObstacle()) {
            result.add(new NoeudOuvert(n, n.getX() + 1, n.getY(), BotControlleur.distance(n.getX() + 1, n.getY(), xj, yj), n.getCoutG() + 1));
            if (n.getY() - 1 >= 0 && !getCaseForeground(n.getX(), n.getY() - 1).isObstacle()) {
                if (!getCaseForeground(n.getX() + 1, n.getY() - 1).isObstacle()) {
                    result.add(new NoeudOuvert(n, n.getX() + 1, n.getY() - 1, BotControlleur.distance(n.getX() + 1, n.getY() - 1, xj, yj), Math.pow(n.getCoutG() + 2, 2)));
                }
            }
            if (n.getY() + 1 < 16 && !getCaseForeground(n.getX(), n.getY() + 1).isObstacle()) {
                if (!getCaseForeground(n.getX() + 1, n.getY() + 1).isObstacle()) {
                    result.add(new NoeudOuvert(n, n.getX() + 1, n.getY() + 1, BotControlleur.distance(n.getX() + 1, n.getY() + 1, xj, yj), Math.pow(n.getCoutG() + 2, 2)));
                }
            }
        }
        if (n.getY() - 1 >= 0 && !getCaseForeground(n.getX(), n.getY() - 1).isObstacle()) {
            result.add(new NoeudOuvert(n, n.getX(), n.getY() - 1, BotControlleur.distance(n.getX(), n.getY() - 1, xj, yj), n.getCoutG() + 1));
        }
        if (n.getY() + 1 < 16 && !getCaseForeground(n.getX(), n.getY() + 1).isObstacle()) {
            result.add(new NoeudOuvert(n, n.getX(), n.getY() + 1, BotControlleur.distance(n.getX(), n.getY() + 1, xj, yj), n.getCoutG() + 1));
        }
        return result;
    }

    public LinkedList<Coordonnees> listePasObstacle() { // Renvoie une liste de cases non occupées par des obstacles.
        LinkedList<Coordonnees> res = new LinkedList<Coordonnees>();
        for (int i = 0; i < foreground.length; i++) {
            for (int j = 0; j < foreground[i].length; j++) {
                if (foreground[i][j].getCaseType() == CaseType.Vide) {
                    if (Math.abs(j - (int) tank.getTank().getCentre().getX() / 34) + Math.abs(i - (int) tank.getTank().getCentre().getY() / 34) >= 10) {
                        res.add(new Coordonnees(j * 34 + 17, i * 34 + 17));
                    }
                }
            }
        }

        return res;
    }

    public CaseModelNew[][] getForeground() {
        return foreground;
    }

    public CaseModelNew[][] getBackground() {
        return background;
    }

    public Joueur getTank() {
        return tank;
    }

    public LinkedList<Ia> getIa() {
        return ia;
    }

    public boolean getBoss() {
        return boss;
    }

    public int getNbdeBots() {
        return nbDeBot;
    }

    public LinkedList<MissileModel> getMissile() {
        return missiles;
    }

    public CaseModelNew getCaseForeground(int x, int y) {
        if (x < 0 || x >= foreground[0].length || y < 0 || y >= foreground.length) return null;
        return foreground[y][x];
    }

    public Boss getModeleBoss() {
        for (Ia bot : ia) {
            if (bot instanceof Boss) {
                return (Boss) bot;
            }
        }
        return null;
    }


}