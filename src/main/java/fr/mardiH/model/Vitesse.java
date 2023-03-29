package fr.mardiH.model;

public class Vitesse {
    double dx = 0;
    double dy = 0;

    public Vitesse(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void add(double x, double y) {
        dx += x;
        dy += y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

}
