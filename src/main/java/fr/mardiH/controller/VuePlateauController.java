package fr.mardiH.controller;

import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.Enum.BonusType;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.interfaces.CaseM;
import fr.mardiH.util.Constants;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.VuePlateau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Random;

public class VuePlateauController {
    private final JFrame parent = MainMenu.Instance;


    public class Bonus implements ActionListener {

        VuePlateau plat;
        int repeat = 0;
        long trySpawn = 0;

        HashMap<CaseModelNew, Timer> bonusList = new HashMap<>();
        long spawn = 0;
        long warn = 0;
        long blink = 0;
        long die = 0;
        public Bonus(VuePlateau plat) {
            this.plat = plat;
        }

        private void genereateRandomBonus() {
            if (plat.getPlat().getForeground() != null && repeat == 0) {
                SwingWorker gen = new SwingWorker() {
                    @Override
                    protected Object doInBackground() {
                        CaseModelNew[][] fg = plat.getPlat().getForeground();
                        int bonusNum = getRandomNum(3);
                        for (int i = 0; i < bonusNum; i++) {
                            genereateRandomBonus(fg);
                        }
                        plat.repaint();
                        return null;
                    }

                    @Override
                    protected void done() {
                        super.done();
                        repeat = 0;
                    }
                };
                gen.execute();
            }
        }

        private void genereateRandomBonus(CaseModelNew[][] fg) {
            int x = getRandomNum(fg[0].length);
            int y = getRandomNum(fg.length);

            if (fg[y][x].getCaseType() == CaseType.Vide) {
                BonusType type = BonusType.random(plat.isCustom());

                warn(fg[y][x]);
                spawn = System.currentTimeMillis();
                Timer spawnTimer = new Timer(Constants.BONUS_WARN, e -> {
                    warn = System.currentTimeMillis();
                    if (parent instanceof MainMenu) {
                        (((MainMenu) parent).getSound()).pop();
                    }
                    fg[y][x].setCaseType(type);
                    fg[y][x].getLabel().setIcon(new ImageIcon(type.getImage().getImage().getScaledInstance(fg[y][x].getLabel().getIcon().getIconHeight() - 5, fg[y][x].getLabel().getIcon().getIconHeight() - 5, Image.SCALE_SMOOTH)));

                });
                Timer blinkTimer = new Timer((Constants.BONUS_LIFE - Constants.BONUS_BLINK), e -> {
                    blink = System.currentTimeMillis();
                    blink(fg[y][x], type);
                });
                Timer deadTimer = new Timer(Constants.BONUS_LIFE, e -> {
                    die(fg[y][x]);
                });
                spawnTimer.setRepeats(false);
                blinkTimer.setRepeats(false);
                deadTimer.setRepeats(false);
                spawnTimer.start();
                blinkTimer.start();
                deadTimer.start();
                MainMenu.timerList.add(spawnTimer);
                MainMenu.timerList.add(blinkTimer);
                MainMenu.timerList.add(deadTimer);

            } else if (repeat <= 5) {
                genereateRandomBonus(fg);
                repeat++;
            }
        }


        private void warn(CaseModelNew caseM) {
            caseM.setCaseType(BonusType.WARN);
            caseM.getLabel().setIcon(new ImageIcon(BonusType.WARN.getImage().getImage().getScaledInstance(caseM.getLabel().getHeight(), caseM.getLabel().getHeight(), Image.SCALE_FAST)));
        }


        private void blink(CaseModelNew caseM, CaseM type) {
            final boolean[] show = {true};
            Timer hideTimer = new Timer(500, evt -> {
                if (show[0]) {
                    caseM.getLabel().setIcon(new ImageIcon(CaseType.Vide.getImage().getImage().getScaledInstance(caseM.getLabel().getHeight(), caseM.getLabel().getHeight() - 5, Image.SCALE_SMOOTH)));
                } else {
                    caseM.getLabel().setIcon(new ImageIcon(caseM.getCaseType().getImage().getImage().getScaledInstance(caseM.getLabel().getHeight() - 5, caseM.getLabel().getHeight() - 5, Image.SCALE_SMOOTH)));
                }
                show[0] = !show[0];
                plat.repaint();
            });
            bonusList.put(caseM, hideTimer);
            hideTimer.start();
            MainMenu.timerList.add(hideTimer);
        }

        private void die(CaseModelNew caseM) {
            if (bonusList.containsKey(caseM)) {
                bonusList.get(caseM).stop();
                bonusList.remove(caseM);
            }
            die = System.currentTimeMillis();
            caseM.setCaseType(CaseType.Vide);
            caseM.getLabel().setIcon(new ImageIcon(CaseType.Vide.getImage().getImage().getScaledInstance(caseM.getLabel().getHeight(), caseM.getLabel().getHeight(), Image.SCALE_SMOOTH)));
        }


        private int getRandomNum(int max) {
            Random random = new Random(System.currentTimeMillis() + System.nanoTime() * Double.doubleToLongBits(Math.random()));
            return random.nextInt(max);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            trySpawn = System.currentTimeMillis();
            genereateRandomBonus();
        }
    }

    public class resize implements ComponentListener {

        VuePlateau plat;

        public resize(VuePlateau plat) {
            this.plat = plat;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            // TODO Auto-generated method stub
            plat.resize();

        }

        @Override
        public void componentMoved(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentShown(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void componentHidden(ComponentEvent e) {
            // TODO Auto-generated method stub

        }

    }
}
