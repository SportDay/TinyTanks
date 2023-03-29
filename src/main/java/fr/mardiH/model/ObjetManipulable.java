package fr.mardiH.model;

import fr.mardiH.model.Enum.BonusType;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.Tank.Ia;

import javax.swing.*;
import java.awt.*;


public class ObjetManipulable {

    // Tailles de la fenêtre
    private static final int FRAME_WIDTH = 782;
    private static final int FRAME_HEIGHT = 544;
    Coordonnees centre;
    double w; //
    double h;
    Shape Hitbox;


    public ObjetManipulable(Coordonnees c1, Coordonnees c2, Coordonnees c3, Coordonnees c4, Coordonnees centre, double w, double h) {

        this.centre = centre;
        this.w = w;
        this.h = h;
        Hitbox = new Rectangle((int) c1.x, (int) c1.y, (int) w, (int) h);
    }

    public ObjetManipulable(Coordonnees centre, double w, double h, double orientation) {
        this.centre = centre;
        double cx = centre.getX();
        double cy = centre.getY();

        rotation(orientation);
        Rectangle rect = new Rectangle();
        rect.setBounds((int) cx / 2, (int) cy / 2, (int) w, (int) h);
        Hitbox = rect;
        this.w = w;
        this.h = h;
    }


    public void rotation(double angle) { // On considère que le 0° est le 0° trigonométrique (donc la tête de l'objet pointe vers la droite)
        angle = -angle;
    }

    // Fonction de déplacement du coeur pour les tanks
    public double[] move(double vitesse_X, double vitesse_Y, PlateauModel plateau, TankModel tank) {
        //Coordonnée la plus à gauche
        double hitboxX = Hitbox.getBounds().getX();
        //Coordonnée la plus en haut
        double hitboxY = Hitbox.getBounds().getY();
        //Largeur
        double hitboxWidth = Hitbox.getBounds().getWidth();
        //Hauteur
        double hitboxHeight = Hitbox.getBounds().getHeight();

        //Code séparé en 2 parties : Tout d'abord on fait les tests de collision sur l'ajout de vitesse_X aux coorodonnées.
        //Ensuite on fait la même chose pour vitesse_Y

        //Test Pour les bordures gauche / droite
        if (vitesse_X < 0) {
            if (hitboxX + vitesse_X < 0) {
                vitesse_X = 0;
            }
            // Pour pouvoir gérer plusieurs cas dans un seul appel, l'appel est fait avec le nombre maximal de valeur. Elles sont mises à 0 si considérées inutiles
            //(Le point hitboxX représentant déjà le point gauche du tank, si le tank va vers la gauche hitboxWidth n'a aucune utilité)
            hitboxWidth = 0;
        } else if (vitesse_X > 0) {
            //Bordures encore
            if (vitesse_X + hitboxX + hitboxWidth >= FRAME_WIDTH) {
                vitesse_X = 0;
            }
        }
        double signeX = vitesse_X > 0 ? 1 : -1;
        //Ici on va récupérer 2 cases, la plus haute sur laquelle le tank est actuellement et la plus basse sur laquelle le tank est actuellement. Elles peuvent être identiques
        //C'est pour gérer les cas où le tank est sur 2 cases en même temps.
        //Si une des cases est un obstacle, on annule le déplacement.
        CaseModelNew prochainC1 = plateau.getCaseForeground((int) ((vitesse_X + (2 * signeX) + hitboxX + hitboxWidth) / (34)), (int) ((hitboxY + hitboxHeight) / (34)));
        if (prochainC1 != null && prochainC1.isObstacle()) {
            vitesse_X = 0;
        }

        CaseModelNew prochainC2 = plateau.getCaseForeground((int) ((vitesse_X + (2 * signeX) + hitboxX + hitboxWidth) / (34)), (int) ((hitboxY) / (34)));
        if (prochainC2 != null && prochainC2.isObstacle()) {
            vitesse_X = 0;
        }

        //On a besoin de la largeur donc on le réinitialise
        hitboxWidth = Hitbox.getBounds().getWidth();
        //Même principe que pour dx
        if (vitesse_Y < 0) {
            if (hitboxY + vitesse_Y < 0) {
                vitesse_Y = 0;
            }
            hitboxHeight = 0;
        } else if (vitesse_Y > 0 && vitesse_Y + hitboxY + hitboxHeight >= FRAME_HEIGHT) {
            vitesse_Y = 0;
        }

        double signeY = vitesse_Y > 0 ? 1 : -1;

        //2e série de test, ici pour dy
        //(C3) équivaut à la droite et (C4) à la gauche de l'objet
        CaseModelNew prochainC3 = plateau.getCaseForeground((int) ((hitboxX + hitboxWidth) / (34)), (int) ((vitesse_Y + (2 * signeY) + hitboxY + hitboxHeight) / (34)));
        if (prochainC3 != null && prochainC3.isObstacle()) {
            vitesse_Y = 0;
        }
        CaseModelNew prochainC4 = plateau.getCaseForeground((int) ((hitboxX) / (34)), (int) ((vitesse_Y + (2 * signeY) + hitboxY + hitboxHeight) / (34)));
        if (prochainC4 != null && prochainC4.isObstacle()) {
            vitesse_Y = 0;
        }

        //Finalement on regarde s'il y a actuellement des bonus sur les cases par lesquelles passe le tank.
        pickUpBonus(prochainC1, tank);
        pickUpBonus(prochainC2, tank);
        pickUpBonus(prochainC3, tank);
        pickUpBonus(prochainC4, tank);
        centre.move(vitesse_X, vitesse_Y);
        return new double[]{vitesse_X, vitesse_Y};


    }

    // Fonction de récupération de bonus
    private void pickUpBonus(CaseModelNew caseM, TankModel tank) {
        if (caseM != null && caseM.getCaseType() instanceof BonusType && caseM.getCaseType() != BonusType.WARN) {
            if (!(caseM.getCaseType() == BonusType.LIFE && tank instanceof Ia)) {
                ((BonusType) caseM.getCaseType()).execute(tank);
                caseM.setCaseType(CaseType.Vide);
                caseM.getLabel().setIcon(new ImageIcon(CaseType.Vide.getImage().getImage().getScaledInstance(caseM.getLabel().getHeight(), caseM.getLabel().getHeight(), Image.SCALE_SMOOTH)));
            }
        }
    }

    //Fonction qui renvoie true si le tank est dans un buisson, false sinon
    public boolean canHide(TankModel tank, PlateauModel plateau) {
        int x1 = (int) ((Hitbox.getBounds().getMinX() + 10) / 34);
        int x2 = (int) ((Hitbox.getBounds().getMaxX() - 10) / 34);
        int y1 = (int) ((Hitbox.getBounds().getMinY() + 10) / 34);
        int y2 = (int) ((Hitbox.getBounds().getMaxY() - 10) / 34);
        if (x1 == x2 && y1 == y2) {
            CaseModelNew c = plateau.getCaseForeground(x1, y1);
            return c.isABush();
        } else {
            CaseModelNew c1 = plateau.getCaseForeground(x1, y1);
            CaseModelNew c2 = plateau.getCaseForeground(x2, y2);
            return c1.isABush() && c2.isABush();
        }

    }

    public void changementOrientation(double ancienneOrientation, double ajout) {
        double angle = ajout;

        rotation(angle);
    }

    public Coordonnees getCentre() {
        return centre;
    }

    public double getImageHeight() {
        return h;
    }

    public double getImageWidth() {
        return w;
    }

    public Shape getHitbox() {
        return Hitbox;
    }

    public void setHitbox(Shape hitbox) {
        Hitbox = hitbox;
    }


}
