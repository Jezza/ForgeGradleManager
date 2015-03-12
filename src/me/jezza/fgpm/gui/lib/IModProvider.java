package me.jezza.fgpm.gui.lib;

import me.jezza.fgpm.mod.ModState;

public interface IModProvider {

    public boolean hasModState();

    public ModState getModState();

    public void triggerUpdate();
}
