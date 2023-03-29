package fr.mardiH.model;

import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.VuePlateau;

import javax.swing.*;
import java.awt.*;

public class MissileModel {

    private final Coordonnees start; // coordonnées de départ
    private final Coordonnees end; //coordonnées d'arrivée
    private double dx;
    private double dy;
    private double speed = 4;
    private final double speedCoeff = 0.65;
    private double orientation;
    private final Timer timer = new Timer(9000, null); // auto-destruction en fonction du temps
    private boolean visible = true; // pour savoir quand le retirer de la liste de missiles
    private final boolean joueur;
    private boolean boss;
    private final Image missile;
    private final ObjetManipulable objetMissile;
    private final long time;

    /**
     * @param angle en radian
     */
    public MissileModel(double x, double y, double angle, boolean joueur, boolean boss) {
        this.start = new Coordonnees(x, y);
        this.end = new Coordonnees(x, y);
        this.dx = (speed * Math.cos(angle));
        this.dy = (speed * Math.sin(angle));
        this.joueur = joueur;
        this.setBoss(boss);
        missile = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/bulletDark1_outline.png")).getImage();
        objetMissile = new ObjetManipulable(new Coordonnees(x, y), missile.getWidth(null), missile.getHeight(null), 0);

        // auto-destruction
        MainMenu.timerList.add(timer);
        timer.addActionListener((e) -> {
            this.visible = false;
        });
        timer.start();
        time = System.currentTimeMillis();
    }


    public void move() {
        // rebond sur les bordures du plateau
        if (end.getX() > VuePlateau.ORIGINAL_WIDTH || end.getX() < 0) {
            dx = -dx;
            speed *= speedCoeff;
        }
        if (end.getY() > VuePlateau.ORIGINAL_HEIGHT || end.getY() < 0) {
            dy = -dy;
            speed *= speedCoeff;
        }
        if ((int) speed == 0) {
            this.visible = false;
        }

        // déplacement
        this.start.setX(end.getX());
        this.start.setY(end.getY());
        this.end.move(dx, dy);

        // orientation de l'image
        this.orientation = Math.atan2((end.getY() + objetMissile.getImageHeight() / 2) - (start.getY() + objetMissile.getImageHeight() / 2), (end.getX() + objetMissile.getImageWidth() / 2) - (start.getX() + objetMissile.getImageWidth() / 2)) + Math.toRadians(90);
    }

    public void checkCollision(CaseModelNew c, int x, int y, PlateauModel plateau) {
        // c correspond à la case touchée et (x,y) ses coordonnées
        if (c.getCaseType() == CaseType.MurCassable) {
            c.setCaseType(CaseType.Vide);
            c.getLabel().setIcon(new ImageIcon(c.getCaseType().getImage().getImage().getScaledInstance(c.getLabel().getIcon().getIconWidth(), c.getLabel().getIcon().getIconWidth(), Image.SCALE_DEFAULT)));
            this.visible = false;
        } else if (c.getCaseType() == CaseType.Mur) {
            /*
            Pour calculer les collisions tout d'abord on regarde l'orientation du missile
            Pour chaque direction il n'y a que 2 possibilités de collisions
            Ensuite on détermine dans que cas on est à partir des coordonnées du missile par rapport à la case 
            */
            orientation = -(this.orientation - Math.PI / 2);
            if (orientation < 0) {
                orientation += 2 * Math.PI;
            }
            /**
             * Le raisonnement est identique à chaque cas mais les valeurs différent.
             * C'est un peu complexe à comprendre sans schéma
             */
            //Direction haut droite
            if (orientation > 0 && orientation < Math.PI / 2) {
                /**
                 *Pour faire simple, la majeure différence entre les 2 collisions possibles de la direction est la position des points aux extremités.
                 *Si le missile fait une collision avec une case qui est haut dessus de lui alors, son y le plus haut est exactement (plus ou moins) a la frontière entre les 2 cases
                 *Et c'est pas le cas si le missile fait une collision avec une case qui est à sa droite
                 *Donc si on augmente la valeur du y minimal et qu'il possède encore le même y que la case touchée alors -> la case touchée était pas au dessus de lui
                 *(c'est plus simple à comprendre avec un schéma)
                 *Ici c2 permet de gérer la collision à l'intersection des cases
                 */

                CaseModelNew c2 = plateau.getCaseForeground(x, y + 1);
                if ((int) (getBounds().getMinY() + 3) / 34 == y || (c2 != null && c2.isObstacle())) {
                    dx = -dx;
                } else {
                    dy = -dy;
                }
            }
            //Direction haut gauche
            if (orientation < Math.PI && orientation > Math.PI / 2) {
                CaseModelNew c2 = plateau.getCaseForeground(x, y + 1);
                if ((int) (getBounds().getMinY() + 3) / 34 == y || (c2 != null && c2.isObstacle())) {
                    dx = -dx;
                } else {
                    dy = -dy;
                }
            }
            //Direction bas droite
            if (orientation < Math.PI * 2 && orientation > 3 * Math.PI / 2) {
                CaseModelNew c2 = plateau.getCaseForeground(x, y - 1);
                // Les coordonnées de maxY sont erronnées donc j'ai du faire autrement
                if ((int) ((getEndY() - 3) / 34) == y || (c2 != null && c2.isObstacle())) {
                    dx = -dx;
                } else {
                    dy = -dy;
                }
            }
            //Direction bas gauche
            if (orientation > Math.PI && orientation < 3 * Math.PI / 2) {
                CaseModelNew c2 = plateau.getCaseForeground(x + 1, y);
                if ((int) (getBounds().getMinX() + 3) / 34 == x || (c2 != null && c2.isObstacle())) {
                    dy = -dy;
                } else {
                    dx = -dx;
                }
            }

            speed *= speedCoeff;
            move();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) end.getX(), (int) end.getY(), (int) objetMissile.getImageHeight(), (int) objetMissile.getImageWidth());
    }

    public double getEndX() {
        return this.end.getX();
    }

    public double getEndY() {
        return this.end.getY();
    }

    public double getOrientation() {
        return this.orientation;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean estJoueur() {
        return this.joueur;
    }

    public Image getImage() {
        return this.missile;
    }

    public ObjetManipulable getMissile() {
        return this.objetMissile;
    }

    public void setHitbox(Shape hitbox) {
        objetMissile.setHitbox(hitbox);
    }

    public double getWidth() {
        return objetMissile.getImageWidth();
    }

    public double getHeight() {
        return objetMissile.getImageHeight();
    }

    public Timer getTimer() {
        return timer;
    }

    public long getTime() {
        return time;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

}