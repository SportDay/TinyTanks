package fr.mardiH.view.panel;

import fr.mardiH.model.Bomb;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public class BombView extends JPanel {
    LinkedList<Bomb> bombs = new LinkedList<Bomb>();
    private final Image imgBomb = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/bomb.png")).getImage();

    public BombView() {
        setVisible(true);
        setOpaque(false);
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

    public void addBombs(Bomb b) {
        bombs.add(b);
    }

    public void removeBombs(Bomb b) {
        bombs.remove(b);
    }

    public LinkedList<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public void paintComponent(Graphics g) { // Fonction d'affichage des composants du tank
        super.paintComponent(g);
        for (Bomb b : getBombs()) {
            doDrawing(g, b);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void doDrawing(Graphics g, Bomb b) {
        Graphics2D g2d = (Graphics2D) g;

        double x = b.getCoordonnees().getX() * TankView.coeff;
        double y = b.getCoordonnees().getY() * TankView.coeff;


        float alpha = 5 * 0.1f;
        alpha = (float) (1 - (b.getDecompte() * 0.5));
        if (alpha < 0 || alpha > 1) {
            alpha = 1;
        }
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(alcom);

        double coeff = 1 + (b.getDecompte() * 0.8);
        AffineTransform aT = new AffineTransform();
        aT.translate(x, y);
        aT.scale(0.3 * coeff * TankView.coeff, 0.3 * coeff * TankView.coeff);
        aT.translate(-0.5 * imgBomb.getWidth(null), -0.5 * imgBomb.getHeight(null));
        g2d.drawImage(imgBomb, aT, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }


    public void resize() {
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }
}
