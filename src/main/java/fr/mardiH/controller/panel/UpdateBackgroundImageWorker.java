package fr.mardiH.controller.panel;

import javax.swing.*;
import java.awt.*;

public class UpdateBackgroundImageWorker extends SwingWorker {

    private final JLabel label;
    private final Image icon;

    private final int width;
    private final int height;

    public UpdateBackgroundImageWorker(JLabel label, ImageIcon icon, int width, int height) {
        this.label = label;
        this.icon = icon.getImage();
        this.width = width;
        this.height = height;
    }

    @Override
    protected Image doInBackground() {
        Image t = icon.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        label.setIcon(new ImageIcon(t));
        return t;
    }
}


