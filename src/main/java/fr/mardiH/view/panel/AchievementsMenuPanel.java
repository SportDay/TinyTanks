package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.AchievementsMenuController;
import fr.mardiH.model.AchievementsModelPanel;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.ui.CustomScrollBarUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.LinkedList;

public class AchievementsMenuPanel extends JPanel implements ViewPanels {

    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private final AchievementsMenuController controller;
    LinkedList<JPanel> achiList = new LinkedList<>();
    private final JLabel imageBG = new JLabel();
    private AchievementsMenuController.MouseInputs controllerMouse;
    private final JLabel backBTN = new JLabel();

    public AchievementsMenuPanel() {
        controller = new AchievementsMenuController();
        initPanel();
    }

    public LinkedList<JPanel> getAchiList() {
        return achiList;
    }


    public JLabel getBackBTN() {
        return backBTN;
    }


    public JLabel getImageBG() {
        return imageBG;
    }

    public ImageIcon getBackgroundIMG() {
        return background;
    }


    private void initPanel() {
        setLayout(new BorderLayout(0, 0));
        imageBG.setLayout(new BorderLayout(0, 0));

        AchievementsMenuController.MouseInputs mouseInputs = controller.new MouseInputs(this);

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
        scrollPane.setBorder(new TitledBorder(new LineBorder(Color.white, 4, true), Language.getString("label.achi.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.WHITE));
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
        controllerMouse = controller.new MouseInputs(this);

        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getNbrKill()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getNbrDie()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getPlayTime()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getNoDie()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getCampagne()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getViewCredits()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getMakeMap()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getPlaneSurv()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getBotKillBot()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getNoBullet()));
        addAchievements(new AchievementsModelPanel(panel_6, MainMenu.Instance.getAchievements().getEndGame()));


    }

    private void addAchievements(AchievementsModelPanel panel) {
        achiList.add(panel);
    }


    @Override
    public Container getParents() {
        return new MainMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.achi.title");
    }
}
