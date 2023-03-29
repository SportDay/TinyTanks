package fr.mardiH.controller.jeux;

import fr.mardiH.model.Coordonnees;
import fr.mardiH.model.MissileModel;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.IA.Boss;
import fr.mardiH.model.TankModel;
import fr.mardiH.view.panel.GameView;

import java.util.LinkedList;

public class BossControlleur extends IaControlleur {
    private double timeTir = System.currentTimeMillis();

    public BossControlleur(GameView vue, PlateauModel plateau, TankModel tank) {
        super(vue, plateau, tank);
    }

    @Override
    public void nextMove() {
        short r = (short) (Math.random() * 100);
        if (r == 0) {
            haut = true;
        } else if (r == 1) {
            bas = true;
        } else if (r == 2) {
            gauche = true;
        } else if (r == 3) {
            droite = true;
        } else if (r == 4) {
            haut = false;
        } else if (r == 15) {
            bas = false;
        } else if (r == 6) {
            gauche = false;
        } else if (r == 7) {
            droite = false;
        }
    }

    @Override
    public void tir(TankModel t) {
        if (System.currentTimeMillis() - timeTir > 1000) {
            timeTir = System.currentTimeMillis();
            LinkedList<MissileModel> missile = ((BossControlleur) t.controller).tirBoss();
            for (MissileModel missileModel : missile) {
                if (missile != null) {
                    plateau.addMissile(missileModel);
                    vue.updateMissiles();
                    t.setBalles(t.getBalles() - 1);
                }
            }
        }
    }

    public void changementOrientationCanons(double tmp, double[] xy) {

        double tankX = model.getTank().getHitbox().getBounds().getX();
        double tankMaxX = model.getTank().getHitbox().getBounds().getX() + model.getTank().getHitbox().getBounds().getWidth();
        double tankY = model.getTank().getHitbox().getBounds().getY();
        double tankMaxY = model.getTank().getHitbox().getBounds().getY() + model.getTank().getHitbox().getBounds().getHeight();

        if (model.getOrientationRoues() == 0) {

            model.getCoordCanon1().setXY(tankX + 15, tankY + 15);
            ((Boss) model).getCoordCanon2().setXY(tankMaxX - 15, tankY + 15);
            ((Boss) model).getCoordCanon3().setXY(tankX + 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon4().setXY(tankMaxX - 15, tankMaxY - 15);
        } else if (model.getOrientationRoues() == Math.PI / 2) {
            model.getCoordCanon1().setXY(tankX + 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon2().setXY(tankX + 15, tankY + 15);
            ((Boss) model).getCoordCanon3().setXY(tankMaxX - 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon4().setXY(tankMaxX - 15, tankY + 15);
        } else if (model.getOrientationRoues() == Math.PI) {
            model.getCoordCanon1().setXY(tankMaxX - 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon2().setXY(tankX + 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon3().setXY(tankMaxX - 15, tankY + 15);
            ((Boss) model).getCoordCanon4().setXY(tankX + 15, tankY + 15);
        } else if (model.getOrientationRoues() == (3 * Math.PI) / 2) {
            model.getCoordCanon1().setXY(tankMaxX - 15, tankY + 15);
            ((Boss) model).getCoordCanon2().setXY(tankMaxX - 15, tankMaxY - 15);
            ((Boss) model).getCoordCanon3().setXY(tankX + 15, tankY + 15);
            ((Boss) model).getCoordCanon4().setXY(tankX + 15, tankMaxY - 15);
        } else {
            deplacementCanon(model.getCoordCanon1(), tmp, xy);
            deplacementCanon(((Boss) model).getCoordCanon2(), tmp, xy);
            deplacementCanon(((Boss) model).getCoordCanon3(), tmp, xy);
            deplacementCanon(((Boss) model).getCoordCanon4(), tmp, xy);

        }
        updateOrientationCanons();
    }

    public void updateOrientationCanons() {
        model.setOrientationCanon(-model.getOrientationRoues() + 5 * (Math.PI / 4));
        ((Boss) model).setOrientationCanon2(-model.getOrientationRoues() + 7 * (Math.PI / 4));
        ((Boss) model).setOrientationCanon3(-model.getOrientationRoues() + 3 * (Math.PI / 4));
        ((Boss) model).setOrientationCanon4(-model.getOrientationRoues() + (Math.PI / 4));
    }

    public void deplacementCanon(Coordonnees canon, double tmp, double[] xy) {
        double[] cCanon = {canon.getX() + xy[0], canon.getY() + xy[1]};
        double[] cTank = {model.getTank().getCentre().getX(), model.getTank().getCentre().getY()};
        double angleRotation = 0;
        if (Math.toDegrees(Math.abs(tmp - model.getOrientationRoues())) > 300) {
            if (Math.toDegrees(tmp) < 180) {
                angleRotation = -(tmp + (Math.PI * 2 - model.getOrientationRoues()));
            } else {
                angleRotation = -(model.getOrientationRoues() + (Math.PI * 2 - tmp));
            }
        } else {
            angleRotation = -(tmp - model.getOrientationRoues());
        }
        double[] newC = rotate(cCanon, cTank, angleRotation);
        canon.setXY(newC[0] + xy[0], newC[1] + xy[1]);
    }

    private double[] rotate(double[] c, double[] o, double angle) {
        double x = c[0];
        double y = c[1];

        double x2 = x - o[0];
        double y2 = y - o[1];

        x = x2 * Math.cos(angle) + y2 * Math.sin(angle) + o[0];
        y = -x2 * Math.sin(angle) + y2 * Math.cos(angle) + o[1];

        return new double[]{x, y};
    }

    public LinkedList<MissileModel> tirBoss() {
        LinkedList<MissileModel> res = new LinkedList<MissileModel>();
        res.add(new MissileModel(getModel().getCoordCanon1().getX(), getModel().getCoordCanon1().getY(), getModel().getOrientationCanon() + 2 * Math.PI, false, true));
        res.add(new MissileModel(((Boss) getModel()).getCoordCanon2().getX(), ((Boss) getModel()).getCoordCanon2().getY(), ((Boss) getModel()).getOrientationCanon2() + 2 * Math.PI, false, true));
        res.add(new MissileModel(((Boss) getModel()).getCoordCanon3().getX(), ((Boss) getModel()).getCoordCanon3().getY(), ((Boss) getModel()).getOrientationCanon3() + 2 * Math.PI, false, true));
        res.add(new MissileModel(((Boss) getModel()).getCoordCanon4().getX(), ((Boss) getModel()).getCoordCanon4().getY(), ((Boss) getModel()).getOrientationCanon4() + 2 * Math.PI, false, true));
        return res;
    }

}
