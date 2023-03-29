package fr.mardiH.model.Tank.IA;

import fr.mardiH.model.Coordonnees;
import fr.mardiH.model.MissileModel;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.view.panel.GameView;

import java.util.LinkedList;

public class Boss extends Ia {

    public int healthPoints = 25;
    //CoordCanon1 correpond au canon en haut à gauche
    //Haut Droite
    protected Coordonnees coordCanon2;
    protected double orientationCanon2;

    //Bas Gauche
    protected Coordonnees coordCanon3;
    protected double orientationCanon3;
    //Bas Droite
    protected Coordonnees coordCanon4;
    protected double orientationCanon4;

    public Boss(double x, double y, double orientationCanon, double orientationRoues, int typeTank, PlateauModel plat, Joueur j, LinkedList<MissileModel> missiles) {
        super(x, y, orientationCanon, orientationRoues, typeTank, plat, j, missiles);
        coordCanon2 = new Coordonnees(tank.getHitbox().getBounds().getX() + tank.getHitbox().getBounds().getWidth(), tank.getHitbox().getBounds().getY());
        coordCanon3 = new Coordonnees(tank.getHitbox().getBounds().getX(), tank.getHitbox().getBounds().getY() + tank.getHitbox().getBounds().getHeight());
        coordCanon4 = new Coordonnees(tank.getHitbox().getBounds().getX() + tank.getHitbox().getBounds().getWidth(), tank.getHitbox().getBounds().getY() + tank.getHitbox().getBounds().getHeight());
        orientationCanon2 = orientationCanon;
        orientationCanon3 = orientationCanon;
        orientationCanon4 = orientationCanon;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void hit(int damage) {
        healthPoints -= damage;
        if (GameView.barLifeBoss != null) {
            GameView.barLifeBoss.refresh(healthPoints);
        }
    }

    public double getOrientationCanon2() {
        return orientationCanon2;
    }

    public void setOrientationCanon2(double orientationCanon2) {
        this.orientationCanon2 = orientationCanon2;
    }

    public double getOrientationCanon3() {
        return orientationCanon3;
    }

    public void setOrientationCanon3(double orientationCanon3) {
        this.orientationCanon3 = orientationCanon3;
    }

    public double getOrientationCanon4() {
        return orientationCanon4;
    }

    public void setOrientationCanon4(double orientationCanon4) {
        this.orientationCanon4 = orientationCanon4;
    }

    public Coordonnees getCoordCanon2() {
        return coordCanon2;
    }

    public Coordonnees getCoordCanon3() {
        return coordCanon3;
    }

    public Coordonnees getCoordCanon4() {
        return coordCanon4;
    }

}