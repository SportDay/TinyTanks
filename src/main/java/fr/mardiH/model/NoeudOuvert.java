package fr.mardiH.model;

public class NoeudOuvert {
    private double coutG;
    private double coutH;
    private double coutF;
    private final NoeudOuvert parent;
    private final int[] coordonnees;

    public NoeudOuvert(NoeudOuvert parent, int x, int y, double h, double g) {
        this.parent = parent;
        this.coordonnees = new int[2];
        this.coordonnees[0] = x;
        this.coordonnees[1] = y;
        this.coutH = h;
        this.coutG = g;
        this.coutF = h + g;
    }

    public double getCoutF() {
        return coutF;
    }

    public void setCoutF(double coutF) {
        this.coutF = coutF;
    }

    public double getCoutG() {
        return coutG;
    }

    public void setCoutG(double coutG) {
        this.coutG = coutG;
    }

    public double getCoutH() {
        return coutH;
    }

    public void setCoutH(double coutH) {
        this.coutH = coutH;
    }

    public NoeudOuvert getParent() {
        return parent;
    }

    public int getX() {
        return coordonnees[0];
    }

    public int getY() {
        return coordonnees[1];
    }

}
