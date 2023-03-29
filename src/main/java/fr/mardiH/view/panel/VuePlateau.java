package fr.mardiH.view.panel;

import fr.mardiH.Error.InvalidMapException;
import fr.mardiH.controller.VuePlateauController;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.Joueur;
import fr.mardiH.util.Constants;
import fr.mardiH.util.Language;
import fr.mardiH.util.StringUtils;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.IOException;


@SuppressWarnings("deprecation")
public class VuePlateau extends JPanel {
    public final static double ORIGINAL_WIDTH = 782;
    public final static double ORIGINAL_HEIGHT = 544;
    public static double PLATEAU_BORD_GAUCHE = 51;
    public static double PLATEAU_BORD_HAUT = 60;
    public static double PLATEAU_WIDTH = 782;
    public static double PLATEAU_HEIGHT = 544;
    //Modifications Pour le this mais sûrement à garder
    PlateauModel plat;
    JPanel background;
    JPanel foreground;
    JPanel gui;
    String name;
    boolean custom;
    int w;
    int h;
    Joueur tank;
    JFrame topFrame;
    Timer timer;
    private final VuePlateauController.Bonus controller;

    VuePlateau(String name, boolean custom) {
        this.name = name;
        this.custom = custom;
        this.topFrame = MainMenu.Instance; //topFrame prend la fenêtre principale (MainMenu donc)
        controller = new VuePlateauController().new Bonus(this);

        timer = new Timer(Constants.BONUS_DELAY, controller);
        timer.setInitialDelay(5000);
        timer.start();
        MainMenu.timerList.add(timer);
    }

    public boolean isCustom() {
        return custom;
    }

