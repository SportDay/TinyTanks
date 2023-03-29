package fr.mardiH.model;

import fr.mardiH.view.ui.FancyProgressBar;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class AchievementsModelPanel extends JPanel {

    private final Achievements.Achievement el;
    private final JPanel panel;

    public AchievementsModelPanel(JPanel panel, Achievements.Achievement el) {
        this.panel = panel;
        this.el = el;
        Color BG_COLOR = new Color(0, 0, 0, 102);
        this.setBackground(BG_COLOR);
        init();
    }


    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private void init() {

        panel.add(this);
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(200, 200));
        this.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
        this.setMaximumSize(new Dimension(50, 50));
        this.setLayout(new BorderLayout(0, 0));


        JPanel panel_7 = new JPanel();
        panel_7.setOpaque(false);
        this.add(panel_7, BorderLayout.NORTH);
        FlowLayout fl_panel_7 = new FlowLayout(FlowLayout.CENTER, 5, 5);
        panel_7.setLayout(fl_panel_7);

        JLabel lblNewLabel_3 = new JLabel(el.getTitle());
        lblNewLabel_3.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNewLabel_3.setFont(new Font("Arial", Font.BOLD, 15));
        lblNewLabel_3.setForeground(Color.WHITE);
        panel_7.add(lblNewLabel_3);

        JLabel iconeee = new JLabel();
        if (el.isUnlocked()) {
            this.setBorder(new LineBorder(new Color(0, 255, 0), 2, true));
            iconeee.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/check.png")).getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT)));
        } else {
            this.setBorder(new LineBorder(new Color(255, 0, 0), 2, true));
            iconeee.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/closeBtn.png")).getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT)));
        }


        panel_7.add(iconeee);
        iconeee.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel panel_9 = new JPanel();
        panel_9.setOpaque(false);
        this.add(panel_9, BorderLayout.CENTER);
        panel_9.setLayout(new BoxLayout(panel_9, BoxLayout.Y_AXIS));

        JTextPane txt = new JTextPane();
        txt.setForeground(new Color(250, 250, 250));
        txt.setOpaque(false);
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        txt.setAutoscrolls(false);
        txt.setText(el.getText());
        txt.setEditable(false);
        panel_9.add(txt);

        if (el.isProgress()) {
            addProgressBar(panel_9);
        }
    }


    private void addProgressBar(JPanel p) {

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("nimbusOrange", defaults.get("nimbusBase"));

        JPanel panel_8 = new JPanel();
        panel_8.setOpaque(false);
        p.add(panel_8);
        panel_8.setLayout(new BoxLayout(panel_8, BoxLayout.X_AXIS));

        Component horizontalStrut_2 = Box.createHorizontalStrut(5);
        panel_8.add(horizontalStrut_2);

        JLabel minLabel = new JLabel(0 + "");
        minLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        minLabel.setFont(new Font("Arial", Font.BOLD, 12));
        minLabel.setForeground(new Color(239, 239, 239));
        panel_8.add(minLabel);

        Component horizontalStrut = Box.createHorizontalStrut(5);
        panel_8.add(horizontalStrut);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(239, 239, 239, 0));

        progressBar.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
        progressBar.putClientProperty("Nimbus.Overrides", defaults);

        progressBar.setBorder(null);
        progressBar.setUI(new FancyProgressBar());
//        progressBar.setUI(new GradientPalletProgressBarUI());

        progressBar.setMinimum(0);

        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);

        progressBar.setString(el.getCurrent() + " / " + el.getMax());
        panel_8.add(progressBar);
        progressBar.setAutoscrolls(true);
        progressBar.setValue((int) el.getCurrent());

        progressBar.setMaximum(el.getMax());

        Component horizontalStrut_1 = Box.createHorizontalStrut(5);
        panel_8.add(horizontalStrut_1);

        JLabel maxLabel = new JLabel(el.getMax() + "");
        maxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        maxLabel.setFont(new Font("Arial", Font.BOLD, 12));
        maxLabel.setForeground(new Color(239, 239, 239));
        panel_8.add(maxLabel);

        Component horizontalStrut_3 = Box.createHorizontalStrut(5);
        panel_8.add(horizontalStrut_3);
    }
}

