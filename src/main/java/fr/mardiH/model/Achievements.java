package fr.mardiH.model;


import fr.mardiH.util.Language;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Achievements extends Observable implements Serializable {

    private final String pcUUID = System.getProperty("os.name") + "#" + System.getProperty("user.name") + "#" + System.getenv("USERDOMAIN") + "#" + System.getProperty("user.home");
    /* done */ private final Achievement nbrKill = new Achievement("achi.numtankkill.title", "achi.numtankkill.text", 50, true);
    /* done */ private final Achievement nbrDie = new Achievement("achi.nbrdie.title", "achi.nbrdie.text", 50, true);
    /* done */ private final Achievement playTime = new Achievement("achi.playtime.title", "achi.playtime.text", 100, true);
    /* done */ private final Achievement noDie = new Achievement("achi.nodie.title", "achi.nodie.text");
    /* done */ private final Achievement campagne = new Achievement("achi.campaign.title", "achi.campaign.text", 6, true);
    /* done */ private final Achievement endGame = new Achievement("achi.endgame.title", "achi.endgame.text");
    /* done */ private final Achievement viewCredits = new Achievement("achi.viewcredits.title", "achi.viewcredits.text");
    /* done */ private final Achievement makeMap = new Achievement("achi.makemap.title", "achi.makemap.text");
    /* done */ private final Achievement planeSurv = new Achievement("achi.planesurv.title", "achi.planesurv.text");
    /* done */ private final Achievement botKillBot = new Achievement("achi.botkillbot.title", "achi.botkillbot.text");
    /* done */ private final Achievement noBullet = new Achievement("achi.nobullet.title", "achi.nobullet.text");

    public String getPcUUID() {
        return pcUUID;
    }

    public Achievement getNbrKill() {
        return nbrKill;
    }

    public Achievement getNbrDie() {
        return nbrDie;
    }

    public Achievement getPlayTime() {
        return playTime;
    }

    public Achievement getNoDie() {
        return noDie;
    }

    public Achievement getCampagne() {
        return campagne;
    }

    public Achievement getViewCredits() {
        return viewCredits;
    }

    public Achievement getMakeMap() {
        return makeMap;
    }

    public Achievement getPlaneSurv() {
        return planeSurv;
    }

    public Achievement getBotKillBot() {
        return botKillBot;
    }

    public Achievement getNoBullet() {
        return noBullet;
    }

    public Achievement getEndGame() {
        return endGame;
    }

    public class Achievement implements Serializable {
        private final String title;
        private final String text;
        private final int max;
        private final boolean progress;
        private double current = 0;
        private boolean unlocked = false;

        public Achievement(String title, String text, int max, boolean progress) {
            this.title = title;
            this.text = text;
            this.max = max;
            this.progress = progress;
        }

        public Achievement(String title, String text) {
            this(title, text, 0, false);
        }

        public void addCurrent(double current) {
            this.current = BigDecimal.valueOf(this.current).add(BigDecimal.valueOf(current)).doubleValue();
            this.current = Double.parseDouble(new DecimalFormat(".###").format(this.current).replaceAll(",", "."));
            if (this.current >= max) {
                unlocked = true;
            }
        }

        public void addCurrent() {
            if (progress && !unlocked) {
                this.current++;
                if (current >= max) {
                    unlocked = true;
                }
                setChanged();
                notifyObservers();
            }
        }

        public String getTitle() {
            return Language.getString(title);
        }

        public String getText() {
            if (progress) {
                return Language.getString(text).replaceAll("%num%", max + "");
            }
            return Language.getString(text);
        }

        public double getCurrent() {
            return current;
        }

        public int getMax() {
            return max;
        }

        public boolean isUnlocked() {
            return unlocked;
        }

        public void setUnlocked(boolean unlocked) {
            this.unlocked = unlocked;
            setChanged();
            notifyObservers();
        }

        public boolean isProgress() {
            return progress;
        }
    }
}
