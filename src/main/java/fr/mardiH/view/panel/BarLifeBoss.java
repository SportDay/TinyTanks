package fr.mardiH.view.panel;

import fr.mardiH.model.Tank.IA.Boss;

import javax.swing.*;
import java.awt.*;

public class BarLifeBoss extends JProgressBar {

    Boss boss;

    BarLifeBoss(Boss boss) {
        if (boss == null) {
            return;
        }
        this.boss = boss;
        this.setMaximum(25);
        this.setValue((int) boss.getHealthPoints());
        this.setBounds((int) (VuePlateau.PLATEAU_BORD_GAUCHE + (271 * TankView.coeff)), (int) (VuePlateau.PLATEAU_BORD_HAUT + 20 * TankView.coeff), (int) (280 * TankView.coeff), (int) (22 * TankView.coeff));
        this.setStringPainted(true);
        this.setFont(new Font("Andale Mono", Font.BOLD, 15));
        this.setForeground(Color.red);
        this.setBackground(Color.black);
    }

    public void refresh(int a) {
        this.setValue(a);
    }

}
