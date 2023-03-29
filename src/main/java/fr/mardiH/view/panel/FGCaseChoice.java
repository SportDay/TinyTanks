package fr.mardiH.view.panel;

import fr.mardiH.controller.panel.CreateMenuController;
import fr.mardiH.model.CreatorMapJRadioButtonModel;
import fr.mardiH.model.Enum.CaseType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class FGCaseChoice extends JPanel {

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final CreateMenuController controller;

    public FGCaseChoice(CreateMenuController controller) {
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
            if (!caseT.bg) {
                if (caseT.lettre == ' ') {
                    JRadioButton dell = new CreatorMapJRadioButtonModel(CaseType.Vide, controller);
                    buttonGroup.add(dell);
                    KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK);
                    dell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "paintLevel");
                    dell.getActionMap().put("paintLevel", controller.new Action(dell));
                    dell.setText(dell.getText().replaceAll("%r%", "DEL"));

                    panel.add(dell);
                } else {
                    JRadioButton tmp = new CreatorMapJRadioButtonModel(caseT, controller);
                    buttonGroup.add(tmp);
                    panel.add(tmp);
                }
            }
        }

    }

}
