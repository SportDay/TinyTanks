package fr.mardiH.model;

import fr.mardiH.controller.panel.CreateMenuController;
import fr.mardiH.model.Enum.CaseType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.InputEvent;

public class CreatorMapJRadioButtonModel extends JRadioButton {

    private static final Color BG_COLOR = new Color(0, 0, 0, 64);
    private static final Color FG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0, 0, 0);

    private CaseType type;
    private CreateMenuController controller;

    public CreatorMapJRadioButtonModel(CaseType type, CreateMenuController controller) {
        init();
        setController(controller);
        setTypeImage(type);
        setOpaque(false);
    }

    public CaseType getType() {
        return type;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private void init() {
        setBorder(new LineBorder(BORDER_COLOR, 2, true));
        setBorderPainted(true);
        setRolloverEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setVerticalTextPosition(JRadioButton.BOTTOM);
        setHorizontalTextPosition(JRadioButton.CENTER);
        setForeground(FG_COLOR);
        setBackground(BG_COLOR);
        setFont(new Font("Arial", Font.BOLD, getFont().getSize()));
        setFocusPainted(false);
        setHideActionText(true);
        setFocusable(false);


    }


    public void setController(CreateMenuController controller) {
        this.controller = controller;
        addChangeListener(controller.new JRadio());
    }

    public void setTypeImage(CaseType type) {
        this.type = type;
        setIcon(type.getImage());
        setActionCommand(type.toString());
        if (type.getLettre() == ' ') {
            setText(type.getName() + " (%r%)");
            setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/closeBtn.png")));
            setToolTipText(type.getName());
        } else {
            setText(type.getName() + " (" + String.valueOf(type.getLettre()).toUpperCase() + ")");
            setToolTipText(type.getName() + " (" + String.valueOf(type.getLettre()).toUpperCase() + ")");
        }
//        setMnemonic(type.getLettre());
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(type.getLettre(), InputEvent.CTRL_DOWN_MASK);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "paintLevel");
        getActionMap().put("paintLevel", controller.new Action(this));
    }
}