    public void init() {
        int w = (int) topFrame.getSize().getWidth() - 102;
        int h = (int) topFrame.getSize().getHeight() - 217;

        int[] tab = choixResp();
        w = (tab[0] / 23) * 23;
        h = (tab[1] / 16) * 16;


        try {
            if (plat == null) {
                if (custom) {
                    plat = new PlateauModel(name, true);
                } else {
                    plat = new PlateauModel(name, false);
                }
            }
        } catch (InvalidMapException | IOException e) {
            MainMenu.Instance.setLevelPanel();
            JOptionPane.showMessageDialog(MainMenu.Instance, Language.getString("text.maploaderror.text").replaceAll("%mapname%", name), Language.getString("text.maploaderror.title").replaceAll("%mapname%", name), JOptionPane.ERROR_MESSAGE);
        }
        tank = plat.getTank();
        this.setLayout(new BorderLayout());


        // Creation du plateau

        JLayeredPane pane = new JLayeredPane();
        this.add(pane);
        pane.setPreferredSize(new Dimension(topFrame.getWidth(), topFrame.getHeight()));

        // Fond du plateau
        boolean boss = plat.getBoss();
        JPanel ImageLayer = new JPanel();
        JLabel picLabel = new JLabel();
        if (boss == false) {
            ImageLayer.setLayout(new BorderLayout(0, 0));
            ImageLayer.setBounds(0, 0, topFrame.getWidth(), topFrame.getHeight());
            ImageLayer.setBackground(new ColorUIResource(241, 166, 58));

            picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/BackgroundPlateau.png")).getImage().getScaledInstance(topFrame.getWidth(), topFrame.getHeight(), Image.SCALE_SMOOTH)));
            ImageLayer.add(picLabel);
        } else {
            ImageLayer.setLayout(new BorderLayout(0, 0));
            ImageLayer.setBounds(0, 0, topFrame.getWidth(), topFrame.getHeight());
            ImageLayer.setBackground(new ColorUIResource(241, 166, 58));

            picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/BackgroundPlateauBoss.png")).getImage().getScaledInstance(topFrame.getWidth(), topFrame.getHeight(), Image.SCALE_SMOOTH)));
            ImageLayer.add(picLabel);
        }

        pane.add(ImageLayer, new Integer(1));

        //Background du plateau (Herbe, sable ...)

        background = null;
        try {
            background = createBackground(w, h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        background.setOpaque(false);
        pane.add(background, new Integer(2));


        foreground = null;
        try {
            foreground = createForeground(w, h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        foreground.setOpaque(false);
        pane.add(foreground, new Integer(3));

        // Titre du niveau

        String title = StringUtils.removeExtension(name, " ", ".yml", ".yaml");
        JLabel level = new JLabel(Language.getString("label.level.text") + " " + title, SwingConstants.CENTER);
        level.setFont(new Font("Andale Mono", Font.BOLD, 40));
        level.setBounds((topFrame.getWidth() / 2) - 250, 0, 500, 60);
        pane.add(level, new Integer(4));


        //Statistiques du niveau

        // Fond des stats
        JPanel plate = new JPanel();
        plate.setBounds(topFrame.getWidth() / 3 - 50, topFrame.getHeight() - (topFrame.getHeight() - background.getHeight() - 60 - 10), topFrame.getWidth() / 3 + 100, 100);
        plate.setBackground(new ColorUIResource(241, 166, 58));
        plate.setOpaque(false);

        picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/plate.png")).getImage().getScaledInstance(topFrame.getWidth() / 3 + 100, 100, Image.SCALE_SMOOTH)));

        plate.add(picLabel);

        pane.add(plate, new Integer(5));


        //Points de vie
        JPanel lifePoint = new JPanel();
        lifePoint.setBounds(topFrame.getWidth() / 3, topFrame.getHeight() - (topFrame.getHeight() - background.getHeight() - 60 - 10 - 20), 50, 80);
        lifePoint.setBackground(new ColorUIResource(241, 166, 58));
        lifePoint.setOpaque(false);

        picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/newHeart.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));


        lifePoint.add(picLabel);

        pane.add(lifePoint, new Integer(6));

        // RECUPER LES DONNEES DE POINTS DE VIE
        JLabel life = new JLabel("X " + MainMenu.Instance.getCampagne().getLife()); // qui sera ensuite ("X " + POINTS DE VIE );
        life.setFont(new Font("Andale Mono", Font.BOLD, 33));
        life.setBounds(topFrame.getWidth() / 3 + 60, topFrame.getHeight() - (topFrame.getHeight() - background.getHeight() - 60 - 10 - 20), 60, 60);
        pane.add(life, new Integer(6));

        // Nombre de tanks ennemis encore vivants

        JPanel nbEnnemis = new JPanel();
        nbEnnemis.setBounds(topFrame.getWidth() / 3 - 50 + plate.getWidth() - 100, topFrame.getHeight() - (topFrame.getHeight() - background.getHeight() - 60 - 10 - 20), 50, 80);
        nbEnnemis.setBackground(new ColorUIResource(241, 166, 58));
        nbEnnemis.setOpaque(false);

        picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tank_red.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));


        nbEnnemis.add(picLabel);

        pane.add(nbEnnemis, new Integer(6));

        // RECUPER LES DONNEES DE NOMBRE DE TANKS RESTANTS

        JLabel ennemis = new JLabel(plat.getNbdeBots() + " X");
        ennemis.setFont(new Font("Andale Mono", Font.BOLD, 33));
        ennemis.setBounds(topFrame.getWidth() / 3 - 50 + plate.getWidth() - 170, topFrame.getHeight() - (topFrame.getHeight() - background.getHeight() - 60 - 10 - 20), 60, 60);
        pane.add(ennemis, new Integer(6));


        // Très important, vu que le layout est null on est obligé de placer le panel sinon il s'affiche pas
        this.setBounds(0, 0, topFrame.getWidth(), topFrame.getHeight());
        PLATEAU_WIDTH = w;
        PLATEAU_HEIGHT = h;

        PLATEAU_BORD_GAUCHE = (this.getWidth() - w) / 2;
        TankView.setCoeff(w / ORIGINAL_WIDTH);
        MissileView.setCoeff(w / ORIGINAL_WIDTH);
    }

    public void resize() {
        this.removeAll();
        init();
        topFrame.revalidate();

    }


