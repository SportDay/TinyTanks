package fr.mardiH.view.panel;

import fr.mardiH.Error.InvalidMapException;
import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.PlateauModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LevelIMGPanel extends JLayeredPane {

    private PlateauModel plat;

    private final JPanel fg = new JPanel();
    private final JPanel bg = new JPanel();

    private final int size = 64;

    public LevelIMGPanel(String path) {
        try {
            plat = new PlateauModel(path, true);
        } catch (InvalidMapException | IOException e) {

        }
        init();
    }

    private void init() {
        int width = 23 * size;
        int height = 16 * size;
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);
        setBounds(0, 0, width, height);

        bg.setOpaque(false);
        bg.setBounds(0, 0, width, height);
        add(bg);
        bg.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);

        bg.add(panel_1);
        panel_1.setLayout(new GridLayout(0, 23, 0, 0));

        buildCustomBG(panel_1);

        fg.setOpaque(false);
        setLayer(fg, 1);
        fg.setBounds(0, 0, width, height);
        add(fg);
        fg.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JPanel panel = new JPanel();

        panel.setOpaque(false);
        fg.add(panel);
        panel.setLayout(new GridLayout(0, 23, 0, 0));

        buildCustomFG(panel);
    }

    private void buildCustomBG(JPanel panel) {
        for (int y = 0; y < plat.getBackground().length; y++) {
            for (int x = 0; x < plat.getBackground()[y].length; x++) {
                CaseModelNew caseM = plat.getBackground()[y][x];
                JLabel lblNewLabel = new JLabel();
                panel.add(lblNewLabel);
                lblNewLabel.setIcon(new ImageIcon(caseM.getCaseType().getImage().getImage().getScaledInstance(size, size, Image.SCALE_FAST)));
            }
        }
    }

    private void buildCustomFG(JPanel panel) {
        for (int y = 0; y < plat.getForeground().length; y++) {
            for (int x = 0; x < plat.getForeground()[y].length; x++) {
                CaseModelNew caseM = plat.getForeground()[y][x];
                JLabel lblNewLabel_1 = new JLabel();
                panel.add(lblNewLabel_1);
                if (caseM.getCaseType() == CaseType.Vide) {
                    lblNewLabel_1.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/blank.png")).getImage().getScaledInstance(size, size, Image.SCALE_FAST)));
                } else {
                    lblNewLabel_1.setIcon(new ImageIcon(caseM.getCaseType().getImage().getImage().getScaledInstance(size, size, Image.SCALE_FAST)));
                }
            }
        }
    }
}
