package fr.mardiH.controller.jeux;

import fr.mardiH.model.CaseModelNew;
import fr.mardiH.model.NoeudOuvert;
import fr.mardiH.model.PlateauModel;
import fr.mardiH.model.Tank.IA.Bot;
import fr.mardiH.model.TankModel;
import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.view.panel.GameView;

import java.util.LinkedList;
import java.util.List;

public class BotControlleur extends IaControlleur {

    public BotControlleur(GameView vue, PlateauModel plateau, TankModel tank) {
        super(vue, plateau, tank);
    }

    /**
     * Il renvoie la distance entre deux points
     *
     * @param x1 Coordonnée x du premier point.
     * @param y1 La coordonnée y du premier point
     * @param x2 Coordonnée x du deuxième point.
     * @param y2 La coordonnée y du deuxième point.
     * @return La distance entre deux points.
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public void nextMove() {
        //Si le bot vois le joueur :
        //  Aller dans un direction aléatoire accessible
        //Sinon :
        //  Si le bot vois le joueur dans les case autour accessible :
        //    Aller dans une de ces direction
        //  Sinon :
        //    Se diriger vers le joueur
        if (getPlateau().getTank().isInvisible()) {
            if(((Bot) model).getTimeDest() == -1 || System.currentTimeMillis() - ((Bot) model).getTimeDest() > 750) {
                ((Bot) model).setTimeDest(System.currentTimeMillis());
                LinkedList<CaseModelNew> dest = voisinsDeplacement();
                ((Bot) model).getChemin().clear();
                if (!dest.isEmpty()) {
                    ((Bot) model).getChemin().add(dest.get((int) (Math.random() * dest.size())));
                }
            }
            Bot.setBushList(voisinsBushs((int) plateau.getTank().getTank().getCentre().getX() / 34, (int) plateau.getTank().getTank().getCentre().getY() / 34));
        }
        else {
            if (tirSansObstacles()) {
                if (((Bot) model).getTimeDest() == -1 || System.currentTimeMillis() - ((Bot) model).getTimeDest() > 1000 || ((Bot) model).getChemin().isEmpty()) {
                    ((Bot) model).setTimeDest(System.currentTimeMillis());
                    LinkedList<CaseModelNew> dest = voisinsDeplacement();
                    ((Bot) model).getChemin().clear();
                    if (!dest.isEmpty()) {
                        ((Bot) model).getChemin().add(dest.get((int) (Math.random() * dest.size())));
                    }
                }
            } else {
                ((Bot) model).setTimeDest(-1);
                if (((Bot) model).getTimeDir() == -1 || System.currentTimeMillis() - ((Bot) model).getTimeDir() > 1000) {
                    ((Bot) model).setTimeDir(System.currentTimeMillis());
                    LinkedList<CaseModelNew> dest = voisinsDeplacementVision();
                    if (dest.isEmpty()) {
                        ((Bot) model).getChemin().clear();
                        dest = directionJoueur(((int) plateau.getTank().getTank().getCentre().getX() / 34), ((int) plateau.getTank().getTank().getCentre().getY() / 34));
                        if (dest.isEmpty()) {
                            ((Bot) model).setTimeDest(System.currentTimeMillis());
                            dest = voisinsDeplacement();
                            if (!dest.isEmpty()) {
                                ((Bot) model).getChemin().clear();
                                ((Bot) model).getChemin().add(dest.get((int) (Math.random() * dest.size())));
                            }
                        } else {
                            ((Bot) model).getChemin().addAll(dest);
                        }
                    } else {
                        ((Bot) model).getChemin().clear();
                        ((Bot) model).getChemin().add(dest.get((int) (Math.random() * dest.size())));
                    }
                }
            }
        }
        if (!((Bot) model).getChemin().isEmpty()) {
            boolean[] dir = getDirectionChemin();
            boolean f = false;
            for (boolean b : dir) {
                if (b) {
                    f = true;
                }
            }
            if (f) {
                haut = dir[0];
                gauche = dir[1];
                droite = dir[2];
                bas = dir[3];
                if (haut) {
                    model.VerticalH = true;
                }
                if (gauche) {
                    model.HorizontalG = true;
                }
                if (droite) {
                    model.HorizontalG = false;
                }
                if (bas) {
                    model.VerticalH = false;
                }
            }
        }
    }

    /**
     * Il renvoie une liste de tous les cas possibles vers lesquels le char peut se déplacer
     *
     * @return Une liste d'objets CaseModelNew.
     */
    public LinkedList<CaseModelNew> voisinsDeplacement() {
        LinkedList<CaseModelNew> destination = new LinkedList<CaseModelNew>();
        int x = (int) model.getTank().getCentre().getX() / 34;
        int y = (int) model.getTank().getCentre().getY() / 34;
        if (x - 1 >= 0 && !plateau.getCaseForeground(x - 1, y).isObstacle()) {
            destination.add(plateau.getCaseForeground(x - 1, y));
            if (y - 1 >= 0 && !plateau.getCaseForeground(x, y - 1).isObstacle()) {
                if (!plateau.getCaseForeground(x - 1, y - 1).isObstacle()) {
                    destination.add(plateau.getCaseForeground(x - 1, y - 1));
                }
            }
            if (y + 1 < 16 && !plateau.getCaseForeground(x, y + 1).isObstacle()) {
                if (!plateau.getCaseForeground(x - 1, y + 1).isObstacle()) {
                    destination.add(plateau.getCaseForeground(x - 1, y + 1));
                }
            }
        }
        if (x + 1 < 23 && !plateau.getCaseForeground(x + 1, y).isObstacle()) {
            destination.add(plateau.getCaseForeground(x + 1, y));
            if (y - 1 >= 0 && !plateau.getCaseForeground(x, y - 1).isObstacle()) {
                if (!plateau.getCaseForeground(x + 1, y - 1).isObstacle()) {
                    destination.add(plateau.getCaseForeground(x + 1, y - 1));
                }
            }
            if (y + 1 < 16 && !plateau.getCaseForeground(x, y + 1).isObstacle()) {
                if (!plateau.getCaseForeground(x + 1, y + 1).isObstacle()) {
                    destination.add(plateau.getCaseForeground(x + 1, y + 1));
                }
            }
        }
        if (y - 1 >= 0 && !plateau.getCaseForeground(x, y - 1).isObstacle()) {
            destination.add(plateau.getCaseForeground(x, y - 1));
        }
        if (y + 1 < 16 && !plateau.getCaseForeground(x, y + 1).isObstacle()) {
            destination.add(plateau.getCaseForeground(x, y + 1));
        }
        return destination;
    }

