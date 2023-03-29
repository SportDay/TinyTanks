package fr.mardiH.view.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Dictionary;
import java.util.Enumeration;

public class CustomSliderUI extends BasicSliderUI {

    private final BasicStroke stroke = new BasicStroke(4f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND, 10f, null, 0f);

    public CustomSliderUI(JSlider b) {
        super(b);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(16, 20);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(stroke);
        g2d.setPaint(new Color(255, 255, 255, 191));
        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
            g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
        } else {
            g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y, trackRect.x + trackRect.width / 2, trackRect.y + trackRect.height);
        }
        g2d.setStroke(old);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x1 = thumbRect.x + 2;
        int x2 = thumbRect.x + thumbRect.width - 2;
        int width = thumbRect.width - 4;
        int topY = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        shape.moveTo(x1, topY);
        shape.lineTo(x2, topY);
        shape.lineTo((x1 + x2) / 2, topY + width);
        shape.closePath();
        g2d.setPaint(Color.DARK_GRAY);
        g2d.fill(shape);
        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2f));
        g2d.setPaint(Color.WHITE);
        g2d.draw(shape);
        g2d.setStroke(old);
    }

    public void paintTicks(Graphics g) {
        Rectangle tickBounds = tickRect;

        g.setColor(Color.WHITE);

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            g.translate(0, tickBounds.y);

            if (slider.getMinorTickSpacing() > 0) {
                int value = slider.getMinimum();

                while (value <= slider.getMaximum()) {
                    int xPos = xPositionForValue(value);
                    paintMinorTickForHorizSlider(g, tickBounds, xPos);

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMinorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMinorTickSpacing();
                }
            }

            if (slider.getMajorTickSpacing() > 0) {
                int value = slider.getMinimum();

                while (value <= slider.getMaximum()) {
                    int xPos = xPositionForValue(value);
                    paintMajorTickForHorizSlider(g, tickBounds, xPos);

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMajorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMajorTickSpacing();
                }
            }

            g.translate(0, -tickBounds.y);
        } else {
            g.translate(tickBounds.x, 0);

            if (slider.getMinorTickSpacing() > 0) {
                int value = slider.getMinimum();

                while (value <= slider.getMaximum()) {
                    int yPos = yPositionForValue(value);
                    paintMinorTickForVertSlider(g, tickBounds, yPos);

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMinorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMinorTickSpacing();
                }


            }

            if (slider.getMajorTickSpacing() > 0) {

                int value = slider.getMinimum();

                while (value <= slider.getMaximum()) {
                    int yPos = yPositionForValue(value);
                    paintMajorTickForVertSlider(g, tickBounds, yPos);

                    // Overflow checking
                    if (Integer.MAX_VALUE - slider.getMajorTickSpacing() < value) {
                        break;
                    }

                    value += slider.getMajorTickSpacing();
                }


            }
            g.translate(-tickBounds.x, 0);
        }
    }

    /**
     * Paints minor tick for horizontal slider.
     *
     * @param g          the graphics
     * @param tickBounds the tick bounds
     * @param x          the x coordinate
     */
    protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
        g.drawLine(x, 0, x, tickBounds.height / 2 - 1);
    }

    /**
     * Paints major tick for horizontal slider.
     *
     * @param g          the graphics
     * @param tickBounds the tick bounds
     * @param x          the x coordinate
     */
    protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
        g.drawLine(x, 0, x, tickBounds.height - 2);
    }

    /**
     * Paints minor tick for vertical slider.
     *
     * @param g          the graphics
     * @param tickBounds the tick bounds
     * @param y          the y coordinate
     */
    protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
        g.drawLine(0, y, tickBounds.width / 2 - 1, y);
    }

    /**
     * Paints major tick for vertical slider.
     *
     * @param g          the graphics
     * @param tickBounds the tick bounds
     * @param y          the y coordinate
     */
    protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
        g.drawLine(0, y, tickBounds.width - 2, y);
    }

    /**
     * Paints the labels.
     *
     * @param g the graphics
     */
    public void paintLabels(Graphics g) {
        Rectangle labelBounds = labelRect;

        @SuppressWarnings("rawtypes")
        Dictionary dictionary = slider.getLabelTable();
        if (dictionary != null) {
            Enumeration<?> keys = dictionary.keys();
            int minValue = slider.getMinimum();
            int maxValue = slider.getMaximum();
            boolean enabled = slider.isEnabled();
            while (keys.hasMoreElements()) {
                Integer key = (Integer) keys.nextElement();
                int value = key.intValue();
                if (value >= minValue && value <= maxValue) {
                    JComponent label = (JComponent) dictionary.get(key);
                    label.setForeground(Color.WHITE);
                    label.setEnabled(enabled);

                    if (label instanceof JLabel) {
                        Icon icon = label.isEnabled() ? ((JLabel) label).getIcon() : ((JLabel) label).getDisabledIcon();

                        if (icon instanceof ImageIcon) {
                            // Register Slider as an image observer. It allows to catch notifications about
                            // image changes (e.g. gif animation)
                            Toolkit.getDefaultToolkit().checkImage(((ImageIcon) icon).getImage(), -1, -1, slider);
                        }
                    }

                    if (slider.getOrientation() == JSlider.HORIZONTAL) {
                        g.translate(0, labelBounds.y);
                        paintHorizontalLabel(g, value, label);
                        g.translate(0, -labelBounds.y);
                    } else {
                        int offset = 0;
                        g.translate(labelBounds.x + offset, 0);
                        paintVerticalLabel(g, value, label);
                        g.translate(-labelBounds.x - offset, 0);
                    }
                }
            }
        }

    }

    /**
     * Called for every label in the label table. Used to draw the labels for
     * horizontal sliders. The graphics have been translated to labelRect.y
     * already.
     *
     * @param g     the graphics context in which to paint
     * @param value the value of the slider
     * @param label the component label in the label table that needs to be
     *              painted
     * @see JSlider#setLabelTable
     */
    protected void paintHorizontalLabel(Graphics g, int value, Component label) {
        int labelCenter = xPositionForValue(value);
        int labelLeft = labelCenter - (label.getPreferredSize().width / 2);
        g.translate(labelLeft, 0);
        label.paint(g);
        g.translate(-labelLeft, 0);
    }

    /**
     * Called for every label in the label table. Used to draw the labels for
     * vertical sliders. The graphics have been translated to labelRect.x
     * already.
     *
     * @param g     the graphics context in which to paint
     * @param value the value of the slider
     * @param label the component label in the label table that needs to be
     *              painted
     * @see JSlider#setLabelTable
     */
    protected void paintVerticalLabel(Graphics g, int value, Component label) {
        int labelCenter = yPositionForValue(value);
        int labelTop = labelCenter - (label.getPreferredSize().height / 2);
        g.translate(0, labelTop);
        label.paint(g);
        g.translate(0, -labelTop);
    }

}