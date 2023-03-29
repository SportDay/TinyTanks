package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.LevelMenuController;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.util.StringUtils;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.ui.CustomScrollBarUI;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static fr.mardiH.Main.logger;

public class LevelMenuPanel extends JPanel implements ViewPanels {

    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private final LevelMenuController controller;
    LinkedHashMap<JLabel, JPanel> levelLabel = new LinkedHashMap<>();
    LinkedList<JLabel> editorList = new LinkedList<>();
    LinkedList<JPanel> levelList = new LinkedList<>();
    LinkedHashMap<JLabel, JPanel> delList = new LinkedHashMap<>();
    private final JLabel imageBG = new JLabel();
    private LevelMenuController.MouseInputs controllerMouse;
    private final JLabel backBTN = new JLabel();
    private final JLabel createMenu = new JLabel();

    public LevelMenuPanel() {
        controller = new LevelMenuController();
        controller.new CreateImage(this);
        initPanel();
    }

    public LinkedList<JLabel> getEditorList() {
        return editorList;
    }

    public LinkedHashMap<JLabel, JPanel> getDelList() {
        return delList;
    }

    public JLabel getBackBTN() {
        return backBTN;
    }

    public JLabel getCreateMenu() {
        return createMenu;
    }

    public JLabel getImageBG() {
        return imageBG;
    }

    public LinkedHashMap<JLabel, JPanel> getLevelLabel() {
        return levelLabel;
    }

    public LinkedList<JPanel> getLevelList() {
        return levelList;
    }

    public ImageIcon getBackgroundIMG() {
        return background;
    }


