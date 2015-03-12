package me.jezza.fgpm.gui.components;

import me.jezza.fgpm.App;
import me.jezza.fgpm.gui.lib.IModList;
import me.jezza.fgpm.gui.lib.IModProvider;
import me.jezza.fgpm.mod.ModState;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ComboModList extends JComboBox<ModState> implements ItemListener, IModList {

    private IModProvider provider;

    public ComboModList(IModProvider provider) {
        this.provider = provider;
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        App.getFGManager().triggerUpdate();
    }

    @Override
    public void addModState(ModState modState) {
        if (modState == null)
            return;
        removeItem(modState);
        addItem(modState);
        dataModel.setSelectedItem(modState);
    }

    @Override
    public void removeModState(ModState modState) {
        removeItem(modState);
        forceUpdate();
    }

    @Override
    public void forceUpdate() {
        setEnabled(!isEmpty());
    }

    public boolean hasSelectedMod() {
        return dataModel.getSelectedItem() != null;
    }

    public ModState getSelectedMod() {
        return (ModState) dataModel.getSelectedItem();
    }

    public boolean isEmpty() {
        return dataModel.getSize() <= 0;
    }
}
