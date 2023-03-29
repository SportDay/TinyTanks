package fr.mardiH.controller.panel;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;
import fr.mardiH.controller.MainMenuController;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.Enum.FileType;
import fr.mardiH.model.GamePadByName;
import fr.mardiH.model.LocalByName;
import fr.mardiH.util.Constants;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.SettingsMenuPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static fr.mardiH.Main.logger;


public class SettingsMenuController {

    private final JPanel source;
    private boolean changeUP = false;
    private boolean changeLeft = false;
    private boolean changeDown = false;
    private boolean changeRight = false;
    public SettingsMenuController(JPanel source) {
        this.source = source;
    }

    public class MouseInputs extends MouseAdapter {

        private JLabel label;

        public MouseInputs() {
        }

        public MouseInputs(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            MainMenu.Instance.getSound().btnClick();
            if (source instanceof SettingsMenuPanel) {
                if (e.getSource() == ((SettingsMenuPanel) source).getBackBTN()) {
                    MainMenu.Instance.setPreviusPanel(MainMenuController.curent);
                } else if (((SettingsMenuPanel) source).getTanksList().contains(e.getSource())) {
                    ((SettingsMenuPanel) source).getTanksList().get(MainMenu.Instance.getSettingsModel().getTankModel()).setBorder(new LineBorder(Color.WHITE, 2));
                    MainMenu.Instance.getSettingsModel().setTankModel(Integer.parseInt(label.getName()));
                    MainMenu.setTankType(Integer.parseInt(label.getName()));
                    CaseType.Joueur.image = new ImageIcon(ClassLoader.getSystemResource("Images/PNG/Retina/tank_" + Integer.parseInt(label.getName()) + ".png"));
                    CaseType.Joueur.image.setDescription("Images/PNG/Retina/tank_" + Integer.parseInt(label.getName()) + ".png");
                    label.setBorder(new LineBorder(Color.GREEN, 2));
                }
            }

        }

    }

    public class Action implements ActionListener {


