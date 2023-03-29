package fr.mardiH.model;

import fr.mardiH.util.Language;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Locale;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class SettingsModel extends Observable implements Serializable {

    private int effectVolume = 100;
    private int musicVolume = 100;
    private int menuVolume = 100;

    private int btnUP = KeyEvent.VK_UP;
    private int btnLEFT = KeyEvent.VK_LEFT;
    private int btnDOWN = KeyEvent.VK_DOWN;
    private int btnRIGHT = KeyEvent.VK_RIGHT;

    private Locale lang = Locale.getDefault();


    private int tankModel;

    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang = lang;
        Language.updateResourceBundle();
        setChanged();
        notifyObservers();
    }

    public int getEffectVolume() {
        return effectVolume;
    }

    public void setEffectVolume(int effectVolume) {
        this.effectVolume = effectVolume;
        setChanged();
        notifyObservers();
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
        setChanged();
        notifyObservers();
    }

    public int getMenuVolume() {
        return menuVolume;
    }

    public void setMenuVolume(int menuVolume) {
        this.menuVolume = menuVolume;
        setChanged();
        notifyObservers();
    }

    public int getTankModel() {
        return tankModel;
    }

    public void setTankModel(int tankModel) {
        this.tankModel = tankModel;
        setChanged();
        notifyObservers();
    }

    public int getBtnUP() {
        return btnUP;
    }

    public void setBtnUP(int btnUP) {
        this.btnUP = btnUP;
        setChanged();
        notifyObservers();
    }

    public int getBtnLEFT() {
        return btnLEFT;
    }

    public void setBtnLEFT(int btnLEFT) {
        this.btnLEFT = btnLEFT;
        setChanged();
        notifyObservers();
    }

    public int getBtnDOWN() {
        return btnDOWN;
    }

    public void setBtnDOWN(int btnDOWN) {
        this.btnDOWN = btnDOWN;
        setChanged();
        notifyObservers();
    }

    public int getBtnRIGHT() {
        return btnRIGHT;
    }

    public void setBtnRIGHT(int btnRIGHT) {
        this.btnRIGHT = btnRIGHT;
        setChanged();
        notifyObservers();
    }
}
