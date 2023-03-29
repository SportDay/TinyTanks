package fr.mardiH.view.panel;

import fr.mardiH.model.MissileModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public class MissileView extends JPanel {

    static double coeff = 1;
    protected LinkedList<MissileModel> missiles = new LinkedList<>();
    protected LinkedList<Smoke> smokes = new LinkedList<>();

    public MissileView(LinkedList<MissileModel> missiles) {
        this.missiles = missiles;
        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);
        this.setVisible(true);
        this.setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

    public static void setCoeff(double c) {
        coeff = c;
    }

    public LinkedList<Smoke> getSmokes() {
        return smokes;
    }

    public LinkedList<MissileModel> getMissiles() {
        return missiles;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Smoke smoke : smokes) {
            smoke.draw(g);
        }
        for (MissileModel missile : missiles) {
            draw(g, missile);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g, MissileModel missile) {
        if (missile.isVisible()) {
            int x = (int) ((missile.getEndX()) * coeff);
            int y = (int) ((missile.getEndY()) * coeff);

            Graphics2D g2D = (Graphics2D) g;
            Rectangle rect = new Rectangle(0, 0, (int) missile.getWidth(), (int) missile.getHeight());
            AffineTransform trans = new AffineTransform();
            trans.translate(x, y);
            trans.scale(coeff * 0.65, coeff * 0.65);
            trans.rotate(missile.getOrientation());
            trans.translate(-missile.getWidth() / 2, -missile.getHeight() / 2);

            Shape newShape = trans.createTransformedShape(rect);
            Rectangle r = new Rectangle();

            r.setFrame((newShape.getBounds().getX()) / coeff, (newShape.getBounds().getY()) / coeff, newShape.getBounds().getWidth() / coeff, newShape.getBounds().getHeight() / coeff);
            missile.setHitbox(r);

            g2D.drawImage(missile.getImage(), trans, null);
        }
    }

    public void updateMissiles() {
        for (MissileModel missile : missiles) {
            if (missile.isVisible()) {
                missile.move();
                Smoke smoke = new Smoke((int) (missile.getEndX() * coeff), (int) (missile.getEndY() * coeff));
                smokes.add(smoke);
            }
        }
        repaint();
    }

    public void resize(ComponentEvent e) {
        this.setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

}