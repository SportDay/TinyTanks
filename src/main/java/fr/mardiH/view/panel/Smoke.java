package fr.mardiH.view.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Smoke extends JPanel {

    private final int x;
    private final int y;
    private final Image image = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/smoke.png")).getImage();
    private boolean visible = true;
    private double t = 0.5;

    public Smoke(int x, int y) {
        this.setBackground(new Color(0, 0, 0, 0));
        this.x = x;
        this.y = y;
        this.setBounds(x, y, image.getWidth(null), image.getHeight(null));
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform trans = new AffineTransform();
        trans.translate(x, y);
        trans.scale(TankView.coeff * t, TankView.coeff * t);
        trans.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
        g2D.drawImage(image, trans, null);
    }

    public void updateSmoke() {
        if (t < 0.3) {
            visible = false;
        }
        if (visible) {
            // grossit et retrcit aléatoirement
            int r = (new Random()).nextInt(5);
            if (t <= 0.45 && (r == 0 || r == 1 || r == 2)) {
                t += 0.1; // grossit
            } else {
                t -= 0.1; // rétrécit
            }
        }
        repaint();
    }

    public boolean isVisible() {
        return visible;
    }

}