    /**
     * Il renvoie une liste de tous les cas qui sont adjacents au cas actuel et qui sont visibles par le
     * cas actuel
     *
     * @return Une liste d'objets CaseModelNew.
     */
    public LinkedList<CaseModelNew> voisinsDeplacementVision() {
        LinkedList<CaseModelNew> res = voisinsDeplacement();
        LinkedList<CaseModelNew> delete = new LinkedList<>();
        for (CaseModelNew caseModelNew : res) {
            if (!tirSansObstacles(caseModelNew.x, caseModelNew.y)) {
                delete.add(caseModelNew);
            }
        }
        res.removeAll(delete);
        return res;
    }

    /**
     * Il renvoie une liste de tous les cas qui sont des buissons et sont adjacents au cas à (x, y)
     *
     * @param x la coordonnée x du cas
     * @param y la coordonnée y du cas
     * @return Une liste d'objets CaseModelNew.
     */
    public LinkedList<CaseModelNew> voisinsBushs(int x, int y) {
        LinkedList<CaseModelNew> destination = new LinkedList<CaseModelNew>();
        if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34, x, y)) {
            destination.add(plateau.getCaseForeground(x, y));
        }
        if (x - 1 >= 0 && plateau.getCaseForeground(x - 1, y).isABush()) {
            if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34, x - 1, y)) {
                destination.add(plateau.getCaseForeground(x - 1, y));
            }
            if (y - 1 >= 0 && plateau.getCaseForeground(x, y - 1).isABush()) {
                if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y - 1)) {
                    destination.add(plateau.getCaseForeground(x, y - 1));
                }
            }
            if (y + 1 < 16 && plateau.getCaseForeground(x, y + 1).isABush()) {
                if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y + 1)) {
                    destination.add(plateau.getCaseForeground(x, y + 1));
                }
            }
        }
        if (x + 1 < 23 && plateau.getCaseForeground(x + 1, y).isABush()) {
            if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x + 1 , y)) {
                destination.add(plateau.getCaseForeground(x + 1, y));
            }
            if (y - 1 >= 0 && plateau.getCaseForeground(x, y - 1).isABush()) {
                if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y - 1)) {
                    destination.add(plateau.getCaseForeground(x, y - 1));
                }
            }
            if (y + 1 < 16 && plateau.getCaseForeground(x, y + 1).isABush()) {
                if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y + 1)) {
                    destination.add(plateau.getCaseForeground(x, y + 1));
                }
            }
        }
        if (y - 1 >= 0 && plateau.getCaseForeground(x, y - 1).isABush()) {
            if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y - 1)) {
                destination.add(plateau.getCaseForeground(x, y - 1));
            }
        }
        if (y + 1 < 16 && plateau.getCaseForeground(x, y + 1).isABush()) {
            if(tirSansObstacles(((Bot) model).getTank().getCentre().getX()/34, ((Bot) model).getTank().getCentre().getY()/34,x , y + 1)) {
                destination.add(plateau.getCaseForeground(x, y + 1));
            }
        }
        return destination;
    }

    public LinkedList<CaseModelNew> directionJoueur(int x, int y) { // Algo A* Cas ou pas de solution a faire
        LinkedList<CaseModelNew> res = new LinkedList<CaseModelNew>();
        List<NoeudOuvert> ouvert = new LinkedList<NoeudOuvert>(); // liste des noeud a évalué
        List<NoeudOuvert> ferme = new LinkedList<NoeudOuvert>(); // liste des noeuds deja évalué
        int xb = (int) model.getTank().getCentre().getX() / 34;
        int yb = (int) model.getTank().getCentre().getY() / 34;
        ouvert.add(new NoeudOuvert(null, xb, yb, distance(xb, yb, x, y), 0)); //on ajoute la case actuelle commme premier noeud
        while (!ouvert.isEmpty()) {
            int indice = 0; //indice du noeud avec le plus petit cout
            double minF = ouvert.get(0).getCoutF(); //coutF du premier noeud
            for (int i = 1; i < ouvert.size(); i++) { //on cherche le noeud avec le plus petit coutF
                if (ouvert.get(i).getCoutF() < minF) {
                    indice = i;
                    minF = ouvert.get(i).getCoutF();
                }
            }
            NoeudOuvert act = ouvert.remove(indice); //on prend le noeud avec le plus petit coutF et on l'enleve de la liste ouvert
            ferme.add(act); //on l'ajoute a la liste ferme
            if (ouvert.size() > 368 || plateau.getCaseForeground(act.getX(), act.getY()).equals(plateau.getCaseForeground(x, y))) { //si toutes les cases on été évalué ou si on a trouvé la case cible
                while (act.getParent() != null) { //on ajoute les noeuds parents de la case cible
                    res.add(plateau.getCaseForeground(act.getX(), act.getY()));
                    act = act.getParent();
                }
                return res;
            }
            LinkedList<NoeudOuvert> voisins = plateau.getVoisin(act, (Bot) model); //On obtient les voisins de la case que l'on traite
            for (NoeudOuvert n : voisins) { //On parcours toute la liste des noeuds voisins
                boolean contien = false;
                for (NoeudOuvert f : ferme) { //On regarde si le noeud voisins est dans la liste ferme
                    if (plateau.getCaseForeground(f.getX(), f.getY()).equals(plateau.getCaseForeground(n.getX(), n.getY()))) {
                        contien = true;
                        break;
                    }
                }
                if (!contien) { //Si elle n'est pas dedans on la traite
                    if (n.getCoutF() < act.getCoutF()) { //erreur du code if qui ne sert a rien
                        for (NoeudOuvert o : ouvert) { // si la liste ouvert ne contient pas le noeud on l'ajoute 
                            if (plateau.getCaseForeground(o.getX(), o.getY()).equals(plateau.getCaseForeground(n.getX(), n.getY()))) {
                                contien = true;
                                break;
                            }
                        }
                        if (!contien) {
                            ouvert.add(n);
                        }
                    } else {
                        for (NoeudOuvert o : ouvert) {
                            if (plateau.getCaseForeground(o.getX(), o.getY()).equals(plateau.getCaseForeground(n.getX(), n.getY()))) {
                                contien = true;
                                break;
                            }
                        }
                        if (!contien) {
                            ouvert.add(n);
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * Il renvoie un tableau de booléens qui indique au bot dans quelle direction aller pour atteindre le
     * cas suivant dans le chemin
     *
     * @return Un tableau booléen de taille 4.
     */
    public boolean[] getDirectionChemin() {
        double x = model.getTank().getCentre().getX() / 34;
        double y = model.getTank().getCentre().getY() / 34;
        boolean[] res = new boolean[4];
        res[0] = false;
        res[1] = false;
        res[2] = false;
        res[3] = false;
        CaseModelNew dest = ((LinkedList<CaseModelNew>) ((Bot) model).getChemin()).getLast();
        int xDest = dest.x;
        int yDest = dest.y;
        if (xDest + 0.4 <= x && xDest + 0.6 >= x) {
            if (yDest + 0.4 <= y && yDest + 0.6 >= y) {
                ((LinkedList<CaseModelNew>) ((Bot) model).getChemin()).removeLast();
            } else if (yDest + 0.4 <= y) {
                res[0] = true;
            } else {
                res[3] = true;
            }
        } else if (xDest + 0.4 <= x) {
            res[1] = true;
            if (yDest + 0.4 <= y && yDest + 0.6 >= y) {
            } else if (yDest + 0.4 <= y) {
                res[0] = true;
            } else {
                res[3] = true;
            }
        } else {
            res[2] = true;
            if (yDest + 0.4 <= y && yDest + 0.6 >= y) {
            } else if (yDest + 0.4 <= y) {
                res[0] = true;
            } else {
                res[3] = true;
            }
        }
        return res;
    }

    public void setPlateau(PlateauModel plateau) {
        this.plateau = plateau;
    }

    /**
     * Si le char est invisible, il tirera sur un buisson au hasard. Si le char est visible, il tirera sur
     * le joueur
     *
     * @param t Modèle du tank
     */
    public void tir(TankModel t) {
        short r = (short) (Math.random() * 50);
        if (r == 0) {
            if (plateau.getTank().isInvisible()) {
                if (plateau.getTank().isHideOnBush() && !Bot.getBushList().isEmpty()) {
                    r = (short) (Math.random() * Bot.getBushList().size());
                    CaseModelNew tmp = Bot.getBushList().get(r);
                    ((Bot) model).setOrientationCanon(tmp);
                    super.tir(t);
                }
            } else {
                if (((IaControlleur) t.controller).tirSansObstacles()) {
                    super.tir(t);
                }
            }
        }
    }

}
