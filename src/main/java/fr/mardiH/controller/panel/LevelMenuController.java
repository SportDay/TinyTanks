package fr.mardiH.controller.panel;

import fr.mardiH.controller.MainMenuController;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.util.Constants;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.LevelIMGPanel;
import fr.mardiH.view.panel.LevelMenuPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static fr.mardiH.Main.logger;

public class LevelMenuController {
    public LevelMenuController() {

    }

    /**
     * Il crée une nouvelle image de la carte si la carte a été modifiée depuis la dernière fois que l'image a été créée
     */
    public class CreateImage {
        JPanel panel;

        public CreateImage(JPanel panel) {
            this.panel = panel;
            initImage();
        }

        private void initImage() {
            SwingWorker work = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    LinkedList<String> map = PlateauModel.mapDispo();
                    for (String entry : map) {
                        File f = new File(Constants.PATH + "Maps" + File.separator + entry + File.separator + "Map.yaml");
                        File img = new File(Constants.PATH + "Maps" + File.separator + entry + File.separator + "logo.png");
                        File img2 = new File(Constants.PATH + "Maps" + File.separator + entry + File.separator + "logo-ignore.png");
                        if (!img2.exists()) {
                            if (img.exists()) {
                                try {
                                    BasicFileAttributes fAtt = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                                    BasicFileAttributes iAtt = Files.readAttributes(img.toPath(), BasicFileAttributes.class);
                                    if (fAtt.lastModifiedTime().toMillis() > iAtt.lastModifiedTime().toMillis()) {
                                        generateNewImage(new LevelIMGPanel(entry), entry);
                                    }
                                } catch (IOException e) {
                                    generateNewImage(new LevelIMGPanel(entry), entry);
                                }
                            } else {
                                generateNewImage(new LevelIMGPanel(entry), entry);
                            }
                        }
                    }
                    return null;
                }

            };

