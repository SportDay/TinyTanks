package fr.mardiH.model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class BTNModel extends JButton {

    private static final Color BG_COLOR = new Color(0, 0, 0, 102);
    private static final Color FG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(0, 0, 0);

    private int textSize = 21;

    public BTNModel(Icon icon) {
        super(icon);
        styleBTN();
    }

    public BTNModel(String text, int textSize) {
        super(text);
        this.textSize = textSize;
        styleBTN();
        setToolTipText(text);
    }


    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private void styleBTN() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Arial", Font.BOLD, textSize));
        setFocusPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(BG_COLOR);
        setForeground(FG_COLOR);
        setBorder(new LineBorder(BORDER_COLOR, 3, true));
        setRolloverEnabled(false);
        setFocusable(false);
        setUI(new BasicButtonUI());
        setOpaque(false);
    }
}
