package fr.mardiH.controller.panel;

import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.BarLifeBoss;
import fr.mardiH.view.panel.TankView;
import fr.mardiH.view.panel.VuePlateau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class BarLifeBossController {

    BarLifeBoss barLifeBoss;
    JFrame topFrame;

    public BarLifeBossController(BarLifeBoss barLifeBoss) {
        this.barLifeBoss = barLifeBoss;
        this.topFrame = MainMenu.Instance;
    }

    public class resizeController implements ComponentListener {

        @Override
        public void componentHidden(ComponentEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentMoved(ComponentEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentResized(ComponentEvent arg0) {
            barLifeBoss.setBounds((int) (VuePlateau.PLATEAU_BORD_GAUCHE + (271 * TankView.coeff)), (int) (VuePlateau.PLATEAU_BORD_HAUT + 20 * TankView.coeff), (int) (280 * TankView.coeff), (int) (22 * TankView.coeff));
            barLifeBoss.setFont(new Font("Andale Mono", Font.BOLD, 15 * (int) TankView.coeff));
        }

        @Override
        public void componentShown(ComponentEvent arg0) {
            // TODO Auto-generated method stub

        }

    }


}
