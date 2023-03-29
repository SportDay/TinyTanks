package fr.mardiH.model.Tank;

import fr.mardiH.model.ListeBonus;
import fr.mardiH.model.MissileModel;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.TankModel;

import java.util.LinkedList;

public abstract class Ia extends TankModel {

    public Joueur joueur;
    public LinkedList<MissileModel> missiles;
    protected double vSpeed = 0;
    protected double hSpeed = 0;
    protected LinkedList<Thread> threadList = new LinkedList<Thread>();
    protected double xImage = 0;
    protected double yImage = 0;

    public Ia(double x, double y, double orientationCanon, double orientationRoues, int typeTank, PlateauModel plat, Joueur j, LinkedList<MissileModel> missiles) {
        super(x, y, orientationCanon, orientationRoues, typeTank);
        this.plateau = plat;
        this.joueur = j;
        this.missiles = missiles;
    }

    @Override
    public void updateSpeed() {
        boolean deuxTouches = false;
        boolean keyUp = controller.haut;
        boolean keyDown = controller.bas;
        boolean keyLeft = controller.gauche;
        boolean keyRight = controller.droite;
        if ((keyUp && (keyLeft ^ keyRight)) ^ ((keyDown && (keyLeft ^ keyRight)))) {
            deuxTouches = true;
        }
        double coeff = 1;
        if (deuxTouches) {
            coeff = 0.8;
        }
        correctionVitesse(coeff);
        if (keyUp) {
            if (keyLeft) {
                if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
                    vSpeed = -Math.abs(hSpeed);
                } else {
                    hSpeed = -Math.abs(vSpeed);
                }
                if (hSpeed > -VITESSEMAX * coeff) {
                    hSpeed -= VITESSEMAX / 80;
                }
            } else if (keyRight) {
                if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
                    vSpeed = -Math.abs(hSpeed);
                } else {
                    hSpeed = Math.abs(vSpeed);
                }
                if (hSpeed < VITESSEMAX * coeff) {
                    hSpeed += VITESSEMAX / 80;
                }
            }
            if (vSpeed > -VITESSEMAX * coeff) {
                vSpeed -= VITESSEMAX / 80;
            }
        } else if (keyDown) {
            if (keyLeft) {
                if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
                    vSpeed = Math.abs(hSpeed);
                } else {
                    hSpeed = -Math.abs(vSpeed);
                }
                if (hSpeed > -VITESSEMAX * coeff) {
                    hSpeed -= VITESSEMAX / 80;
                }
            } else if (keyRight) {
                if (Math.abs(hSpeed) > Math.abs(vSpeed)) {
                    vSpeed = Math.abs(hSpeed);
                } else {
                    hSpeed = Math.abs(vSpeed);
                }

                if (hSpeed < VITESSEMAX * coeff) {
                    hSpeed += VITESSEMAX / 80;
                }
            }
            if (vSpeed < VITESSEMAX * coeff) {
                vSpeed += VITESSEMAX / 80;
            }
        } else if (keyRight && hSpeed < VITESSEMAX * coeff) {
            hSpeed += VITESSEMAX / 80;
        } else if (keyLeft && hSpeed > -VITESSEMAX * coeff) {
            hSpeed -= VITESSEMAX / 80;
        }

        if (keyUp && keyDown || (!keyUp && !keyDown)) {
            vSpeed = 0;
        }

        if (keyRight && keyLeft || (!keyRight && !keyLeft)) {
            hSpeed = 0;
        }

        if (isSlowed()) {
            coeff *= ListeBonus.SLOW;
        } else {
            bonus.slowed = false;
        }

        if (isFast()) {
            coeff *= ListeBonus.FAST;
        } else {
            bonus.fast = false;
        }
        vitesse.setDy(coeff * vSpeed);
        vitesse.setDx(coeff * hSpeed);
    }

    public void setPositionImage(double x, double y) {
        this.xImage = x;
        this.yImage = y;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

}