        public Action() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (source instanceof SettingsMenuPanel) {
                if (e.getSource() == ((SettingsMenuPanel) source).getEffectBTN()) {
                    MainMenu.Instance.getSound().pop();
                } else if (e.getSource() == ((SettingsMenuPanel) source).getMusicBTN()) {
                    if (!MainMenu.Instance.getSound().getClip().isRunning()) {
                        int r = new Random().nextInt(3);
                        switch (r) {
                            case 0:
                                MainMenu.Instance.getSound().menu(false);
                                break;
                            case 1:
                                int b = new Random().nextInt(2);
                                boolean f = b != 0;
                                MainMenu.Instance.getSound().level(f);
                                break;
                            case 2:
                                MainMenu.Instance.getSound().credits();
                                break;
                        }
                    } else {
                        MainMenu.Instance.getSound().getClip().stop();
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getOtherBTN()) {
                    MainMenu.Instance.getSound().btnClick();
                } else if (e.getSource() == ((SettingsMenuPanel) source).getCreditsBTN()) {
                    MainMenu.Instance.setCreditsPanel();
                } else if (e.getSource() == ((SettingsMenuPanel) source).getOpenMapsFolder()) {
                    if (Desktop.isDesktopSupported() && new File(Constants.PATH + File.separator + "Maps").exists()) {
                        try {
                            Desktop.getDesktop().open(new File(Constants.PATH + File.separator + "Maps"));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getUpdateBTN()) {
                    MainMenu.Instance.getSound().btnClick();
                    MainMenu.gamePadManagerInstance.update();
                    if (MainMenu.gamePadManagerInstance.getNumControllers() != 0) {
                        ((SettingsMenuPanel) source).getSaveBTN().setEnabled(true);
                        ((SettingsMenuPanel) source).getComboBox().setEnabled(true);
                        ((SettingsMenuPanel) source).getComboBox().removeAllItems();
                        for (int i = 0; i < MainMenu.gamePadManagerInstance.getNumControllers(); i++) {
                            ControllerIndex gamePad = MainMenu.gamePadManagerInstance.getControllerIndex(i);
                            try {
                                ((SettingsMenuPanel) source).getComboBox().addItem(new GamePadByName(gamePad, gamePad.getName(), i));
                            } catch (ControllerUnpluggedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (MainMenu.gamePadInstance == null) {
                            MainMenu.gamePadInstance = ((GamePadByName) ((SettingsMenuPanel) source).getComboBox().getSelectedItem()).getController();
                        }
                    } else {
                        disableComboBox();
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getSaveBTN()) {
                    MainMenu.Instance.getSound().btnClick();
                    MainMenu.gamePadManagerInstance.update();
                    if (MainMenu.gamePadManagerInstance.getNumControllers() != 0) {
                        MainMenu.gamePadInstance = ((GamePadByName) ((SettingsMenuPanel) source).getComboBox().getSelectedItem()).getController();
                        if (MainMenu.gamePadInstance.isConnected()) {
                            try {
                                ((SettingsMenuPanel) source).getManettePanel().setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), "Manette: " + MainMenu.gamePadInstance.getName() + " (" + MainMenu.gamePadInstance.getIndex() + ")", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
                            } catch (ControllerUnpluggedException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            disableComboBox();
                        }
                    } else {
                        disableComboBox();
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getSaveLangBTN()) {
                    MainMenu.Instance.getSound().btnClick();
                    MainMenu.Instance.getSettingsModel().setLang(((LocalByName) ((SettingsMenuPanel) source).getLangComboBox().getSelectedItem()).getLocale());
                    MainMenu.Instance.setSettingsPanel();
                } else if (e.getSource() == ((SettingsMenuPanel) source).getResetAchievBTN()) {
                    MainMenu.Instance.getSound().btnClick();
                    Object[] options = {Language.getString("btn.delete.text"), Language.getString("btn.cancel.text")};
                    int r = JOptionPane.showOptionDialog(MainMenu.Instance, Language.getString("text.achiremove.text"), Language.getString("text.achiremove.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                    if (r == 0) {
                        MainMenu.Instance.sec.saveCrypt(FileType.achievements, true);
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getBTN_UP()) {
                    MainMenu.Instance.getSound().btnClick();
                    if (!changeUP && !changeLeft && !changeDown && !changeRight) {
                        changeUP = true;
                        ((SettingsMenuPanel) source).getMsgLabel().setVisible(true);
                        ((SettingsMenuPanel) source).getBTN_UP().setBorder(new LineBorder(Color.GREEN, 3, true));
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getBTN_LEFT()) {
                    MainMenu.Instance.getSound().btnClick();
                    if (!changeUP && !changeLeft && !changeDown && !changeRight) {
                        changeLeft = true;
                        ((SettingsMenuPanel) source).getMsgLabel().setVisible(true);
                        ((SettingsMenuPanel) source).getBTN_LEFT().setBorder(new LineBorder(Color.GREEN, 3, true));
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getBTN_DOWN()) {
                    MainMenu.Instance.getSound().btnClick();
                    if (!changeUP && !changeLeft && !changeDown && !changeRight) {
                        changeDown = true;
                        ((SettingsMenuPanel) source).getMsgLabel().setVisible(true);
                        ((SettingsMenuPanel) source).getBTN_DOWN().setBorder(new LineBorder(Color.GREEN, 3, true));
                    }
                } else if (e.getSource() == ((SettingsMenuPanel) source).getBTN_RIGHT()) {
                    MainMenu.Instance.getSound().btnClick();
                    if (!changeUP && !changeLeft && !changeDown && !changeRight) {
                        changeRight = true;
                        ((SettingsMenuPanel) source).getMsgLabel().setVisible(true);
                        ((SettingsMenuPanel) source).getBTN_RIGHT().setBorder(new LineBorder(Color.GREEN, 3, true));
                    }
                }
            }
        }

        private void disableComboBox() {
            ((SettingsMenuPanel) source).getComboBox().setModel(new DefaultComboBoxModel(new String[]{Language.getString("label.nojoystick.text")}));
            ((SettingsMenuPanel) source).getSaveBTN().setEnabled(false);
            ((SettingsMenuPanel) source).getComboBox().setEnabled(false);
            MainMenu.gamePadInstance = null;
            ((SettingsMenuPanel) source).getManettePanel().setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0, 0), 1, true), Language.getString("label.joystick.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), Color.WHITE));
        }
    }

    public class KeyAdapter implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            logger.info("Key pressed: " + key);
            if (source instanceof SettingsMenuPanel) {
                if ((changeRight || changeUP || changeLeft || changeDown) && ((SettingsMenuPanel) source).getKeyList().contains(key)) {
                    JOptionPane.showMessageDialog(MainMenu.Instance, Language.getString("text.keyexist.text").replaceAll("%key%", KeyEvent.getKeyText(key)));
                    keyReleased(e);
                } else if ((changeRight || changeUP || changeLeft || changeDown) && (key == KeyEvent.VK_F11 || key == KeyEvent.VK_WINDOWS || key == KeyEvent.VK_ESCAPE)) {
                    JOptionPane.showMessageDialog(MainMenu.Instance, Language.getString("text.keyerror.text").replaceAll("%key%", KeyEvent.getKeyText(KeyEvent.VK_F11) + ", " + KeyEvent.getKeyText(KeyEvent.VK_ESCAPE) + ", " + KeyEvent.getKeyText(KeyEvent.VK_WINDOWS) + "."));
                    keyReleased(e);
                } else {
                    if (changeUP) {
                        MainMenu.Instance.getSettingsModel().setBtnUP(key);
                        updateBTN(((SettingsMenuPanel) source).getBTN_UP(), key);
                    }
                    if (changeLeft) {
                        MainMenu.Instance.getSettingsModel().setBtnLEFT(key);
                        updateBTN(((SettingsMenuPanel) source).getBTN_LEFT(), key);
                    }
                    if (changeDown) {
                        MainMenu.Instance.getSettingsModel().setBtnDOWN(key);
                        updateBTN(((SettingsMenuPanel) source).getBTN_DOWN(), key);
                    }
                    if (changeRight) {
                        MainMenu.Instance.getSettingsModel().setBtnRIGHT(key);
                        updateBTN(((SettingsMenuPanel) source).getBTN_RIGHT(), key);
                    }
                }
            }
        }

        private void updateBTN(JButton btn, int key) {
            btn.setText(KeyEvent.getKeyText(key));
            ((SettingsMenuPanel) source).getKeyList().remove(Integer.valueOf(btn.getActionCommand()));
            btn.setActionCommand(key + "");
            ((SettingsMenuPanel) source).getKeyList().add(key);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            changeUP = false;
            changeLeft = false;
            changeDown = false;
            changeRight = false;
            if (source instanceof SettingsMenuPanel) {
                ((SettingsMenuPanel) source).getMsgLabel().setVisible(false);
                ((SettingsMenuPanel) source).getBTN_UP().setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
                ((SettingsMenuPanel) source).getBTN_LEFT().setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
                ((SettingsMenuPanel) source).getBTN_DOWN().setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
                ((SettingsMenuPanel) source).getBTN_RIGHT().setBorder(new LineBorder(new Color(0, 0, 0), 3, true));


                int caseSize = Integer.max(Integer.max(((SettingsMenuPanel) source).getBTN_UP().getWidth(), ((SettingsMenuPanel) source).getBTN_LEFT().getWidth()), Integer.max(((SettingsMenuPanel) source).getBTN_DOWN().getWidth(), ((SettingsMenuPanel) source).getBTN_RIGHT().getWidth()));
                ((SettingsMenuPanel) source).getGridLayout().columnWidths = new int[]{caseSize, 5, caseSize, 5, caseSize};
                ((SettingsMenuPanel) source).getMoveBTNPanel().repaint();
                ((SettingsMenuPanel) source).getMoveBTNPanel().revalidate();
            }
        }
    }

    public class Resize extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            if (source instanceof SettingsMenuPanel) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(((SettingsMenuPanel) source).getImageBG(), ((SettingsMenuPanel) source).getBackgroundImg(), source.getWidth(), source.getHeight());
                updateBackgroundImageWorker.execute();

                int caseSize = Integer.max(Integer.max(((SettingsMenuPanel) source).getBTN_UP().getWidth(), ((SettingsMenuPanel) source).getBTN_LEFT().getWidth()), Integer.max(((SettingsMenuPanel) source).getBTN_DOWN().getWidth(), ((SettingsMenuPanel) source).getBTN_RIGHT().getWidth()));
                ((SettingsMenuPanel) source).getGridLayout().columnWidths = new int[]{caseSize, 5, caseSize, 5, caseSize};
                ((SettingsMenuPanel) source).getMoveBTNPanel().repaint();
                ((SettingsMenuPanel) source).getMoveBTNPanel().revalidate();
            }
        }
    }

    public class SliderChange implements ChangeListener {

        public SliderChange() {

        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (source instanceof SettingsMenuPanel) {
                if (e.getSource() == ((SettingsMenuPanel) source).getMenuSlider()) {
                    ((SettingsMenuPanel) source).getMenuPanel().setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.other.text") + " " + ((SettingsMenuPanel) source).getMenuSlider().getValue() + "%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
                    MainMenu.Instance.getSettingsModel().setMenuVolume(((SettingsMenuPanel) source).getMenuSlider().getValue());
                } else if (e.getSource() == ((SettingsMenuPanel) source).getEffectSlider()) {
                    ((SettingsMenuPanel) source).getEffectPanel().setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.effect.text") + " " + ((SettingsMenuPanel) source).getEffectSlider().getValue() + "%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
                    MainMenu.Instance.getSettingsModel().setEffectVolume(((SettingsMenuPanel) source).getEffectSlider().getValue());
                } else if (e.getSource() == ((SettingsMenuPanel) source).getMusicSlider()) {
                    ((SettingsMenuPanel) source).getMusicPanel().setBorder(new TitledBorder(new LineBorder(Color.WHITE, 2, true), Language.getString("label.musicslider.music.text") + " " + ((SettingsMenuPanel) source).getMusicSlider().getValue() + "%", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.WHITE));
                    MainMenu.Instance.getSettingsModel().setMusicVolume(((SettingsMenuPanel) source).getMusicSlider().getValue());
                }
            }
        }
    }
}
