package fr.mardiH.controller.panel;

import fr.mardiH.view.panel.BossLifeView;
import fr.mardiH.view.panel.VuePlateau;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class BossLifeController {

    BossLifeView bossLifeView;

    public BossLifeController(BossLifeView bossLifeView) {
        this.bossLifeView = bossLifeView;
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
            bossLifeView.setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);

        }

        @Override
        public void componentShown(ComponentEvent arg0) {
            // TODO Auto-generated method stub

        }

    }

}
