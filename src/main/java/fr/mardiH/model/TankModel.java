package fr.mardiH.model;

import fr.mardiH.controller.jeux.BossControlleur;
import fr.mardiH.controller.jeux.TankController;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.Tank.IA.Boss;
import fr.mardiH.model.Tank.Joueur;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Observable;


public abstract class TankModel extends Observable {


    protected final double VITESSEMAX = 1.25;
    public boolean VerticalH = false;
    public boolean HorizontalG = false;
    public TankController controller = null;
    protected Image imageTank;
    protected Image imageCannon;
    protected int cannonW;
    protected int cannonH;
    protected Coordonnees coordCanon1;
    protected double orientationCanon; // En radian
    protected int balles = 3;
    protected ObjetManipulable tank;
    protected double orientationRoues; // En radian aussi
    protected Rectangle tailleEcran;
    protected PlateauModel plateau;
    protected Vitesse vitesse = new Vitesse(0, 0);
    protected double vSpeed = 0;
    protected double hSpeed = 0;
    protected ListeBonus bonus = new ListeBonus();

    protected LinkedList<Thread> threadList = new LinkedList<Thread>();
    protected Thread actualisationVitesse = null;


    public TankModel(double x, double y, double orientationCanon, double orientationRoues, int typeTank) {
        typeTank(typeTank, x, y, orientationRoues);
        this.orientationCanon = orientationCanon;
        this.orientationRoues = orientationRoues;
    }

    //int typeTank = 0 Player
    //int typeTank = 1 Bot
    //int typeTank = other Boss

    public void typeTank(int typeTank, double x, double y, double orientationRoues) { // Fonction qui choisit l'image en fonction du type de tank sélectionné
        ImageIcon iT;
        ImageIcon iC;
        if (typeTank == 0) {
            String[] id = CaseType.Joueur.getImage().getDescription().split("_");
            iT = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBody_" + id[1]));
            iC = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBarrel_" + id[1]));
        } else if (typeTank == 1) {
            String[] id = CaseType.Bot.getImage().getDescription().split("_");
            iT = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBody_" + id[1]));
            iC = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBarrel_" + id[1]));
        } else {
            String[] id = CaseType.Boss.getImage().getDescription().split("_");
            iT = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBody_" + id[1]));
            iC = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tankBarrel_" + id[1]));
        }
        imageTank = iT.getImage();
        imageCannon = iC.getImage();
        tank = new ObjetManipulable(new Coordonnees(x, y), imageTank.getWidth(null), imageTank.getHeight(null), 0);
        cannonW = imageCannon.getWidth(null);
        cannonH = imageCannon.getHeight(null);
        coordCanon1 = new Coordonnees(x, y);

    }

    protected void correctionVitesse(double coeff) {
        if (vSpeed < -VITESSEMAX * coeff) {
            vSpeed = -VITESSEMAX * coeff;
        } else if (vSpeed > VITESSEMAX * coeff) {
            vSpeed = VITESSEMAX * coeff;
        }
        if (hSpeed < -VITESSEMAX * coeff) {
            hSpeed = -VITESSEMAX * coeff;
        } else if (hSpeed > VITESSEMAX * coeff) {
            hSpeed = VITESSEMAX * coeff;
        }
    }

