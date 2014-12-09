package me.jezza.fgpm.gui.components;

import javax.swing.*;

public class LocationLabel extends JLabel {

    public LocationLabel(String text) {
        super(text);
        setToolTipText(text);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setToolTipText(text);
    }
}
