package fr.mardiH.model;

import com.studiohartman.jamepad.ControllerIndex;

public class GamePadByName {
    private final ControllerIndex controller;
    private final String name;
    private final int id;

    public GamePadByName(ControllerIndex controller, String name, int id) {
        this.controller = controller;
        this.name = name;
        this.id = id;
    }

    public ControllerIndex getController() {
        return controller;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
