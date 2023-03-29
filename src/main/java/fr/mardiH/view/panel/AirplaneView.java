package fr.mardiH.view.panel;

import fr.mardiH.model.Vitesse;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class AirplaneView extends JPanel {

    boolean enCours = false;
    Timer timer;
    Vitesse vitesse = new Vitesse(0, 0);
    int widthPlane = 0;
    int HeightPlane = 0;
    double orientation = 0;
    double x = 0;
    double y = 0;
    private final Image imgPlane;


    AirplaneView() {
        this.imgPlane = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/avion.png")).getImage();
        setVisible(true);
        setOpaque(false);
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (enCours == true) {
            doDrawing(g);
        }
    }

    public void doDrawing(Graphics g) {
        x += vitesse.getDx();
        y += vitesse.getDy();
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform a = new AffineTransform();
        a.translate(x * TankView.coeff, y * TankView.coeff);
        a.scale(0.2 * TankView.coeff, 0.2 * TankView.coeff);
        a.rotate(-orientation);
        a.translate(-imgPlane.getWidth(null) / 2, -imgPlane.getHeight(null) / 2);
        float opacity = 0.35f;
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2D.setComposite(alcom);
        g2D.drawImage(imgPlane, a, null);
    }

    public boolean getEnCours() {
        return enCours;
    }

    public void setEnCours(boolean a) {
        enCours = a;
    }

    public void setCoordonnees(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVitesse(Vitesse vitesse) {
        this.vitesse = vitesse;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

}
