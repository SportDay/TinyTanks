package fr.mardiH.model.Tank;

import fr.mardiH.controller.jeux.ControllerManette;
import fr.mardiH.controller.jeux.TankController;
import fr.mardiH.model.ListeBonus;
import fr.mardiH.model.TankModel;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Joueur extends TankModel {
    protected MouseEvent souris = null;
    protected double xImage = 0;
    protected double yImage = 0;
    protected double xOrient = 0;
    protected double yOrient = 0;
    private ControllerManette gamePadController;
    private Timer gamePadTimer;
    private boolean hideOnBush = false;

    public Joueur(double x, double y, double orientationCanon, double orientationRoues, int typeTank) {
        super(x, y, orientationCanon, orientationRoues, typeTank);
    }

    public void setGamePadController(ControllerManette gamePadController) {
        this.gamePadController = gamePadController;
    }

    public void updateSpeed() {
        updateSpeeds(controller);
    }


    public void gamePadUpdateSpeed() {
        updateSpeeds(gamePadController);
    }

    public void setGamePadTimer(Timer gamePadTimer) {
        this.gamePadTimer = gamePadTimer;
    }

    private void updateSpeeds(TankController controll) {
        boolean deuxTouches = false;
        boolean keyUp = controll.haut;
        boolean keyDown = controll.bas;
        boolean keyLeft = controll.gauche;
        boolean keyRight = controll.droite;

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

        if (keyUp && keyDown) {
            vSpeed = 0;
        }

        if (keyRight && keyLeft) {
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
        if (souris != null) {
            setOrientationCanon(souris);
        }
    }

    public void keyPressed() { // Fonction qui fait avancer le tank ou changer son orientation selon la touche pressée
        if (actualisationVitesse == null) {
            this.actualisationVitesse = new Thread() {
                public void run() {
                    while (controller.isKeyPressed()) {
                        gamePadTimer.stop();
                        if (gamePadController.isKeyPressed()) {
                            stickReleased();
                        }
                        try {
                            updateSpeed();
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        gamePadTimer.start();
                    }
                    while (gamePadController.isKeyPressed()) {
                        if (controller.isKeyPressed()) {
                            keyReleased();
                        }
                        try {
                            gamePadUpdateSpeed();
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    actualisationVitesse.interrupt();
                    actualisationVitesse = null;
                }
            };
            actualisationVitesse.start();
        }
    }

    public void keyReleased() {
        moveReleased(controller);
    }

    public void stickReleased() {
        moveReleased(gamePadController);
    }

    private void moveReleased(TankController controll) {
        boolean keyUp = controll.haut;
        boolean keyDown = controll.bas;
        boolean keyLeft = controll.gauche;
        boolean keyRight = controll.droite;
        if (!keyLeft && !keyRight) {
            hSpeed = 0;
        }
        if (!keyUp && !keyDown) {
            vSpeed = 0;
        }
    }

    public void setOrientationCanon(MouseEvent e) { // Fonction oriente le canon en fonction de la posiiton de la souris
        souris = e;
        xOrient = e.getX() - xImage;
        yOrient = e.getY() - yImage;
        this.orientationCanon = Math.atan2(e.getY() - yImage, e.getX() - xImage);
    }

    public void setOrientationCanonGamePad(double x, double y) { // Fonction oriente le canon en fonction de la posiiton de la souris
        souris = null;
        xOrient = x;
        yOrient = y;
        this.orientationCanon = Math.atan2(y, x);
    }

    public void setPositionImage(double x, double y) {
        this.xImage = x;
        this.yImage = y;
    }

    public boolean isHideOnBush() {
        return hideOnBush;
    }

    public void setHideOnBush(boolean hideOnBush) {
        this.hideOnBush = hideOnBush;
    }

}