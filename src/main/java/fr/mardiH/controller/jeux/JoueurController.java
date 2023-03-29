package fr.mardiH.controller.jeux;

import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.TankModel;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.GameView;

import java.awt.event.*;


public class JoueurController extends TankController {
    boolean shoot = false;

    public JoueurController(GameView vue, PlateauModel plateau, TankModel tank) {
        super(vue, plateau, tank);
    }

    public boolean isShoot() {
        return shoot;
    }

    public class KeyAdapter implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == MainMenu.Instance.getSettingsModel().getBtnLEFT()) {
                gauche = false;
            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnRIGHT()) {
                droite = false;
            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnUP()) {
                haut = false;
            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnDOWN()) {
                bas = false;
            }
            //Update des données dans le modèle du tank
            plateau.getTank().keyReleased();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == MainMenu.Instance.getSettingsModel().getBtnLEFT()) {
                gauche = true;
                model.HorizontalG = true;
            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnRIGHT()) {
                droite = true;
                model.HorizontalG = false;

            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnUP()) {
                haut = true;
                model.VerticalH = true;
            }
            if (key == MainMenu.Instance.getSettingsModel().getBtnDOWN()) {
                bas = true;
                model.VerticalH = false;
            }
            //Update des données dans le modèle du tank
            plateau.getTank().keyPressed();
        }
    }

    public class MouseAdapter implements MouseMotionListener, MouseListener {
        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            plateau.getTank().setOrientationCanon(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (MainMenu.tir) {
                    tir(model);
                    shoot = true;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
