package fr.mardiH.view.panel;

import fr.mardiH.Error.InvalidMapException;
import fr.mardiH.controller.panel.ShowCreateController;
import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class ShowCreate extends JLayeredPane {

    private final int size = 37;
    private final LinkedList<JLabel> bgLabel = new LinkedList<>();
    private final LinkedList<JLabel> fgLabel = new LinkedList<>();
    private final JPanel source;
    private final JPanel fg = new JPanel();
    private final JPanel bg = new JPanel();
    private final ShowCreateController controller;
    private final ShowCreateController.MouseInputs controllerMouse;
    private String path = null;
    private boolean custom = false;
    private PlateauModel plat;

    public ShowCreate(JPanel source) {
        this.source = source;
        controller = new ShowCreateController(source);
        controllerMouse = controller.new MouseInputs(this);
        init();
    }
    public ShowCreate(JPanel source, String path) {
        this.source = source;
        controller = new ShowCreateController(source);
        controllerMouse = controller.new MouseInputs(this);
        this.path = path;
        custom = true;
        try {
            plat = new PlateauModel(path, true);
        } catch (InvalidMapException | IOException e) {
            MainMenu.Instance.setMainMenu();
        }
        ((CreateMenuPanel) source).setRaid(plat.isRaid());
        init();
    }

    public JPanel getFg() {
        return fg;
    }

    public JPanel getBg() {
        return bg;
    }

    public int getSizaCell() {
        return size;
    }

    public LinkedList<JLabel> getBgLabel() {
        return bgLabel;
    }

    public LinkedList<JLabel> getFgLabel() {
        return fgLabel;
    }

    private void init() {
        setPreferredSize(new Dimension((24 * 64), (17 * 64)));
        setBounds(new Rectangle(0, 0, 0, 0));

        bg.setOpaque(false);
        bg.setBounds(0, 0, (24 * 64), (17 * 64));
        add(bg);
        bg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);

        bg.add(panel_1);
        panel_1.setLayout(new GridLayout(0, 23, 0, 0));

        if (custom) {
            buildCustomBG(panel_1);
        } else {
            buildBG(panel_1);
        }

        fg.setOpaque(false);
        setLayer(fg, 1);
        fg.setBounds(0, 0, (24 * 64), (17 * 64));
        add(fg);
        fg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel = new JPanel();

        panel.setOpaque(false);
        fg.add(panel);
        panel.setLayout(new GridLayout(0, 23, 0, 0));

        if (custom) {
            buildCustomFG(panel);
        } else {
            buildFG(panel);
        }

    }

    private void buildCustomBG(JPanel panel) {
        for (int y = 0; y < plat.getBackground().length; y++) {
            for (int x = 0; x < plat.getBackground()[y].length; x++) {
                CaseModelNew caseM = plat.getBackground()[y][x];
                JLabel lblNewLabel = new JLabel();
                panel.add(lblNewLabel);
                lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                lblNewLabel.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
                lblNewLabel.setIcon(caseM.getCaseType().getImage());
                ((ImageIcon) lblNewLabel.getIcon()).setDescription(caseM.getCaseType().getImage().getDescription());
                lblNewLabel.setName(caseM.getCaseType().getLettre() + "");
                bgLabel.add(lblNewLabel);
                if (source instanceof CreateMenuPanel) {
                    lblNewLabel.addMouseListener(controllerMouse);
                }
            }
        }
    }

    private void buildCustomFG(JPanel panel) {
        for (int y = 0; y < plat.getForeground().length; y++) {
            for (int x = 0; x < plat.getForeground()[y].length; x++) {
                CaseModelNew caseM = plat.getForeground()[y][x];
                JLabel lblNewLabel_1 = new JLabel();
                panel.add(lblNewLabel_1);
                lblNewLabel_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));

                lblNewLabel_1.setIcon(caseM.getCaseType().getImage());
                ((ImageIcon) lblNewLabel_1.getIcon()).setDescription(caseM.getCaseType().getImage().getDescription());


                if (caseM.getCaseType() == CaseType.Boss) {
                    controller.setBoss(lblNewLabel_1);
                }
                if (caseM.getCaseType() == CaseType.Joueur) {
                    controller.setPlayer(lblNewLabel_1);
                }
                lblNewLabel_1.setName(caseM.getCaseType().getLettre() + "");
                fgLabel.add(lblNewLabel_1);
                lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (source instanceof CreateMenuPanel) {
                    lblNewLabel_1.addMouseListener(controllerMouse);
                }
            }
        }
    }

    private void buildBG(JPanel panel) {
        for (int x = 0; x < 23 * 16; x++) {
            JLabel lblNewLabel = new JLabel();
            panel.add(lblNewLabel);
            int rnd = new Random((System.currentTimeMillis() * System.nanoTime()) / 5).nextInt(2) + 1;
            lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblNewLabel.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
            lblNewLabel.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tileGrass" + rnd + ".png")));
            ((ImageIcon) lblNewLabel.getIcon()).setDescription("Images/PNG/Retina/tileGrass" + rnd + ".png");
            lblNewLabel.setName("H");
            bgLabel.add(lblNewLabel);
            if (source instanceof CreateMenuPanel) {
                lblNewLabel.addMouseListener(controllerMouse);
            }
        }
    }

    private void buildFG(JPanel panel) {
        for (int x = 0; x < 23 * 16; x++) {
            JLabel lblNewLabel_1 = new JLabel();
            panel.add(lblNewLabel_1);
            lblNewLabel_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
            lblNewLabel_1.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/blank.png")));
            ((ImageIcon) lblNewLabel_1.getIcon()).setDescription("Images/menu/blank.png");
            lblNewLabel_1.setName(" ");
            fgLabel.add(lblNewLabel_1);
            lblNewLabel_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (source instanceof CreateMenuPanel) {
                lblNewLabel_1.addMouseListener(controllerMouse);
            }
        }
    }
}