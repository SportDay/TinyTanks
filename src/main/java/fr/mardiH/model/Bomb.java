package fr.mardiH.model;

public class Bomb {
    //Le temps auquel la bombe a été crée
    protected double ancienTimer;
    //Les coordonnées du centre
    protected Coordonnees coordonnees;
    //Temps en secondes avant l'explosion
    protected double decompte = 2;

    public Bomb(double ancienTimer, double x, double y) {
        this.ancienTimer = ancienTimer;
        this.coordonnees = new Coordonnees(x, y);
    }

    public boolean chute() { // Mise à jour du décompte
        double tmp = ancienTimer;
        ancienTimer = System.currentTimeMillis();
        decompte -= (ancienTimer - tmp) / 1000;
        return decompte < 0;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public double getDecompte() {
        return decompte;
    }

}
