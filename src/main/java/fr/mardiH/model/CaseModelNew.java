package fr.mardiH.model;

import fr.mardiH.model.Enum.CaseType;
import fr.mardiH.model.interfaces.CaseM;

import javax.swing.*;

public class CaseModelNew {

    public int x;
    public int y;
    private JLabel panel;
    private CaseM caseType;

    public CaseModelNew(CaseM caseType, int x, int y) {
        this.caseType = caseType;
        this.x = x;
        this.y = y;
    }

    public JLabel getLabel() {
        return panel;
    }

    public void setLabel(JLabel panel) {
        this.panel = panel;
    }

    public CaseM getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseM caseType) {
        this.caseType = caseType;
    }

    public boolean isObstacle() {
        if (caseType instanceof CaseType) {
            return ((CaseType) caseType).obstacle;
        }
        return false;
    }

    public boolean isAWall() {
        return caseType == CaseType.Mur || caseType == CaseType.MurCassable;
    }

    public boolean isABush() {
        return caseType == CaseType.Buisson_Vert || caseType == CaseType.Buisson_Orange;
    }

}
