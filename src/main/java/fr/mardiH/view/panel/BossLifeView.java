package fr.mardiH.view.panel;

import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class BossLifeView extends JPanel {

    JFrame topFrame;
    private final Image imgBar;

    BossLifeView() {
        this.imgBar = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/healthbar.png")).getImage();
        this.topFrame = MainMenu.Instance; //topFrame prend la fenêtre principale (MainMenu donc)
        setVisible(true);
        setOpaque(false);
        setBounds((int) VuePlateau.PLATEAU_BORD_GAUCHE, (int) VuePlateau.PLATEAU_BORD_HAUT, (int) VuePlateau.PLATEAU_WIDTH, (int) VuePlateau.PLATEAU_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);

    }

    public void doDrawing(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform a = new AffineTransform();
        a.translate(400 * TankView.coeff, 30 * TankView.coeff);
        a.scale(0.2 * TankView.coeff, 0.2 * TankView.coeff);
        a.translate(-imgBar.getWidth(null) / 2, -imgBar.getHeight(null) / 2);
        float opacity = 0.7f;
        AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        //g2D.setComposite(alcom);
        g2D.drawImage(imgBar, a, null);
    }


}
