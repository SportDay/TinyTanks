package fr.mardiH.view.panel;

import fr.mardiH.util.Language;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {


    public LoadingPanel() {
        init();

    }


    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }


    private void init() {


        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(255, 255, 255, 96));
        Component glue = Box.createGlue();
        add(glue);
        setOpaque(false);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblNewLabel = new JLabel(Language.getString("text.wait.text"));
        lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
        lblNewLabel.setForeground(Color.WHITE);
        panel.add(lblNewLabel);

        Component verticalStrut = Box.createVerticalStrut(20);
        panel.add(verticalStrut);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(Color.BLACK);
        progressBar.setBackground(new Color(0, 0, 0, 0));
        progressBar.setBorderPainted(false);
        progressBar.setIndeterminate(true);
        panel.add(progressBar);

        Component glue_1 = Box.createGlue();
        add(glue_1);
    }
}