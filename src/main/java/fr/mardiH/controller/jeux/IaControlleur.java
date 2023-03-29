package fr.mardiH.controller.jeux;

import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.TankModel;
import fr.mardiH.view.panel.GameView;

public abstract class IaControlleur extends TankController {

    public IaControlleur(GameView vue, PlateauModel plateau, TankModel tank) {
        super(vue, plateau, tank);
    }

    public boolean tirSansObstacles(double xb, double yb, double xj, double yj) {
        CaseModelNew[][] plat = plateau.getForeground();
        xj = (int) xj;
        yj = (int) yj;
        xb = (int) xb;
        yb = (int) yb;
        if (xj == xb) {
            if (yj == yb) {
                return true;
            }
            for (double i = Math.min(yj, yb); i <= Math.max(yj, yb); i++) {
                if (plat[(int) i][(int) xj].getCaseType().equals(CaseType.Mur)) {
                    return false;
                }
            }
        } else if (yj == yb) {
            for (double i = Math.min(xj, xb); i <= Math.max(xj, xb); i++) {
                if (plat[(int) yj][(int) i].getCaseType().equals(CaseType.Mur)) {
                    return false;
                }
            }
        } else {
            double coefDir = (yj - yb) / (xj - xb);
            double b = yj - (coefDir * xj);
            double y;
            for (double x = Math.min(xj, xb); x < Math.max(xj, xb); x += 0.15) {
                y = coefDir * x + b;
                if (x < 22 && y >= 0 && y < 15) {
                    if (y >= Math.min(yj, yb) && y <= Math.max(yj, yb)) {
                        if (plat[(int) y][(int) x].getCaseType().equals(CaseType.Mur) || y < 23 && plat[(int) y + 1][(int) x].getCaseType().equals(CaseType.Mur) || x < 23 && plat[(int) y][(int) x + 1].getCaseType().equals(CaseType.Mur)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean tirSansObstacles() {
        return tirSansObstacles(model.getTank().getCentre().getX() / 34, model.getTank().getCentre().getY() / 34, plateau.getTank().getTank().getCentre().getX() / 34, plateau.getTank().getTank().getCentre().getY() / 34);
    }
    
    public boolean tirSansObstacles(double xb, double yb) {
        return tirSansObstacles(xb, yb, plateau.getTank().getTank().getCentre().getX() / 34, plateau.getTank().getTank().getCentre().getY() / 34);
    }

    public abstract void nextMove();

}
