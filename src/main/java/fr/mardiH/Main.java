package fr.mardiH;

import fr.mardiH.util.Constants;
import fr.mardiH.view.MainMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);
    private static final String PATH = Constants.PATH;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                init();
                Toolkit.getDefaultToolkit().setDynamicLayout(false);
                UIManager.put("ProgressBar.selectionBackground", Color.LIGHT_GRAY);
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
                MainMenu.Instance.sec.checkOk();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static void init() { // A METTRE AU LANCEMENT DU PROGRAMME
        if (!new File(PATH).exists()) {
            new File(PATH).mkdir();
        }
        if (!new File(PATH + "Maps").exists()) {
            new File(PATH + "Maps").mkdir();
        }
    }

}
