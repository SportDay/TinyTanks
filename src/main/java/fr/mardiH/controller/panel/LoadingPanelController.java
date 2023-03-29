package fr.mardiH.controller.panel;

import fr.mardiH.view.panel.LoadingPanel;
import fr.mardiH.view.panel.VuePlateau;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LoadingPanelController {

    private final JPanel source;

    public LoadingPanelController(JPanel source) {
        this.source = source;
    }


    public class Resize extends ComponentAdapter {
        private final VuePlateau plateau;


        public Resize(VuePlateau plateau) {
            this.plateau = plateau;
        }

        @Override
        public void componentResized(ComponentEvent e) {

            if (source instanceof LoadingPanel) {
                source.setBounds(plateau.getBounds());
                source.revalidate();
                source.repaint();
            }
        }
    }
}
