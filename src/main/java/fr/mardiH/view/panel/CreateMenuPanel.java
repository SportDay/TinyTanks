package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.CreateMenuController;
import fr.mardiH.controller.panel.ShowCreateController;
import fr.mardiH.model.BTNModel;
import fr.mardiH.model.interfaces.ViewPanels;
import fr.mardiH.util.Language;
import fr.mardiH.view.ui.CustomScrollBarUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CreateMenuPanel extends JPanel implements ViewPanels {
    private final ImageIcon background = new ImageIcon(ClassLoader.getSystemResource("Images/menu/menuBG.png"));
    private final CreateMenuController controller = new CreateMenuController(this);
    private final ShowCreateController showController = new ShowCreateController(this);
    private final CreateMenuController.BTNListener btnController = controller.new BTNListener();
    private JTextField txtNomDeLa;
    private JButton fgBTN;
    private JButton bgBTN;
    private JButton infoBTN;
    private final JButton validerBTN = new BTNModel(Language.getString("btn.validate.text"), 14);
    private final JButton annulBTN = new BTNModel(Language.getString("btn.cancel.text"), 14);
    private JCheckBox checkRaid;
    private ButtonGroup buttonGroup;
    private final JPanel choicePanelContainer = new JPanel();

    private final JScrollPane scrollPane = new JScrollPane();

    private JLayeredPane layeredPane;


    private final JLabel imageBG = new JLabel();

    private String path = null;
    private boolean custom = false;
    private boolean raid = false;


    public CreateMenuPanel() {
        init();
    }

    public CreateMenuPanel(String path) {
        custom = true;
        this.path = path;
        init();
    }

    public JCheckBox getCheckRaid() {
        return checkRaid;
    }

    public void setRaid(boolean raid) {
        this.raid = raid;
    }

    public JButton getInfoBTN() {
        return infoBTN;
    }

    public ImageIcon getBackgroundIMG() {
        return background;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JTextField getTxtNomDeLa() {
        return txtNomDeLa;
    }

    public JButton getValiderBTN() {
        return validerBTN;
    }

    public JPanel getChoicePanelContainer() {
        return choicePanelContainer;
    }

    public JButton getFgBTN() {
        return fgBTN;
    }

    public JButton getBgBTN() {
        return bgBTN;
    }

    public JButton getAnnulBTN() {
        return annulBTN;
    }

    public CreateMenuController getController() {
        return controller;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }

    public JLabel getImageBG() {
        return imageBG;
    }

    public boolean isCustom() {
        return custom;
    }

    public ShowCreateController getShowController() {
        return showController;
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        imageBG.setLayout(new BorderLayout(0, 0));
        add(imageBG);

        JPanel panel_20 = new JPanel();
        panel_20.setOpaque(false);
        imageBG.add(panel_20, BorderLayout.CENTER);
        panel_20.setBounds(new Rectangle(100, 100, 0, 0));
        panel_20.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        if (custom) {
            layeredPane = new ShowCreate(this, path);
        } else {
            layeredPane = new ShowCreate(this);
        }
        layeredPane.setOpaque(false);
        panel_20.add(layeredPane);

        controller.setLayeredPane(layeredPane);


        addComponentListener(controller.new Resize(this));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        imageBG.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        Component glue_1 = Box.createGlue();
        panel.add(glue_1);

        JPanel panel_17 = new JPanel();
        panel_17.setOpaque(false);
        panel.add(panel_17);
        panel_17.setLayout(new BoxLayout(panel_17, BoxLayout.Y_AXIS));

        Component verticalGlue = Box.createVerticalGlue();
        panel_17.add(verticalGlue);

        JPanel panel_19 = new JPanel();
        panel_19.setOpaque(false);
        panel_17.add(panel_19);
        panel_19.setLayout(new BoxLayout(panel_19, BoxLayout.X_AXIS));

        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        panel_19.add(panel_1);
        panel_1.setLayout(new GridLayout(0, 1, 0, 5));


        bgBTN = new BTNModel(Language.getString("btn.background.text"), 14);
        bgBTN.addActionListener(btnController);
        panel_1.add(bgBTN);

        fgBTN = new BTNModel(Language.getString("btn.foreground.text"), 14);
        fgBTN.addActionListener(btnController);
        fgBTN.setBorder(new LineBorder(Color.GREEN, 3, true));
        panel_1.add(fgBTN);

        infoBTN = new BTNModel(Language.getString("btn.information.text"), 14);
        infoBTN.addActionListener(btnController);
        panel_1.add(infoBTN);

        Component verticalGlue_1 = Box.createVerticalGlue();
        panel_17.add(verticalGlue_1);

        Component glue_3 = Box.createGlue();
        panel.add(glue_3);


        choicePanelContainer.setOpaque(false);
        choicePanelContainer.setLayout(new BoxLayout(choicePanelContainer, BoxLayout.Y_AXIS));

        scrollPane.setPreferredSize(new Dimension(550, 130));
        scrollPane.setFont(new Font("Arial", Font.BOLD, 16));
        scrollPane.setBorder(new TitledBorder(new LineBorder(Color.white, 2, true), Language.getString("label.foreground.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(15);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
        scrollPane.getHorizontalScrollBar().setOpaque(false);

        panel.add(scrollPane);
        scrollPane.setViewportView(choicePanelContainer);

        Component verticalGlue_4 = Box.createVerticalGlue();
        choicePanelContainer.add(verticalGlue_4);

        FGCaseChoice choicePanel = new FGCaseChoice(controller);
        buttonGroup = choicePanel.getButtonGroup();
        choicePanel.setOpaque(false);
        choicePanelContainer.add(choicePanel);
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));

        Component verticalGlue_5 = Box.createVerticalGlue();
        choicePanelContainer.add(verticalGlue_5);

        Component glue_2 = Box.createGlue();
        panel.add(glue_2);

        JPanel panel_21 = new JPanel();
        panel_21.setOpaque(false);
        panel.add(panel_21);
        panel_21.setLayout(new BoxLayout(panel_21, BoxLayout.Y_AXIS));

        Component verticalGlue_2 = Box.createVerticalGlue();
        panel_21.add(verticalGlue_2);

        JPanel panel_18 = new JPanel();
        panel_18.setOpaque(false);
        panel_21.add(panel_18);
        panel_18.setLayout(new BoxLayout(panel_18, BoxLayout.Y_AXIS));

        JPanel panel_2 = new JPanel();
        panel_2.setOpaque(false);
        panel_18.add(panel_2);
        panel_2.setLayout(new GridLayout(0, 1, 0, 5));

        JPanel panel_3 = new JPanel();
        panel_3.setOpaque(false);
        panel_2.add(panel_3);
        panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

        txtNomDeLa = new JTextField();
        txtNomDeLa.setHorizontalAlignment(SwingConstants.CENTER);
        txtNomDeLa.setText(Language.getString("text.levelname.text"));
        panel_3.add(txtNomDeLa);
        txtNomDeLa.setColumns(10);
        if (custom) {
            txtNomDeLa.setText(path);
            btnController.setOldName(path);
        }

        JPanel panel_4 = new JPanel();
        panel_4.setOpaque(false);
        panel_2.add(panel_4);
        panel_4.setLayout(new GridLayout(0, 2, 5, 0));

        annulBTN.addActionListener(btnController);
        panel_4.add(annulBTN);

        validerBTN.addActionListener(btnController);
        panel_4.add(validerBTN);

        checkRaid = new JCheckBox(Language.getString("checkbox.airraid.text")) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        checkRaid.setOpaque(false);
        checkRaid.setFocusable(false);
        checkRaid.setFocusPainted(false);
        checkRaid.setForeground(Color.WHITE);
        checkRaid.setBackground(new Color(0, 0, 0, 102));
        checkRaid.setBorder(new LineBorder(Color.BLACK, 3, true));
        checkRaid.setBorderPainted(true);
        checkRaid.setFont(new Font("Arial", Font.BOLD, 15));
        checkRaid.setSelected(raid);


        Component verticalGlue_55 = Box.createVerticalGlue();
        panel_18.add(verticalGlue_55);

        panel_18.add(checkRaid);
        checkRaid.setAlignmentX(Component.CENTER_ALIGNMENT);

        Component verticalGlue_3 = Box.createVerticalGlue();
        panel_21.add(verticalGlue_3);

        Component glue = Box.createGlue();
        panel.add(glue);
    }

    @Override
    public Container getParents() {
        return new LevelMenuPanel();
    }

    @Override
    public String getTitles() {
        return "Tiny Tanks - " + Language.getString("menu.editor.title");
    }
}