            work.execute();

        }


        private void generateNewImage(JLayeredPane pane, String mapName) {
            SwingWorker work = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    JFrame t = new JFrame();
                    t.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    t.setBounds(0, 0, 0, 0);
                    t.add(pane);
                    t.setUndecorated(true);
                    t.pack();
                    BufferedImage image = new BufferedImage(pane.getWidth(), pane.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                    pane.paint(image.getGraphics());
                    try {
                        ImageIO.write(image, "png", new File(Constants.PATH + "Maps" + File.separator + mapName + File.separator + "logo.png"));
                    } catch (IOException exp) {
                        exp.printStackTrace();
                    }
                    t.dispatchEvent(new WindowEvent(t, WindowEvent.WINDOW_CLOSING));

                    if (panel instanceof LevelMenuPanel) {
                        new UpdateIcon(((LevelMenuPanel) panel).getLevelLabel()).execute();
                    }

                    return null;
                }
            };
            work.execute();

        }
    }

    public class MouseInputs extends MouseAdapter {

        private final JPanel parent;
        private JPanel parent2;

        public MouseInputs(JPanel parent) {
            this.parent = parent;
        }

        public MouseInputs(JPanel parent, JPanel parent2) {
            this.parent = parent;
            this.parent2 = parent2;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MainMenu.Instance.getSound().btnClick();


            if (parent instanceof LevelMenuPanel) {
                if (e.getSource() == ((LevelMenuPanel) parent).getBackBTN()) {
                    MainMenu.Instance.setPreviusPanel(MainMenuController.curent);
                } else if (e.getSource() == ((LevelMenuPanel) parent).getCreateMenu()) {
                    MainMenu.Instance.setCreatePanel();
                } else if (e.getSource() instanceof JPanel) {
                    if (((LevelMenuPanel) parent).getLevelList().contains((JPanel) e.getSource())) {
                        JPanel panel = (JPanel) e.getSource();
                        if (panel.getName() != null && panel.getName().contains("!")) {
                            String[] tmp = panel.getName().split("!");
                            if (tmp.length == 3) {
                                String levelName = tmp[0];
                                boolean allowStart = Boolean.parseBoolean(tmp[1]);
                                boolean custom = Boolean.parseBoolean(tmp[2]);
                                if (allowStart) {
                                    logger.debug("Launch level: " + levelName);
                                    MainMenu.Instance.setLevelPanel(levelName, custom, false);
                                }
                            }
                        }
                    }
                } else if (e.getSource() instanceof JLabel) {
                    if (((LevelMenuPanel) parent).getDelList().containsKey((JLabel) e.getSource()) && parent2 != null) {
                        JLabel label = (JLabel) e.getSource();
                        if (label.getName() != null) {
                            Object[] options = {Language.getString("btn.delete.text"), Language.getString("btn.cancel.text")};
                            int r = JOptionPane.showOptionDialog(MainMenu.Instance, Language.getString("text.map.remove.text").replaceAll("%mapname%", label.getName()), Language.getString("text.map.remove.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                            if (r == 0) {
                                logger.debug("Remove: " + label.getName());
                                PlateauModel.remove(label.getName());
                                parent2.remove(((LevelMenuPanel) parent).getDelList().get(label));
                                parent2.revalidate();
                                parent2.repaint();
                            }
                        }
                    } else if (((LevelMenuPanel) parent).getEditorList().contains((JLabel) e.getSource())) {
                        JLabel label = (JLabel) e.getSource();
                        if (label.getName() != null) {
                            logger.debug("Edit: " + label.getName());
                            MainMenu.Instance.setCustomCreatePanel(label.getName());
                        }
                    }
                }
            }

        }
    }

    /**
     * C'est un SwingWorker qui met à jour les icônes des niveaux dans la liste des niveaux
     */
    public class UpdateIcon extends SwingWorker {

        private final LinkedHashMap<JLabel, JPanel> levelList;

        public UpdateIcon(LinkedHashMap<JLabel, JPanel> levelList) {
            this.levelList = levelList;
        }

        @Override
        protected Object doInBackground() {
            if (levelList != null) {
                for (Map.Entry<JLabel, JPanel> entry : levelList.entrySet()) {

                    int width = (275 * MainMenu.Instance.getWidth()) / MainMenu.Instance.getMinWidth();
                    int height = (225 * MainMenu.Instance.getHeight()) / MainMenu.Instance.getMinHeight();

                    entry.getValue().setPreferredSize(new Dimension(width, height));
                    entry.getValue().revalidate();

                    String[] info = entry.getValue().getName().split("!");

                    String name = null;
                    boolean allowStart = false;
                    if (info.length >= 2) {
                        name = info[0];
                        allowStart = Boolean.parseBoolean(info[1]);
                    }

                    if (name != null) {
                        if (name.toLowerCase().contains(".yaml")) {
                            setPrivateLevelImg(entry.getKey(), ".yaml", name, allowStart);
                        } else if (name.toLowerCase().contains(".yml")) {
                            setPrivateLevelImg(entry.getKey(), ".yml", name, allowStart);
                        } else {
                            String finalName = name;
                            Timer timer = new Timer(1, e -> {
                                File img2 = new File(Constants.PATH + "Maps" + File.separator + finalName + File.separator + "logo-ignore.png");
                                if (img2.exists()) {
                                    entry.getKey().setIcon(new ImageIcon(new ImageIcon(img2.getAbsolutePath()).getImage().getScaledInstance(entry.getKey().getWidth(), entry.getKey().getHeight(), Image.SCALE_FAST)));
                                } else {
                                    entry.getKey().setIcon(new ImageIcon(new ImageIcon(Constants.PATH + "Maps" + File.separator + finalName + File.separator + "logo.png").getImage().getScaledInstance(entry.getKey().getWidth(), entry.getKey().getHeight(), Image.SCALE_FAST)));
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                        entry.getKey().repaint();
                        entry.getKey().revalidate();
                    }

                }
            }
            return null;
        }

        private void setPrivateLevelImg(JLabel label, String extension, String name, boolean allowStart) {
            Timer timer = new Timer(1, e -> {
                if (!allowStart) {
                    int size = 512;
                    int newSize = Integer.min((size * label.getWidth()) / MainMenu.Instance.getMinWidth(), (size * label.getHeight()) / MainMenu.Instance.getMinHeight());
                    label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/lock.png")).getImage().getScaledInstance(newSize, newSize, Image.SCALE_FAST)));
                } else {
                    label.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Maps/" + name.replaceAll(extension, ".png"))).getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_FAST)));
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

    }


    public class Resize extends ComponentAdapter {

        private final JPanel panel;

        public Resize(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (panel instanceof LevelMenuPanel) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(((LevelMenuPanel) panel).getImageBG(), ((LevelMenuPanel) panel).getBackgroundIMG(), panel.getWidth(), panel.getHeight());
                updateBackgroundImageWorker.execute();

                UpdateIcon updateIcon = new UpdateIcon(((LevelMenuPanel) panel).getLevelLabel());
                updateIcon.execute();
            }
        }
    }
}
