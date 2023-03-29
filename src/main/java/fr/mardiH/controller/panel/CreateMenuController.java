package fr.mardiH.controller.panel;

import fr.mardiH.Error.InvalidNameMapException;
import fr.mardiH.Error.NoBotException;
import fr.mardiH.Error.NoPlayerException;
import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.CreatorMapJRadioButtonModel;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.util.Constants;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;
import fr.mardiH.view.panel.BGCaseChoice;
import fr.mardiH.view.panel.CreateMenuPanel;
import fr.mardiH.view.panel.FGCaseChoice;
import fr.mardiH.view.panel.ShowCreate;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CreateMenuController {

    private static final String PATH = Constants.PATH;
    private final JPanel source;
    private JLayeredPane layeredPane;

    public CreateMenuController(JPanel source) {
        this.source = source;
    }

    public void setLayeredPane(JLayeredPane layeredPane) {
        this.layeredPane = layeredPane;
    }

    public class JRadio implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (((JRadioButton) e.getSource()).isSelected()) {
                ((JRadioButton) e.getSource()).setBorder(new LineBorder(Color.GREEN, 2));
                ((JRadioButton) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } else {
                ((JRadioButton) e.getSource()).setBorder(new LineBorder(Color.BLACK, 2));
                ((JRadioButton) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }

    public class BTNListener implements ActionListener {

        String oldName;

        public void setOldName(String oldName) {
            this.oldName = oldName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton && source instanceof CreateMenuPanel) {
                CreateMenuPanel src = ((CreateMenuPanel) source);
                JButton btn = (JButton) e.getSource();
                if (btn == src.getBgBTN()) {
                    if (layeredPane instanceof ShowCreate) {
                        ((ShowCreate) layeredPane).getFg().setVisible(false);
                        src.getBgBTN().setBorder(new LineBorder(Color.GREEN, 3, true));
                        src.getFgBTN().setBorder(new LineBorder(Color.BLACK, 3, true));
                        setBGPanel(src);
                    }
                } else if (btn == ((CreateMenuPanel) source).getFgBTN()) {
                    if (layeredPane instanceof ShowCreate) {
                        ((ShowCreate) layeredPane).getFg().setVisible(true);
                        src.getBgBTN().setBorder(new LineBorder(Color.BLACK, 3, true));
                        src.getFgBTN().setBorder(new LineBorder(Color.GREEN, 3, true));
                        setFGPanel(src);
                    }
                } else if (btn == ((CreateMenuPanel) source).getInfoBTN()) {
                    if (layeredPane instanceof ShowCreate) {
                        String t1 = Language.getString("text.shortcutinfo.text") + "\n" +
                                Language.getString("text.forcelogo.text").replaceAll("%filename%", " logo-ignore.png");
                        JOptionPane.showMessageDialog(MainMenu.Instance, t1, Language.getString("label.information.text"),
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (btn == ((CreateMenuPanel) source).getAnnulBTN()) {
                    MainMenu.Instance.setLevelPanel();
                } else if (btn == ((CreateMenuPanel) source).getValiderBTN()) {
                    try {
                        plateauxToYaml();
                        MainMenu.Instance.setLevelPanel();
                        MainMenu.Instance.getAchievements().getMakeMap().setUnlocked(true);
                    } catch (Exception exeption) {

                    }
                }
            }
        }

        /**
         * Il convertit la carte en un fichier yaml
         */
        public void plateauxToYaml() throws InvalidNameMapException, NoBotException, NoPlayerException {
            PlateauModel p = new PlateauModel((CreateMenuPanel) source);
            CaseModelNew[][] fg = p.getForeground();
            CaseModelNew[][] bg = p.getBackground();
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            boolean bot = false;
            boolean joueur = false;

            String b = "########################";
            for (int i = 0; i < 16; i++) {
                b += "#\n#";
                for (int j = 0; j < 23; j++) {
                    if (bg[i][j].getCaseType().getLettre() == ' ') {
                        b += " ";
                    } else {
                        b += bg[i][j].getCaseType().getLettre();
                    }
                }
            }
            b += "#\n#########################";

            map.put("bg", b);

            String f = "########################";
            for (int i = 0; i < 16; i++) {
                f += "#\n#";
                for (int j = 0; j < 23; j++) {
                    if (fg[i][j].getCaseType().getLettre() == 'J') {
                        joueur = true;
                    } else if (fg[i][j].getCaseType().getLettre() == 'R') {
                        bot = true;
                    } else if (fg[i][j].getCaseType().getLettre() == 'Z') {
                        bot = true;
                    }
                    f += fg[i][j].getCaseType().getLettre();
                }
            }
            f += "#\n#########################";
            if (!bot) {
                JOptionPane.showMessageDialog(source, Language.getString("text.map.nobot.text"), Language.getString("text.error.text"), JOptionPane.ERROR_MESSAGE);
                throw new NoBotException();
            }
            if (!joueur) {
                JOptionPane.showMessageDialog(source, Language.getString("text.map.noplayer.text"), Language.getString("text.error.text"), JOptionPane.ERROR_MESSAGE);
                throw new NoPlayerException();
            }
            map.put("fg", f);
            //TODO METTRE LA VALEUR DE LA CASE RAID ICI
            map.put("raid", ((CreateMenuPanel) source).getCheckRaid().isSelected());

            Yaml yaml = new Yaml();
            try {
                String nomMap = nomMapValide();
                if (((CreateMenuPanel) source).isCustom() && !nomMap.equalsIgnoreCase(oldName)) {
                    PlateauModel.remove(oldName);
                }
                new File(PATH + "Maps" + File.separator + nomMap).mkdir();
                FileWriter writer = new FileWriter(PATH + "Maps" + File.separator + nomMap + File.separator + "Map.yaml");
                writer.write(CaseType.caseDescription("#"));
                yaml.dump(map, writer);
                JOptionPane.showMessageDialog(source, Language.getString("text.map.generetesucces.text").replaceAll("%mapname%", nomMap) + PATH + nomMap, Language.getString("text.success.text"), JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(source, Language.getString("text.map.invalidname.text"), Language.getString("text.error.text"), JOptionPane.ERROR_MESSAGE);
                throw new InvalidNameMapException();
            } catch (InvalidNameMapException e) {
                JOptionPane.showMessageDialog(source, Language.getString("text.map.namenocontaint.text") + "\n: \\ ! * / \" < > |", Language.getString("text.error.text"), JOptionPane.ERROR_MESSAGE);
                throw new InvalidNameMapException();
            }


        }

        // Vérifier si le nom de la carte est valide.
        public String nomMapValide() throws InvalidNameMapException {
            char[] invalideChar = {':', '\\', '!', '*', '/', '"', '<', '>', '|'};
            String nom = ((CreateMenuPanel) source).getTxtNomDeLa().getText();
            for (int i = 0; i < nom.length(); i++) {
                for (int j = 0; j < invalideChar.length; j++) {
                    if (nom.charAt(i) == invalideChar[j]) {
                        throw new InvalidNameMapException();
                    }
                }
            }
            File dir = new File(PATH + "Maps" + File.separator);
            for (File f : dir.listFiles()) {
                if ((f.getName().equalsIgnoreCase(nom) && !((CreateMenuPanel) source).isCustom()) || (f.getName().equalsIgnoreCase(nom) && !nom.equalsIgnoreCase(oldName))) {
                    return nomMapCorrection(nom);
                }
            }
            return nom;
        }

        public String nomMapCorrection(String nom) {
            int i = 0;
            do {
                i++;
            } while (new File(PATH + "Maps" + File.separator + nom + " (" + i + ")").exists());
            return nom + " (" + i + ")";
        }

        private void setBGPanel(CreateMenuPanel panel) {
            panel.getChoicePanelContainer().removeAll();
            Component verticalGlue_4 = Box.createVerticalGlue();
            panel.getChoicePanelContainer().add(verticalGlue_4);

            JPanel choicePanel = new BGCaseChoice(panel.getController());
            panel.setButtonGroup(((BGCaseChoice) choicePanel).getButtonGroup());
            choicePanel.setOpaque(false);
            panel.getChoicePanelContainer().add(choicePanel);
            choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));

            Component verticalGlue_5 = Box.createVerticalGlue();
            panel.getChoicePanelContainer().add(verticalGlue_5);

            panel.getScrollPane().setBorder(new TitledBorder(new LineBorder(Color.white, 2, true), Language.getString("label.background.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));
            panel.getChoicePanelContainer().repaint();
            panel.getChoicePanelContainer().revalidate();
        }

        private void setFGPanel(CreateMenuPanel panel) {
            panel.getChoicePanelContainer().removeAll();
            Component verticalGlue_4 = Box.createVerticalGlue();
            panel.getChoicePanelContainer().add(verticalGlue_4);

            JPanel choicePanel = new FGCaseChoice(panel.getController());
            panel.setButtonGroup(((FGCaseChoice) choicePanel).getButtonGroup());
            choicePanel.setOpaque(false);
            panel.getChoicePanelContainer().add(choicePanel);
            choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.X_AXIS));

            Component verticalGlue_5 = Box.createVerticalGlue();
            panel.getChoicePanelContainer().add(verticalGlue_5);

            panel.getScrollPane().setBorder(new TitledBorder(new LineBorder(Color.white, 2, true), Language.getString("label.foreground.text"), TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));
            panel.getChoicePanelContainer().repaint();
            panel.getChoicePanelContainer().revalidate();
        }


    }

    public class UpdateIcon extends SwingWorker {

        int size = 35 * 2;

        public UpdateIcon() {
        }

        @Override
        // Redimensionnement des boutons dans le menu de création.
        protected Object doInBackground() {
            if (source instanceof CreateMenuPanel) {
                CreateMenuPanel src = ((CreateMenuPanel) source);
                int widthInsets = src.getInsets().left + src.getInsets().right;
                int heightInsets = src.getInsets().top + src.getInsets().bottom;
                int newSize = (size * (source.getWidth() - widthInsets)) / (MainMenu.Instance.getMinWidth() - widthInsets);
                int newSize1 = (size * (source.getHeight() - heightInsets)) / (MainMenu.Instance.getMinWidth() - heightInsets);
                int min = Integer.min(newSize, newSize1);

                for (AbstractButton btn : Collections.list(((CreateMenuPanel) source).getButtonGroup().getElements())) {
                    if (btn instanceof CreatorMapJRadioButtonModel) {
                        if (((CreatorMapJRadioButtonModel) btn).getType().getLettre() == ' ') {
                            btn.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("Images/menu/closeBtn.png")).getImage().getScaledInstance(min, min, Image.SCALE_FAST)));
                        } else {
                            btn.setIcon(new ImageIcon(((CreatorMapJRadioButtonModel) btn).getType().getImage().getImage().getScaledInstance(min, min, Image.SCALE_FAST)));
                        }
                    }
                }
                ((CreateMenuPanel) source).getScrollPane().setPreferredSize(new Dimension((550 * (source.getWidth() - widthInsets)) / (MainMenu.Instance.getMinWidth() - widthInsets), (135 * (source.getHeight() - heightInsets)) / (MainMenu.Instance.getMinHeight() - heightInsets)));
            }
            return null;
        }
    }

    public class Action extends AbstractAction {

        private final JRadioButton btn;

        public Action(JRadioButton btn) {
            this.btn = btn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            btn.setSelected(true);
        }
    }

    public class Resize extends ComponentAdapter {

        private final JPanel panel;

        public Resize(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (panel instanceof CreateMenuPanel) {
                UpdateBackgroundImageWorker updateBackgroundImageWorker = new UpdateBackgroundImageWorker(((CreateMenuPanel) panel).getImageBG(), ((CreateMenuPanel) panel).getBackgroundIMG(), panel.getWidth(), panel.getHeight());
                updateBackgroundImageWorker.execute();

                if (layeredPane instanceof ShowCreate) {
                    ShowCreateController.UpdateIcon updateIcon = ((CreateMenuPanel) panel).getShowController().new UpdateIcon(layeredPane);
                    updateIcon.execute();
                }

                UpdateIcon updateIcon = new UpdateIcon();
                updateIcon.execute();
            }
        }
    }
}
