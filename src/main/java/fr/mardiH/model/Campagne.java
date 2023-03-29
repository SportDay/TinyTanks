package fr.mardiH.model;

import fr.mardiH.view.MainMenu;

import java.io.Serializable;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Campagne extends Observable implements Serializable {

    private final String pcUUID = System.getProperty("os.name") + "#" + System.getProperty("user.name") + "#" + System.getenv("USERDOMAIN") + "#" + System.getProperty("user.home");
    private int levelNum = 0;
    private int life = 5;

    public String getPcUUID() {
        return pcUUID;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        if (MainMenu.totalPrivateLevel > levelNum) {
            this.levelNum = levelNum;
            setChanged();
            notifyObservers();
        }
    }

    public void increaseLevel() {
        if (MainMenu.totalPrivateLevel > levelNum) {
            this.levelNum++;
            setChanged();
            notifyObservers();
        }
    }

    public int getLife() {
        return life;
    }

    public void setlife(int life) {
        this.life = life;
        setChanged();
        notifyObservers();
    }

    public void decreaseLife() {
        this.life--;
        setChanged();
        notifyObservers();
    }
}
