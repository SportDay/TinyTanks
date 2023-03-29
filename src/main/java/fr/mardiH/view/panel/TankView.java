package fr.mardiH.view.panel;

import fr.mardiH.model.Coordonnees;
import fr.mardiH.model.Tank.IA.Boss;
import fr.mardiH.model.Tank.IA.Bot;
import fr.mardiH.model.Tank.Ia;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.model.TankModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


public class TankView extends JPanel implements Observer {

    public static double coeff = 1;
    public Coordonnees c1;
    public Coordonnees centre;
    LinkedList<TankModel> tanks = new LinkedList<TankModel>();
    Joueur joueur;
    double rotationCanon = 0;
    private final double coeffImage = 0.270;
    TankView(Joueur joueur, LinkedList<Ia> ia) {
        this.joueur = joueur;
        joueur.addObserver(this);
        setBackground(new Color(120, 0, 0, 60)); // Fond Vert pour visualier le Panel
        setOpaque(false);
        setVisible(true);
        tanks.add(joueur);
        for (Ia bot : ia) {
            tanks.add(bot);
        }
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);

    }

    public static double getCoeff() {
        return coeff;
    }

    public static void setCoeff(double newCoeff) {
        coeff = newCoeff;
    }

    public LinkedList<TankModel> getTanks() {
        return tanks;
    }

    public void move() { // Fonction qui appelle la fonction de déplacement du tank et stocke ses coordonnées
        if (joueur != null) {
            joueur.move();
            centre = joueur.getTank().getCentre();
        }
    }

    @Override
    public void paintComponent(Graphics g) { // Fonction d'affichage des composants du tank
        super.paintComponent(g);
        for (TankModel t : tanks) {
            doDrawing(g, t);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void doDrawing(Graphics g, TankModel tank) {
        // Coordonnées mises à l'échelle de l'affichage
        double x = tank.getTank().getCentre().getX() * coeff;
        double y = tank.getTank().getCentre().getY() * coeff;

        double rotation = tank.getOrientationRoues();
        Graphics2D g2d = (Graphics2D) g;
        Rectangle rect = new Rectangle(0, 0, (int) (tank.getImageWidth()), (int) (tank.getImageHeight())); // Carré qui est la base de la hitbox
        // AffineTransform permet de stocker plusieurs modifications d'affichage d'un coup
        AffineTransform aT = new AffineTransform();
        int coeffBoss = (tank instanceof Boss) ? 2 : 1;
        aT.translate(x, y);
        aT.scale(coeff * coeffImage * coeffBoss, coeff * coeffImage * coeffBoss);
        aT.rotate(-rotation); // La fonction rotate se fait dans le sens des aiguilles d'une montre, mais le calcul de degré est fait dans le sens trigonométrique
        aT.translate(-tank.getImageWidth() * 0.5, -tank.getImageHeight() * 0.5);
        //Creation de la nouvelle hitbox
        Shape newS = aT.createTransformedShape(rect);
        if (!tank.isInvisible()) {
            g2d.drawImage(tank.getImageTank(), aT, null);
            if (tank instanceof Boss) {
                afficheCanons(g2d, (Boss) tank);
            } else {
                afficheCanon(g2d, tank, x, y);
            }
            if (tank.isShielded()) {
                afficheShield(g2d, tank, x, y, coeffBoss);
            }
        } else if (tank instanceof Joueur) {
            //AlphaComposite gère l'opacité du dessin
            float alpha = 5 * 0.1f;
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(alcom);
            g2d.drawImage(tank.getImageTank(), aT, null);
            afficheCanon(g2d, tank, x, y);
            if (tank.isShielded()) {
                afficheShield(g2d, tank, x, y, coeffBoss);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
        Rectangle e = new Rectangle();
        e.setFrame((newS.getBounds().getX()) / coeff, (newS.getBounds().getY()) / coeff, newS.getBounds().getWidth() / coeff, newS.getBounds().getHeight() / coeff);
        tank.setHitbox(e);

        if (tank instanceof Joueur) {
            //Coordonnées du tank sur l'écran pour le calcul de l'orientation du canon
            ((Joueur) tank).setPositionImage(x + VuePlateau.PLATEAU_BORD_GAUCHE, y + VuePlateau.PLATEAU_BORD_HAUT);
        }
        if (tank instanceof Ia) {
            ((Ia) tank).setPositionImage(x, y);
        }
    }

    public void afficheCanons(Graphics2D g2d, Boss tank) { // Affichage des canons
        afficheCanon(g2d, tank, tank.getCoordCanon1(), tank.getOrientationCanon());
        afficheCanon(g2d, tank, tank.getCoordCanon2(), tank.getOrientationCanon2());
        afficheCanon(g2d, tank, tank.getCoordCanon3(), tank.getOrientationCanon3());
        afficheCanon(g2d, tank, tank.getCoordCanon4(), tank.getOrientationCanon4());
    }

    public void afficheCanon(Graphics2D g2d, TankModel tank, double x, double y) { // Affichage du canon central du Tank
        x = tank.getCoordCanon1().getX() * coeff;
        y = tank.getCoordCanon1().getY() * coeff;

        rotationCanon = tank.getOrientationCanon();
        AffineTransform aT = new AffineTransform();
        aT.translate(x, y);
        aT.scale(coeff * coeffImage, coeff * coeffImage);
        aT.rotate(rotationCanon);
        aT.translate(0, -tank.getCannonHeight() * 0.50);
        g2d.drawImage(tank.getImageCannon(), aT, null);
    }

    public void afficheCanon(Graphics2D g2d, TankModel tank, Coordonnees canon, double angle) { // Affichage du canon "canon"
        double x = canon.getX() * coeff;
        double y = canon.getY() * coeff;

        rotationCanon = tank.getOrientationCanon();
        AffineTransform aT = new AffineTransform();
        int coeffBoss = (tank instanceof Boss) ? 2 : 1;
        aT.translate(x, y);
        aT.scale(coeff * coeffImage * coeffBoss, coeff * coeffImage * coeffBoss);
        aT.rotate(angle);
        aT.translate(0, -tank.getCannonHeight() * 0.50);
        g2d.drawImage(tank.getImageCannon(), aT, null);
    }

    public void afficheShield(Graphics2D g2d, TankModel tank, double x, double y, int coeffBoss) { // Affichage du bouclier
        ImageIcon shield = new ImageIcon(ClassLoader.getSystemResource("Images/Bonus/bubble.png"));
        float alpha = 9 * 0.1f;
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alcom);
        AffineTransform aT = new AffineTransform();
        aT.translate(x, y);
        aT.scale(coeff * coeffImage * 2 * coeffBoss, coeff * coeffImage * 2 * coeffBoss);
        if (tank instanceof Boss) {
            aT.translate(-tank.getImageWidth() * 0.48, -tank.getImageHeight() * 0.48);
        } else {
            aT.translate(-tank.getImageWidth() * 0.6, -tank.getImageHeight() * 0.6);

        }
        g2d.drawImage(shield.getImage(), aT, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();

    }

    public void resize(ComponentEvent e) {
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

    public void addTank(TankModel t) {
        tanks.add(t);
    }

    public void updateSpeed() {
        for (TankModel t : tanks) {
            t.updateSpeed();
            t.move();
        }
    }

    public void updateCanon() {
        for (TankModel t : tanks) {
            if (t instanceof Bot) {
                ((Bot) t).setOrientationCanon();
                t.controller.tir(t);
            } else if (t instanceof Boss) {
                t.controller.tir(t);
            }
        }
    }

}
