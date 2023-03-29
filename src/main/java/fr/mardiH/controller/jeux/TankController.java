package fr.mardiH.controller.jeux;


import fr.mardiH.model.MissileModel;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.TankModel;
import fr.mardiH.view.panel.BombView;
import fr.mardiH.view.panel.GameView;
import fr.mardiH.view.panel.MissileView;
import fr.mardiH.view.panel.TankView;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class TankController implements ComponentListener {
    public GameView vue;
    public PlateauModel plateau;
    public boolean haut = false;
    public boolean bas = false;
    public boolean gauche = false;
    public boolean droite = false;
    protected TankModel model;
    TankView tankView;
    MissileView missileView;
    BombView bombView;

    public TankController(GameView vue, PlateauModel plateau, TankModel tank) {
        this.vue = vue;
        this.plateau = plateau;
        this.tankView = vue.getTv();
        this.missileView = vue.getMissileView();
        this.bombView = vue.getBombView();
        this.model = tank;
    }

    public void tir(TankModel t) {
        if (t.getBalles() > 0) {
            MissileModel missile = t.tir();
            if (missile != null) {
                for (int i = 0; i < 4; i++) {
                    missile.move();
                }
                plateau.addMissile(missile);
                vue.updateMissiles();
                t.setBalles(t.getBalles() - 1);
            }
        }
    }


    @Override
    public void componentResized(ComponentEvent e) {
        tankView.resize(e);
        missileView.resize(e);
        bombView.resize();
        vue.repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    public boolean isKeyPressed() {
        return haut || bas || gauche || droite;
    }

    public PlateauModel getPlateau() {
        return plateau;
    }

    public void setPlateau(PlateauModel plateau) {
        this.plateau = plateau;
    }

    public GameView getVue() {
        return vue;
    }

    public void setVue(GameView vue) {
        this.vue = vue;
    }

    public TankModel getModel() {
        return model;
    }

}