    public int[] choixResp() {
        int newWidth = (int) topFrame.getSize().getWidth() - 102;
        int newHeight = (int) topFrame.getSize().getHeight() - 217;


        double z = (newWidth * 100 / newHeight * 100) * 0.0001;
        double y = (782 * 100 / 544 * 100) * 0.0001;

        if ((newWidth * 100 / newHeight * 100) * 0.0001 > y) {
            double tmp = newHeight * y;
            newWidth = (int) tmp;
        } else if ((newWidth * 100 / newHeight * 100) * 0.0001 < y) {
            double tmp2 = newWidth / y;
            newHeight = (int) tmp2;
        }


        int[] tab = new int[2];
        tab[0] = newWidth;
        tab[1] = newHeight;

        return tab;

    }


    public JPanel createBackground(int a, int b) throws IOException {
        JPanel plateau = new JPanel();
        plateau.setBackground(new Color(255, 229, 154, 0));
        plateau.setPreferredSize(new Dimension(a, b));
        plateau.setBounds((topFrame.getWidth() - a) / 2, 60, a, b);
        for (int i = 0; i < plat.getBackground().length; i++) {
            for (int y = 0; y < plat.getBackground()[i].length; y++) {
                ImageIcon i1 = plat.getBackground()[i][y].getCaseType().getImage();
                Image newImage = i1.getImage().getScaledInstance(a / 23, b / 16, Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(newImage);
                JLabel jlabel = new JLabel(icon, JLabel.CENTER);
                plat.getBackground()[i][y].setLabel(jlabel);
                plateau.add(jlabel);
            }
        }
        plateau.setLayout(new GridLayout(16, 23, 0, 0));
        return plateau;
    }

    public JPanel createForeground(int a, int b) throws IOException {
        JPanel plateau = new JPanel();
        plateau.setBackground(new Color(255, 229, 154));
        plateau.setPreferredSize(new Dimension(a, b));
        plateau.setBounds((topFrame.getWidth() - a) / 2, 60, a, b);
        for (int i = 0; i < plat.getForeground().length; i++) {
            for (int y = 0; y < plat.getForeground()[i].length; y++) {
                ImageIcon i1 = plat.getForeground()[i][y].getCaseType().getImage();
                if (plat.getForeground()[i][y].getCaseType() == CaseType.Joueur || plat.getForeground()[i][y].getCaseType() == CaseType.Bot || plat.getForeground()[i][y].getCaseType() == CaseType.Boss) {
                    i1 = CaseType.Vide.getImage();
                    plat.getForeground()[i][y].setCaseType(CaseType.Vide);
                }
                Image newImage = i1.getImage().getScaledInstance(a / 23, b / 16, Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(newImage);
                JLabel jlabel = new JLabel(icon, JLabel.CENTER);
                plat.getForeground()[i][y].setLabel(jlabel);
                plateau.add(jlabel);
            }
        }
        plateau.setLayout(new GridLayout(16, 23, 0, 0));
        return plateau;
    }


    public JPanel ChooseBackground() {
        boolean boss = false;
        for (int i = 0; i < plat.getForeground().length; i++) {
            for (int y = 0; y < plat.getForeground()[i].length; y++) {
                if (plat.getForeground()[i][y].getCaseType() == CaseType.Boss) {
                    boss = true;
                    break;
                }
            }
        }
        JPanel ImageLayer = new JPanel();
        if (boss == false) {
            ImageLayer.setLayout(new BorderLayout(0, 0));
            ImageLayer.setBounds(0, 0, topFrame.getWidth(), topFrame.getHeight());
            ImageLayer.setBackground(new ColorUIResource(241, 166, 58));

            JLabel picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/BackgroundPlateau.png")).getImage().getScaledInstance(topFrame.getWidth(), topFrame.getHeight(), Image.SCALE_SMOOTH)));
            ImageLayer.add(picLabel);
        } else {
            ImageLayer.setLayout(new BorderLayout(0, 0));
            ImageLayer.setBounds(0, 0, topFrame.getWidth(), topFrame.getHeight());
            ImageLayer.setBackground(new ColorUIResource(241, 166, 58));

            JLabel picLabel = new JLabel(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/BackgroundPlateauBoss.png")).getImage().getScaledInstance(topFrame.getWidth(), topFrame.getHeight(), Image.SCALE_SMOOTH)));
            ImageLayer.add(picLabel);
        }
        return ImageLayer;
    }


    public JPanel getForegroundPanel() {
        return foreground;
    }

    public PlateauModel getPlat() {
        return plat;
    }


}