    public void move() { // Fonction qui fait avancer le tank et choisit son orientation en fonction de la direction
        double tmp = orientationRoues;
        if (vitesse.getDy() < 0) {
            if (vitesse.getDx() > 0) {
                if (orientationRoues != Math.PI / 4) {
                    if (orientationRoues < Math.PI / 4 || orientationRoues > (5 * Math.PI) / 4) {
                        changementOrientation(0.3);
                    } else {
                        changementOrientation(-0.3);
                    }
                    if (orientationRoues > (Math.PI / 4 - 0.3) && orientationRoues < (Math.PI / 4 + 0.3)) {
                        orientationRoues = Math.PI / 4;
                    }
                }
            } else if (vitesse.getDx() < 0) {
                if (orientationRoues != (3 * Math.PI) / 4) {
                    if (orientationRoues < (3 * Math.PI) / 4) {
                        changementOrientation(0.3);
                    } else {
                        changementOrientation(-0.3);
                    }
                    if (orientationRoues > ((3 * Math.PI) / 4 - 0.3) && orientationRoues < ((3 * Math.PI) / 4 + 0.3)) {
                        orientationRoues = (3 * Math.PI) / 4;
                    }
                }
            } else {
                if (orientationRoues != Math.PI / 2) {
                    if (HorizontalG) {
                        changementOrientation(-0.3);
                        if (orientationRoues < Math.PI / 2) {
                            orientationRoues = Math.PI / 2;
                        }
                    } else {
                        changementOrientation(0.3);
                        if (orientationRoues < Math.PI && orientationRoues > Math.PI / 2) {
                            orientationRoues = Math.PI / 2;
                        }
                    }
                }
            }
        } else if (vitesse.getDy() > 0) {
            if (vitesse.getDx() > 0) {
                if (orientationRoues != (7 * Math.PI) / 4) {
                    if (orientationRoues > (7 * Math.PI) / 4 || orientationRoues <= Math.PI / 2) {
                        changementOrientation(-0.3);
                    } else {
                        changementOrientation(0.3);
                    }
                    if (orientationRoues > ((7 * Math.PI) / 4 - 0.3) && orientationRoues < ((7 * Math.PI) / 4 + 0.3)) {
                        orientationRoues = (7 * Math.PI) / 4;
                    }
                }
            } else if (vitesse.getDx() < 0) {
                if (orientationRoues != (5 * Math.PI) / 4) {
                    if (orientationRoues < (5 * Math.PI) / 4) {
                        changementOrientation(0.3);
                    } else {
                        changementOrientation(-0.3);
                    }
                    if (orientationRoues > ((5 * Math.PI) / 4 - 0.3) && orientationRoues < ((5 * Math.PI) / 4 + 0.3)) {
                        orientationRoues = (5 * Math.PI) / 4;
                    }
                }
            } else {
                if (orientationRoues != (3 * Math.PI) / 2) {
                    if (HorizontalG) {
                        changementOrientation(0.3);
                        if (orientationRoues > (3 * Math.PI) / 2) {
                            orientationRoues = (3 * Math.PI) / 2;
                        }
                    } else {
                        changementOrientation(-0.3);
                        if (orientationRoues > Math.PI && orientationRoues < (3 * Math.PI) / 2) {
                            orientationRoues = (3 * Math.PI) / 2;
                        }
                    }
                }
            }
        } else if (vitesse.getDx() > 0) {
            if (orientationRoues != 0) {
                if (VerticalH) {
                    changementOrientation(-0.3);
                    if (orientationRoues > (3 * Math.PI) / 2 && orientationRoues < 2 * Math.PI) {
                        orientationRoues = 0;
                    }

                } else {
                    changementOrientation(0.3);
                    if (orientationRoues < Math.PI / 2 && orientationRoues > 0) {
                        orientationRoues = 0;
                    }
                }
            }
        } else if (vitesse.getDx() < 0) {
            if (orientationRoues != Math.PI) {
                if (VerticalH) {
                    changementOrientation(0.3);
                    if (orientationRoues > Math.PI) {
                        orientationRoues = Math.PI;
                    }
                } else {
                    changementOrientation(-0.3);
                    if (orientationRoues < Math.PI) {
                        orientationRoues = Math.PI;
                    }
                }
            }
        }
        if (bonus.fast) {
            updateFast();
        }
        if (bonus.slowed) {
            updateSlow();
        }
        tank.changementOrientation(orientationRoues, orientationRoues);
        double[] xy = tank.move(vitesse.getDx(), vitesse.getDy(), this.plateau, this);

        if (bonus.invisible) {
            updateInvisible(plateau);
        } else if (tank.canHide(this, plateau)) {
            if (this instanceof Joueur) {
                ((Joueur) this).setHideOnBush(true);
            }
            bonus.invisible = true;
        } else if (this instanceof Joueur) {
            ((Joueur) this).setHideOnBush(false);
        }
        if (bonus.shield) {
            updateShield();
        }
        tank.changementOrientation(orientationRoues, orientationRoues);
        tank.move(vitesse.getDx(), vitesse.getDy(), this.plateau, this);
        if (this instanceof Boss) {
            ((BossControlleur) controller).changementOrientationCanons(tmp, xy);
        } else {
            coordCanon1.setXY(tank.getCentre().getX(), tank.getCentre().getY());
        }
        notifyChange();
    }

