package fr.mardiH.controller.panel;

import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.EndPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class EndPanelController implements ActionListener, ComponentListener {

    private boolean play = true;

    public EndPanelController(EndPanel panel) {
        Timer timer = new Timer(700, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (play) {
                    MainMenu.Instance.getSound().end();
                    play = false;
                }
                panel.update();
                if (panel.getIndex() >= panel.getText().length()) {
                    ((Timer) e.getSource()).stop();

                    MainMenu.Instance.setCreditsPanel();
                }
            }
        });
        timer.setInitialDelay(200);
        timer.start();
        MainMenu.timerList.add(timer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

}
