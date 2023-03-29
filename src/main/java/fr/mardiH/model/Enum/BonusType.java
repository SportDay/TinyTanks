package fr.mardiH.model.Enum;

import fr.mardiH.model.TankModel;
import fr.mardiH.model.interfaces.CaseM;
import fr.mardiH.view.MainMenu;

import javax.swing.*;

import static fr.mardiH.Main.logger;

public enum BonusType implements CaseM {

    // .5 = 50%

    SPEED_UP("Speed UP", "Images/Bonus/speedB.png", 350) {
        @Override
        public void execute(TankModel tank) {
            tank.setTimeFast(System.currentTimeMillis());
            tank.setFast(true);
        }
    },
    SLOW("Slow", "Images/Bonus/slow.png", 350) {
        @Override
        public void execute(TankModel tank) {
            tank.setTimeSlow(System.currentTimeMillis());
            tank.setSlowed(true);
        }
    },
    LIFE("Extra Life", "Images/Bonus/heart.png", 50) {
        @Override
        public void execute(TankModel tank) {
            MainMenu.increaseLife();
        }
    },
    SHIELD("Shield", "Images/Bonus/shield.png", 125) {
        @Override
        public void execute(TankModel tank) {
            tank.setTimeShield(System.currentTimeMillis());
            tank.setShield(true);
        }
    },
    INVISIBILITY("Invisibility", "Images/Bonus/magic_cloak.png", 125) {
        @Override
        public void execute(TankModel tank) {
            tank.setTimeInvi(System.currentTimeMillis());
            tank.setInvisible(true);
        }
    },
    WARN("Warning", "Images/Bonus/warn.gif", 0) {
        @Override
        public void execute(TankModel tank) {
        }
    };


    private static int nextNum = 0;
    public final String name;
    public final int prob;
    public final char lettre = Character.MAX_VALUE;
    public ImageIcon image;
    public int num;


    BonusType(String name, String imagePath, int prob) {
        this.name = name;
        this.image = new ImageIcon(ClassLoader.getSystemResource(imagePath));
        image.setDescription(imagePath);
        this.prob = prob;
        makeNum();
    }

    public static BonusType random(boolean custom) {
        BonusType to_return = testRand();
        if (custom) {
            while (to_return == BonusType.LIFE) {
                to_return = testRand();
            }
        }
        return to_return;
    }

    private static BonusType testRand() {
        double rnd = Math.random() * 1000;
        for (BonusType e : values()) {
            if (rnd < e.prob) {
                return e;
            }
            rnd -= e.prob;
        }
        return BonusType.SPEED_UP;
    }

    public static boolean enumCorrect() {
        int sum = 0;
        for (BonusType type : BonusType.values()) {
            sum += type.prob;
        }
        if (sum != 1000) {
            logger.fatal("ENUM TOTAL PROBABILITY IS: " + sum + ", MUST BE 1000");
            return false;
        }
        return true;
    }

    private void makeNum() {
        num = nextNum++;
    }

    @Override
    public char getLettre() {
        return lettre;
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
    public void destroy() {
        //TODO
    }

    @Override
    public String toString() {
        return "";
    }

    public abstract void execute(TankModel tank);
}
