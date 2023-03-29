package fr.mardiH.util;

import fr.mardiH.view.MainMenu;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Random;

public class Sound {

    public int framePos = 0; // temps dans l'audio quand la musique des menus est mise en pause
    private final JFrame source;
    private Clip clip = null;

    public Sound(JFrame source) {
        this.source = source;
    }

    public void btnClick() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/button2.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getMenuVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void win() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/LevelUP.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void pop() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            int r = new Random().nextInt(2) + 1;
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/pop" + r + ".wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void lose() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/died.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void credits() {
        AudioInputStream audioInputStream = null;
        clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/musiques/Undertale.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (25f * (float) Math.log10(((MainMenu) source).getSettingsModel().getMusicVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void end() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/end.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void explosion() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/explosion.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public void level(boolean boss) {
        AudioInputStream audioInputStream = null;
        clip = null;
        try {
            if (boss) {
                audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/musiques/Battle_Against_A_True_Hero.wav"));
            } else {
                int r = new Random().nextInt(9) + 1;
                audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/musiques/variation" + r + ".wav"));
            }
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getMusicVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void menu(boolean b) {
        AudioInputStream audioInputStream = null;
        clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/musiques/Force_On_The_Move.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            // ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (b) {
            clip.setFramePosition(framePos);
        } else {
            clip.setFramePosition(0);
        }

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getMusicVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void pause() {
        framePos = clip.getFramePosition();
        clip.stop();
    }

    public void raid() {
        AudioInputStream audioInputStream = null;
        Clip clip = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource("/audio/raid.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }

        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.setFramePosition(0);

        float gain = 0;
        if (source instanceof MainMenu) {
            gain = (20f * (float) Math.log10(((MainMenu) source).getSettingsModel().getEffectVolume() / 100f));
        }

        gainControl.setValue(gain);

        clip.start();
    }

    public Clip getClip() {
        return clip;
    }

}