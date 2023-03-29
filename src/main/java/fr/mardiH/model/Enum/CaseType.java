package fr.mardiH.model.Enum;

import fr.mardiH.model.interfaces.CaseM;
import fr.mardiH.util.Language;
import fr.mardiH.view.MainMenu;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static fr.mardiH.Main.logger;

public enum CaseType implements CaseM {
    Herbe(Language.getString("case.grass.text"), "Images/PNG/Retina/tileGrass1.png", 'H', true, false),
    Sable(Language.getString("case.sand.text"), "Images/PNG/Retina/tileSand1.png", 'S', true, false),
    Vide(Language.getString("case.delete.text"), "Images/menu/blank.png", ' ', false, false),
    Mur(Language.getString("case.wall.text"), "Images/PNG/Retina/wall.png", 'M', false, true),
    MurCassable(Language.getString("case.brokenwall.text"), "Images/PNG/Retina/brokenWall.png", 'N', false, true),
    Buisson_Vert(Language.getString("case.greenbush.text"), "Images/PNG/Retina/buisson.png", 'B', false, false),
    Buisson_Orange(Language.getString("case.orangebush.text"), "Images/PNG/Retina/treeBrown_large.png", 'C', false, false),
    Trou(Language.getString("case.hole.text"), "Images/PNG/Retina/hole.png", 'T', false, true),
    Joueur(Language.getString("case.player.text"), "Images/PNG/Retina/tank_" + MainMenu.getTankType() + ".png", 'J', false, false),
    Bot(Language.getString("case.bot.text"), "Images/PNG/Retina/tank_red.png", 'R', false, false),
    Boss(Language.getString("case.boss.text"), "Images/PNG/Retina/tank_bigRed.png", 'Z', false, false);

    private static final Map<Character, CaseType> BY_LETTER = new HashMap<>();

    static {
        for (CaseType e : values()) {
            BY_LETTER.put(e.getLettre(), e);
        }
    }

    public final String name;
    public final char lettre;
    public final boolean bg;
    public final boolean obstacle;
    public ImageIcon image;

    CaseType(String name, String imagePath, char lettre, boolean bg, boolean obstacle) {
        this.name = name;
        this.image = new ImageIcon(ClassLoader.getSystemResource(imagePath));
        image.setDescription(imagePath);
        this.lettre = lettre;
        this.bg = bg;
        this.obstacle = obstacle;
    }

    public static CaseType parse(char c) {
        return BY_LETTER.get(c);
    }

    public static String caseDescription(String firstChar) {
        String to_return = "#BackGround:\n";
        LinkedList<CaseType> tmp = new LinkedList<>();
        for (CaseType type : CaseType.values()) {
            if (type.bg) {
                to_return += firstChar + " " + type.getLettre() + " : " + type.getName() + "\n";
            } else {
                tmp.add(type);
            }
        }
        to_return += "\n#ForeGround:\n";
        for (CaseType type : tmp) {
            to_return += firstChar + " " + type.getLettre() + " : " + type.getName() + "\n";
        }
        return to_return;
    }

    public static boolean enumCorrect() {
        LinkedList<Character> tmp = new LinkedList<>();
        for (CaseType type : CaseType.values()) {
            if (tmp.contains(type.getLettre())) {
                logger.fatal("ENUM ERROR (DUPLICATE), Probleme with: Name: " + type.getName() + " Letter: " + type.getLettre());
                return false;
            }
            tmp.add(type.getLettre());
        }
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ImageIcon getImage() {
        return image;
    }

    @Override
    public char getLettre() {
        return lettre;
    }

    @Override
    public String toString() {
        return image.getDescription() + ";" + name + ";" + lettre;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }


}