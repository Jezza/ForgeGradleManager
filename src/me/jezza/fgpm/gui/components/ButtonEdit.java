package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.gui.GuiAddMenu;
import me.jezza.fgpm.gui.lib.IModListener;
import me.jezza.fgpm.gui.lib.IModProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEdit extends JButton implements IModListener, ActionListener {

    private IModProvider provider;

    public ButtonEdit(String text, IModProvider provider) {
        super(text);
        this.provider = provider;
        addActionListener(this);
        setToolTipText("Edit the currently selected mod.");
    }

    @Override
    public void update(boolean flag) {
        setEnabled(flag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiAddMenu.createAddMenu(provider.getModState());
    }
}
