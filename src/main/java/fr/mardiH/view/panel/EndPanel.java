package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.EndPanelController;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel implements ViewPanels {

    private final JLabel label = new JLabel();
    private final String text = Language.getString("menu.end.title");
    private int index = 0;

    public EndPanel() {
        init();
    }

    private void init() {
        EndPanelController controller = new EndPanelController(this);
        addComponentListener(controller);

        setLayout(new BorderLayout(0, 0));
        setBackground(Color.BLACK);
        add(label);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Ink Free", Font.BOLD, 50));
    }

    public void update() { // Affiche les lettres une par une
        String labelText = label.getText();
        labelText += text.charAt(index);
        label.setText(labelText);
        index++;
    }

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Container getParents() {
        return new MainMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.end.title");
    }

}
