package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.gui.lib.IModListener;

import javax.swing.*;

public class ButtonBuild extends JButton implements IModListener {

    public ButtonBuild(String text) {
        super(text);
    }

    @Override
    public void update(boolean flag) {
        setEnabled(flag);
    }
}
