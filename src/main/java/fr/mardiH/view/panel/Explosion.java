package fr.mardiH.view.panel;

import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Explosion extends JPanel {

    private final int x;
    private final int y;
    private final Image image = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/explosion4.png")).getImage();
    private boolean visible = true;
    private double t = 0.2;
    private final long time;

    public Explosion(int x, int y) {
        this.setBackground(new Color(0, 0, 0, 0));
        this.x = (int) (VuePlateau.PLATEAU_BORD_GAUCHE + ((x) * TankView.coeff - image.getWidth(null) / 2));
        this.y = (int) (VuePlateau.PLATEAU_BORD_HAUT + ((y) * TankView.coeff - image.getHeight(null) / 2));
        this.setBounds(this.x, this.y, image.getWidth(null), image.getHeight(null));
        this.time = System.currentTimeMillis();

        MainMenu.Instance.getSound().explosion();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        Toolkit.getDefaultToolkit().sync();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform trans = new AffineTransform();
        trans.translate(getWidth() / 2, getHeight() / 2);
        trans.scale(TankView.coeff * t, TankView.coeff * t);
        trans.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
        g2d.drawImage(image, trans, null);
    }

    public void updateExplosion() {
        if (t < 0.01) {
            visible = false;
        }
        // grossit puis retrecit
        if (visible) {
            if (System.currentTimeMillis() >= time + 90) { // lorsque cela fait plus de 90ms que l'explosion est apparu
                t -= 0.02; // retrecit
            } else {
                t += 0.07; // grossit
            }
        }
        repaint();
    }

    public boolean isVisible() {
        return visible;
    }

}