package fr.mardiH.util;

import fr.mardiH.model.Achievements;
import fr.mardiH.model.Campagne;
import fr.mardiH.model.Enum.FileType;
import fr.mardiH.model.SettingsModel;
import fr.mardiH.view.MainMenu;

import javax.crypto.*;
import javax.swing.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Security {


    /**
     * La fonction checkOk vérifie si l'UUID du PC est différent de celui stocké dans les fichiers des achievemetns et de campagne.
     * Si c'est le cas, un message d'avertissement apparaît indiquant qu'il pourrait y avoir une tentative de triche.
     * La fonction enregistre également les deux fichiers avec un nouvel UUID afin qu'ils ne puissent plus être utilisés par quelqu'un d'autre.
     */
    public void checkOk() {
        String pcUUID = System.getProperty("os.name") + "#" + System.getProperty("user.name") + "#" + System.getenv("USERDOMAIN") + "#" + System.getProperty("user.home");
        if (!MainMenu.Instance.getAchievements().getPcUUID().equalsIgnoreCase(pcUUID) | !MainMenu.Instance.getCampagne().getPcUUID().equalsIgnoreCase(pcUUID)) {
            JOptionPane.showMessageDialog(MainMenu.Instance,
                    Language.getString("text.anticheat.text"),
                    Language.getString("text.anticheat.title"),
                    JOptionPane.WARNING_MESSAGE);
            saveCrypt(FileType.achievements, true);
            saveCrypt(FileType.campagne, true);
            MainMenu.Instance.setMainMenu();
        }
    }

    /**
     * La fonction getKey renvoie un objet SecretKey qui est utilisé pour chiffrer et déchiffrer le message
     */
    private SecretKey getKey() {
        try (ObjectInputStream ois = new ObjectInputStream(ClassLoader.getSystemResourceAsStream("I_dont_know_what.is"))) {
            return (SecretKey) ois.readObject();
        } catch (IOException | ClassNotFoundException ignored) {

        }
        return null;
    }

    /**
     * La fonction openCrypt lit un fichier chiffrer.
     *
     * @param type Déterminer quel fichier il faut ouvrir
     * @return L'objet qui a été lu à partir du fichier
     */
    public void openCrypt(FileType type) {
        ObjectInputStream ois = null;
        try {
            FileInputStream fichier = new FileInputStream(Constants.PATH + type + ".tinyTanks");
            ois = new ObjectInputStream(fichier);

            Cipher dcipher = Cipher.getInstance("AES");
            dcipher.init(Cipher.DECRYPT_MODE, getKey());


            if (type == FileType.campagne) {
                MainMenu.Instance.setCampagne((Campagne) ((SealedObject) ois.readObject()).getObject(dcipher));
            } else if (type == FileType.achievements) {
                MainMenu.Instance.setAchievements((Achievements) ((SealedObject) ois.readObject()).getObject(dcipher));
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | IOException | ClassNotFoundException e) {
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ignored) {

            }
        }
    }

    /**
     * La fonction saveCrypt enregistre les données du jeu dans un fichier chifrer
     *
     * @param type  Déterminer quel fichier il faut enregistrer
     * @param erase Determine si il faut cree un nouveau fichier
     */
    public void saveCrypt(FileType type, boolean erase) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fichier = new FileOutputStream(Constants.PATH + type + ".tinyTanks");
            oos = new ObjectOutputStream(fichier);
            SealedObject sealedObject = null;
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            if (type == FileType.campagne) {
                if (erase) {
                    MainMenu.Instance.setCampagne(new Campagne());
                }
                sealedObject = new SealedObject(MainMenu.Instance.getCampagne(), cipher);
            } else if (type == FileType.achievements) {
                if (erase) {
                    MainMenu.Instance.setAchievements(new Achievements());
                }
                sealedObject = new SealedObject(MainMenu.Instance.getAchievements(), cipher);
            }
            if (sealedObject == null) {
                throw new NullPointerException("sealedObject is null");
            }
            oos.writeObject(sealedObject);
        } catch (IOException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 InvalidKeyException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException ignored) {

            }
        }
    }


    /**
     * La fonction saveFile enregistre les données du jeu dans un fichier
     *
     * @param type  Déterminer quel fichier il faut enregistrer
     * @param erase Determine si il faut cree un nouveau fichier
     */
    public void saveFile(FileType type, boolean erase) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fichier = new FileOutputStream(Constants.PATH + type + ".tinyTanks");
            oos = new ObjectOutputStream(fichier);
            if (type == FileType.settings) {
                if (erase) {
                    MainMenu.Instance.setSettingsModel(new SettingsModel());
                }
                oos.writeObject(MainMenu.Instance.getSettingsModel());
            }
            oos.flush();
        } catch (final java.io.IOException ignored) {

        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ignored) {

            }
        }
    }

    /**
     * La fonction openSerial lit un fichier.
     *
     * @param type Déterminer quel fichier il faut ouvrir
     * @return L'objet qui a été lu à partir du fichier
     */
    public void openSerial(FileType type) {
        ObjectInputStream ois = null;
        try {
            FileInputStream fichier = new FileInputStream(Constants.PATH + type + ".tinyTanks");
            ois = new ObjectInputStream(fichier);
            if (type == FileType.settings) {
                MainMenu.Instance.setSettingsModel((SettingsModel) ois.readObject());
            }

        } catch (final IOException | ClassNotFoundException ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ignored) {

            }
        }
    }
}
