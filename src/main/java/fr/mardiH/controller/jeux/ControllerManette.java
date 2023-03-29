package fr.mardiH.controller.jeux;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.model.TankModel;
import fr.mardiH.view.panel.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import static fr.mardiH.view.MainMenu.gamePadInstance;

public class ControllerManette extends TankController {
    boolean shoot = false;
    private Timer timer;
    TankModel tank;
    public ControllerManette(GameView vue, PlateauModel plateau, TankModel tank) {
        super(vue, plateau, tank);
        this.tank = tank;
    }

    public void doVibration(ControllerIndex gamePad, float min, float max, int timeMS) {
        if (gamePad != null && gamePad.isConnected()) {
            Random r = new Random();
            float minMax = (max - min) + max;
            float left = r.nextFloat() * minMax;
            float right = r.nextFloat() * minMax;
            if (left > 1f) {
                left = 1f;
            }

            if (right > 1f) {
                right = 1f;
            }
            try {
                gamePad.doVibration(left, right, timeMS);
            } catch (ControllerUnpluggedException e1) {
                e1.printStackTrace();
            }
        } else {
            stopGamepad(true);
        }
    }

    public void stopGamepad(boolean toNull) {
        try {
            if (gamePadInstance != null) {
                if (gamePadInstance.isConnected()) {
                    gamePadInstance.doVibration(0f, 0f, 2);
                } else {
                    gamePadInstance = null;
                }
            }
        } catch (ControllerUnpluggedException e) {
            e.printStackTrace();
        }
        if (droite || gauche || haut || bas) {
            droite = false;
            gauche = false;
            haut = false;
            bas = false;
            plateau.getTank().stickReleased();
            timer.setRepeats(false);
            timer.stop();
            if (toNull) {
                gamePadInstance = null;
            }
        }
    }

    public boolean isShoot() {
        return shoot;
    }

    public class StickListener implements ActionListener {


        public StickListener(Timer time) {
            timer = time;
        }

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (gamePadInstance != null) {
                if (gamePadInstance.isConnected()) {
                    try {
                        checkPresed(gamePadInstance);
                        checkReleased(gamePadInstance);
                        checkBtn(gamePadInstance);
                        checkRightStick(gamePadInstance);
                    } catch (ControllerUnpluggedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    stopGamepad(true);
                }
            }
        }

        private void checkRightStick(ControllerIndex gamePadInstance) {
            if (gamePadInstance != null && gamePadInstance.isConnected()) {
                double x = 0;
                double y = 0;
                if (gamePadInstance != null && gamePadInstance.isConnected()) {
                    try {
                        if (gamePadInstance.getAxisState(ControllerAxis.RIGHTX) >= 0.5 || gamePadInstance.getAxisState(ControllerAxis.RIGHTX) <= -0.5) {
                            x = gamePadInstance.getAxisState(ControllerAxis.RIGHTX);
                        }
                    } catch (ControllerUnpluggedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePadInstance != null && gamePadInstance.isConnected()) {
                    try {
                        if (gamePadInstance.getAxisState(ControllerAxis.RIGHTY) >= 0.5 || gamePadInstance.getAxisState(ControllerAxis.RIGHTY) <= -0.5) {
                            y = gamePadInstance.getAxisState(ControllerAxis.RIGHTY);
                        }
                    } catch (ControllerUnpluggedException ex) {
                        ex.printStackTrace();
                    }
                    if (!(x > -0.5 && x < 0.5 && y > -0.5 && y < 0.5)) {
                        plateau.getTank().setOrientationCanonGamePad(x, y * -1);
                    }
                } else {
                    stopGamepad(true);
                }
            } else {
                stopGamepad(true);
            }
        }

        private void checkBtn(ControllerIndex gamePad) throws ControllerUnpluggedException {
            if (gamePad != null && gamePad.isConnected()) {
                if (gamePad.isButtonJustPressed(ControllerButton.RIGHTBUMPER) || gamePad.isButtonJustPressed(ControllerButton.A)) {
                    if (model.getBalles() > 0) {
                        doVibration(gamePad, .8f, 9f, 300);
                        tir(model);
                        shoot = true;
                    }
                }
            } else {
                stopGamepad(true);
            }
        }

        private void checkReleased(ControllerIndex gamePad) throws ControllerUnpluggedException {

            if (gamePad != null && gamePad.isConnected()) {
                if (gamePad != null && gamePad.isConnected()) {

                    if (droite && gamePad.getAxisState(ControllerAxis.LEFTX) <= 0.49) {
                        droite = false;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {

                    if (gauche && gamePad.getAxisState(ControllerAxis.LEFTX) >= -0.49) {
                        gauche = false;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {

                    if (haut && gamePad.getAxisState(ControllerAxis.LEFTY) <= 0.49) {
                        haut = false;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {

                    if (bas && gamePad.getAxisState(ControllerAxis.LEFTY) >= -0.49) {
                        bas = false;
                    }
                } else {
                    stopGamepad(true);
                }
                plateau.getTank().stickReleased();
            } else {
                stopGamepad(true);
            }
        }


        private void checkPresed(ControllerIndex gamePad) throws ControllerUnpluggedException {
            if (gamePad != null && gamePad.isConnected()) {
                ((Joueur) tank).gamePadUpdateSpeed();
                if (gamePad != null && gamePad.isConnected()) {
                    if (!droite && gamePad.getAxisState(ControllerAxis.LEFTX) >= 0.5) {
                        droite = true;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {

                    if (!gauche && gamePad.getAxisState(ControllerAxis.LEFTX) <= -0.5) {
                        gauche = true;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {
                    if (!haut && gamePad.getAxisState(ControllerAxis.LEFTY) >= 0.5) {
                        haut = true;
                    }
                } else {
                    stopGamepad(true);
                }
                if (gamePad != null && gamePad.isConnected()) {

                    if (!bas && gamePad.getAxisState(ControllerAxis.LEFTY) <= -0.5) {
                        bas = true;
                    }
                } else {
                    stopGamepad(true);
                }
                ((Joueur) tank).gamePadUpdateSpeed();
                plateau.getTank().keyPressed();
                ((Joueur) tank).gamePadUpdateSpeed();
            } else {
                stopGamepad(true);
            }
        }
    }

}
