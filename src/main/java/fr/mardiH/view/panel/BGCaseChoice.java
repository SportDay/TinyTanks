package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.CreateMenuController;
import fr.mardiH.model.CreatorMapJRadioButtonModel;
import fr.mardiH.model.Enum.CaseType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BGCaseChoice extends JPanel {

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final CreateMenuController controller;

    public BGCaseChoice(CreateMenuController controller) {
        this.controller = controller;
        init();
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    private void init() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                CreateMenuController.UpdateIcon updateIcon = controller.new UpdateIcon();
                updateIcon.execute();
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        for (CaseType caseT : CaseType.values()) {
            if (caseT.bg) {
                JRadioButton tmp = new CreatorMapJRadioButtonModel(caseT, controller);
                buttonGroup.add(tmp);
                panel.add(tmp);
            }
        }

    }

}
