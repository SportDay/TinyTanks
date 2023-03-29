package fr.mardiH.controller.panel;

import fr.mardiH.model.CaseImgNameType;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.CreateMenuPanel;
import fr.mardiH.view.panel.ShowCreate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShowCreateController {

    private final JPanel source;

    private int caseSize = 0;

    private JLabel player;
    private JLabel boss;

    public ShowCreateController(JPanel source) {
        this.source = source;
    }

    public void setPlayer(JLabel player) {
        this.player = player;
    }

    public void setBoss(JLabel boss) {
        this.boss = boss;
    }

    public class UpdateIcon extends SwingWorker {

        private final JLayeredPane pane;

        public UpdateIcon(JLayeredPane pane) {
            this.pane = pane;
        }

        @Override
        protected Object doInBackground() {

            if (pane != null) {
                if (pane instanceof ShowCreate) {
                    if (source instanceof CreateMenuPanel) {
                        int newSize = (((ShowCreate) pane).getSizaCell() * source.getWidth()) / MainMenu.Instance.getMinWidth();
                        int newSize1 = (((ShowCreate) pane).getSizaCell() * source.getHeight()) / MainMenu.Instance.getMinHeight();
                        caseSize = Integer.min(newSize, newSize1);
                    }

                    pane.setPreferredSize(new Dimension((25 * caseSize), (17 * caseSize + 5)));
                    ((ShowCreate) pane).getBg().setBounds(0, 0, 25 * caseSize, (17 * caseSize + 5));
                    ((ShowCreate) pane).getFg().setBounds(0, 0, 25 * caseSize, (17 * caseSize + 5));

                    SwingWorker bg = new SwingWorker() {
                        @Override
                        protected Object doInBackground() {
                            for (JLabel label : ((ShowCreate) pane).getBgLabel()) {
                                String path = ((ImageIcon) label.getIcon()).getDescription();
                                label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(path)).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_FAST)));
                                ((ImageIcon) label.getIcon()).setDescription(path);
                            }
                            return null;
                        }
                    };

                    SwingWorker fg = new SwingWorker() {
                        @Override
                        protected Object doInBackground() {
                            for (JLabel label : ((ShowCreate) pane).getFgLabel()) {
                                String path = ((ImageIcon) label.getIcon()).getDescription();
                                label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(path)).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_FAST)));
                                ((ImageIcon) label.getIcon()).setDescription(path);
                            }
                            return null;
                        }
                    };
                    bg.execute();
                    fg.execute();
                }
            }
            return null;
        }
    }


    public class MouseInputs extends MouseAdapter {

        boolean click = false;
        private final JLayeredPane pane;

        public MouseInputs(JLayeredPane pane) {
            this.pane = pane;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (click) {
                changeCase(e);
            }
        }


        @Override
        public void mousePressed(MouseEvent e) {
            click = true;
            changeCase(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            click = false;
        }

        private void changeCase(MouseEvent e) {
            if (e.getSource() instanceof JLabel && source instanceof CreateMenuPanel && pane instanceof ShowCreate) {
                if (((CreateMenuPanel) source).getButtonGroup() != null && ((CreateMenuPanel) source).getButtonGroup().getSelection() != null) {
                    ButtonModel btn = ((CreateMenuPanel) source).getButtonGroup().getSelection();
                    CaseImgNameType type = CaseImgNameType.parse(btn.getActionCommand());
                    if (type != null) {
                        JLabel label = ((JLabel) e.getSource());
                        if (label.getName() != type.getType()) {
                            int newSize = (((ShowCreate) pane).getSizaCell() * source.getWidth()) / MainMenu.Instance.getMinWidth();
                            int newSize1 = (((ShowCreate) pane).getSizaCell() * source.getHeight()) / MainMenu.Instance.getMinHeight();
                            caseSize = Integer.min(newSize, newSize1);
                            label.setName(type.getType());
                            label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource(type.getImgPath())).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_DEFAULT)));
                            ((ImageIcon) label.getIcon()).setDescription(type.getImgPath());
                            if (type.getType().equalsIgnoreCase("J") && player != label) {
                                if (player != null) {
                                    player.setName(" ");
                                    player.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/blank.png")).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_DEFAULT)));
                                    ((ImageIcon) player.getIcon()).setDescription("Images/menu/blank.png");
                                }
                                player = label;
                            } else if (type.getType().equalsIgnoreCase("Z") && boss != label) {
                                if (boss != null) {
                                    boss.setName(" ");
                                    boss.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/blank.png")).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_DEFAULT)));
                                    ((ImageIcon) boss.getIcon()).setDescription("Images/menu/blank.png");
                                }
                                boss = label;
                            } else if (type.getType().equalsIgnoreCase(" ")) {
                                label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/blank.png")).getImage().getScaledInstance(caseSize, caseSize, Image.SCALE_DEFAULT)));
                                ((ImageIcon) label.getIcon()).setDescription("Images/menu/blank.png");
                            }
                        }
                    }
                }
            }
        }

    }


}
