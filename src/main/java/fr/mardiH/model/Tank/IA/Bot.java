package fr.mardiH.model.Tank.IA;

import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.Coordonnees;
import fr.mardiH.model.MissileModel;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.Tank.Joueur;

import java.util.LinkedList;
import java.util.List;

public class Bot extends Ia {

    private static LinkedList<CaseModelNew> bushList = new LinkedList<CaseModelNew>();
    Coordonnees coordonnees = new Coordonnees(-1, -1);
    private List<CaseModelNew> chemin = new LinkedList<CaseModelNew>();
    private long timeDest = -1;
    private long timeDir = -1;

    public Bot(double x, double y, double orientationCanon, double orientationRoues, int typeTank, PlateauModel plat, Joueur tank, LinkedList<MissileModel> missiles) {
        super(x, y, orientationCanon, orientationRoues, typeTank, plat, tank, missiles);
    }

    public static LinkedList<CaseModelNew> getBushList() {
        return bushList;
    }

    public static void setBushList(LinkedList<CaseModelNew> bushList) {
        Bot.bushList = bushList;
    }

    public void setOrientationCanon() { // Fonction oriente le canon du bot
        if (!plateau.getTank().isHideOnBush()) {
            this.orientationCanon = Math.atan2(joueur.getTank().getCentre().getY() - this.getTank().getCentre().getY(), joueur.getTank().getCentre().getX() - this.getTank().getCentre().getX());
        }
    }

    public void setOrientationCanon(CaseModelNew c) { // Fonction oriente le canon du bot
        if (!bushList.isEmpty()) {
            this.orientationCanon = Math.atan2(c.y * 34 - this.getTank().getCentre().getY(), c.x * 34 - this.getTank().getCentre().getX());
        }
        return;
    }

    public long getTimeDest() {
        return timeDest;
    }

    public void setTimeDest(long timeDest) {
        this.timeDest = timeDest;
    }

    public long getTimeDir() {
        return timeDir;
    }

    public void setTimeDir(long timeDir) {
        this.timeDir = timeDir;
    }

    public List<CaseModelNew> getChemin() {
        return chemin;
    }

    public void setChemin(List<CaseModelNew> chemin) {
        this.chemin = chemin;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordonnees coordonnees) {
        this.coordonnees = coordonnees;
    }

}