    private void initPanel() {
        setLayout(new BorderLayout(0, 0));
        imageBG.setLayout(new BorderLayout(0, 0));

        LevelMenuController.MouseInputs mouseInputs = controller.new MouseInputs(this);

        addComponentListener(controller.new Resize(this));

        add(imageBG);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setFont(new Font("Arial", Font.PLAIN, 14));
        imageBG.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        panel_1.setFont(new Font("Arial", Font.PLAIN, 14));
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
        panel.add(panel_1, BorderLayout.NORTH);

        createMenu.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/customLevelIcon2.png"))).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        createMenu.addMouseListener(mouseInputs);
        createMenu.setName("Images/menu/customLevelIcon2.png");
        createMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createMenu.setToolTipText(Language.getString("btn.editor.text"));
        panel_1.add(createMenu);

        Component horizontalGlue = Box.createHorizontalGlue();
        panel_1.add(horizontalGlue);

        backBTN.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/closeBtn.png"))).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        backBTN.addMouseListener(mouseInputs);
        backBTN.setName("Images/menu/closeBtn.png");
        backBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBTN.setToolTipText(Language.getString("btn.back.text"));
        panel_1.add(backBTN);

        JPanel panel_2 = new JPanel();
        panel_2.setOpaque(false);
        panel_2.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(panel_2);
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

        JPanel panel_3 = new JPanel();
        panel_3.setFont(new Font("Arial", Font.PLAIN, 14));
        panel_3.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setFont(new Font("Arial", Font.BOLD, 24));
        scrollPane.setBorder(new TitledBorder(new LineBorder(Color.white, 4, true), Language.getString("label.levelchoice.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.WHITE));
        scrollPane.setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(15);
        panel_2.add(scrollPane);
        scrollPane.setViewportView(panel_3);
        scrollPane.getViewport().setOpaque(false);
        panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setOpaque(false);


        JPanel panel_6 = new JPanel();
        panel_6.setOpaque(false);
        panel_3.add(panel_6);
        panel_6.setLayout(new GridLayout(0, 3, 8, 8));
        controllerMouse = controller.new MouseInputs(this, panel_6);

        ClassLoader cl = getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources = new Resource[0];
        try {
            resources = resolver.getResources("classpath*:/Maps/*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalPrivateLevel = 0;
        for (Resource resource : resources) {
            if (resource.getFilename().endsWith(".yaml") || resource.getFilename().endsWith(".yml")) {
                logger.info("Find level: " + resource.getFilename());
                addLevel(panel_6, resource.getFilename(), false);
                totalPrivateLevel++;
            }
        }
        MainMenu.totalPrivateLevel = totalPrivateLevel;
        LinkedList<String> map = PlateauModel.mapDispo();
        for (String entry : map) {
            // logger.info("Find level: " + entry);
            addLevel(panel_6, entry, true);
        }

    }

    private void addLevel(JPanel panel, String f, boolean custom) {
        Color BG_COLOR = new Color(0, 0, 0, 102);

        JPanel panel_4 = new JPanel();
        panel_4.setOpaque(false);
        panel.add(panel_4);
        panel_4.setFont(new Font("Arial", Font.PLAIN, 14));
        panel_4.setBorder(new LineBorder(Color.GREEN, 2, true));
        panel_4.setLayout(new BorderLayout(0, 0));


        if (custom) {
            JPanel panel_7 = new JPanel();
            panel_7.setBackground(BG_COLOR);
            panel_4.add(panel_7, BorderLayout.NORTH);
            panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.X_AXIS));
            panel_7.setBorder(new MatteBorder(0, 0, 2, 0, new Color(0, 0, 0)));

            JLabel lblNewLabel_3 = new JLabel();
            lblNewLabel_3.setName(f);
            lblNewLabel_3.addMouseListener(controllerMouse);
            lblNewLabel_3.setToolTipText(Language.getString("btn.edit.text"));
            lblNewLabel_3.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/customLevelIcon2.png"))).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
            panel_7.add(lblNewLabel_3);
            lblNewLabel_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            Component horizontalGlue_2 = Box.createHorizontalGlue();
            panel_7.add(horizontalGlue_2);

            JLabel lblNewLabel_4 = new JLabel();
            lblNewLabel_4.addMouseListener(controllerMouse);
            lblNewLabel_4.setName(f);
            lblNewLabel_4.setToolTipText(Language.getString("btn.delete.text"));
            lblNewLabel_4.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/closeBtn.png"))).getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
            panel_7.add(lblNewLabel_4);
            lblNewLabel_4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            editorList.add(lblNewLabel_3);
            delList.put(lblNewLabel_4, panel_4);
        }

        JPanel panel_9 = new JPanel();
        panel_9.addMouseListener(controllerMouse);
        panel_9.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel_9.setOpaque(false);
        panel_4.add(panel_9, BorderLayout.CENTER);
        panel_9.setLayout(new BorderLayout(0, 0));
        panel_4.setName(f + "!" + true + "!" + custom);
        panel_9.setName(f + "!" + true + "!" + custom);

        JLabel levelLogo = new JLabel();
        levelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLogo.setIconTextGap(1);

        levelLogo.setHorizontalAlignment(SwingConstants.CENTER);
        levelLogo.setName("Images/menu/customLevelIcon.png");
        levelLogo.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(("Images/menu/customLevelIcon.png"))).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        levelLogo.setLayout(new GridLayout(0, 1, 0, 0));
        panel_9.add(levelLogo, BorderLayout.CENTER);

        levelLabel.put(levelLogo, panel_4);
        levelList.add(panel_9);


        String names = f;

        if (!custom) {
            String[] name = f.split(" ");
            if (name.length == 2) {
                name[1] = StringUtils.removeExtension(name[1], "", ".yml", ".yaml");
                names = name[0] + " " + name[1];

                int currentLevel = MainMenu.Instance.getCampagne().getLevelNum();
                if (currentLevel < Integer.parseInt(name[1])) {
                    panel_4.setBorder(new LineBorder(Color.BLACK, 2, true));
                    panel_9.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    panel_4.setName(f + "!" + false + "!" + custom);
                    panel_9.setName(f + "!" + false + "!" + custom);
                }

            }
        }
        names = StringUtils.removeExtension(names, "", ".yml", ".yaml");

        JPanel panel_8 = new JPanel();
        panel_8.setBackground(BG_COLOR);
        panel_9.add(panel_8, BorderLayout.SOUTH);
        panel_8.setBorder(new MatteBorder(2, 0, 0, 0, new Color(0, 0, 0)));

        JLabel levelNum = new JLabel(names);
        levelNum.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelNum.setIconTextGap(1);
        levelNum.setFont(new Font("Arial", Font.BOLD, 15));
        levelNum.setForeground(Color.orange);
        levelNum.setHorizontalAlignment(SwingConstants.CENTER);
        panel_8.add(levelNum);
    }

    @Override
    public Container getParents() {
        return new MainMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("label.levelchoice.text");
    }
}