    public void changementOrientation(double orientation) { // Fonction qui change l'orientation du tank
        orientationRoues += orientation;
        if (orientationRoues < 0) {
            orientationRoues = 2 * Math.PI + orientationRoues;
        }
        if (orientationRoues > 2 * Math.PI) {
            orientationRoues = orientationRoues - 2 * Math.PI;
        }
    }

    public MissileModel tir() {
        return new MissileModel(coordCanon1.getX(), coordCanon1.getY(), orientationCanon + 2 * Math.PI, this instanceof Joueur, this instanceof Boss);
    }

    public void updateFast() {
        if (System.currentTimeMillis() - bonus.timeFast > 1500) {
            bonus.fast = false;
        }
    }

    public void updateSlow() {
        if (System.currentTimeMillis() - bonus.timeSlow > 1500) {
            bonus.slowed = false;
        }
    }

    public void updateInvisible(PlateauModel plateau) {
        if (System.currentTimeMillis() - bonus.timeInvi > 1000 && !tank.canHide(this, plateau)) {
            bonus.invisible = false;
        }
    }

    public void updateShield() {
        if (System.currentTimeMillis() - bonus.timeShield > 1500) {
            bonus.shield = false;
        }
    }

    public void setHitbox(Shape hitbox) {
        tank.setHitbox(hitbox);
    }

    public abstract void updateSpeed();

    public double getImageWidth() {
        return tank.getImageWidth();
    }

    public double getImageHeight() {
        return tank.getImageHeight();
    }

    public double getWidth() {
        return tank.getHitbox().getBounds().getWidth();
    }

    public double getHeight() {
        return tank.getHitbox().getBounds().getHeight();
    }

    public int getCannonHeight() {
        return cannonH;
    }

    public int getCannonWidth() {
        return cannonW;
    }

    public Image getImageTank() {
        return imageTank;
    }

    public Image getImageCannon() {
        return imageCannon;
    }

    public double getOrientationRoues() {
        return orientationRoues;
    }

    public double getOrientationCanon() {
        return orientationCanon;
    }

    public void setOrientationCanon(double orientationCanon) {
        this.orientationCanon = orientationCanon;
    }

    public ObjetManipulable getTank() {
        return tank;
    }


    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    public void setPlateau(PlateauModel plateau) {
        this.plateau = plateau;
    }

    public void setController(TankController controller) {
        this.controller = controller;
    }

    public void setTimeSlow(double timeSlow) {
        bonus.timeSlow = timeSlow;
    }

    public void setTimeInvi(double timeInvi) {
        bonus.timeInvi = timeInvi;
    }

    public void setTimeFast(double timeFast) {
        bonus.timeFast = timeFast;
    }

    public void setTimeShield(double timeShield) {
        bonus.timeShield = timeShield;
    }

    public boolean isSlowed() {
        return bonus.slowed;
    }

    public void setSlowed(boolean slowed) {
        bonus.slowed = slowed;
    }

    public boolean isFast() {
        return bonus.fast;
    }

    public void setFast(boolean fast) {
        bonus.fast = fast;
    }

    public boolean isInvisible() {
        return bonus.invisible;
    }

    public void setInvisible(boolean invisible) {
        bonus.invisible = invisible;
    }

    public boolean isShielded() {
        return bonus.shield;
    }

    public void setShield(boolean shield) {
        bonus.shield = shield;
    }

    public Coordonnees getCoordCanon1() {
        return coordCanon1;
    }

    public int getBalles() {
        return balles;
    }

    public void setBalles(int balles) {
        this.balles = balles;
    }

}

