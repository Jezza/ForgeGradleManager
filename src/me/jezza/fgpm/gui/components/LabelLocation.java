package me.jezza.fgpm.gui.components;

import javax.swing.*;

public class LabelLocation extends JLabel {

    public LabelLocation(String text) {
        super(text);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setToolTipText(text);
    }
}
