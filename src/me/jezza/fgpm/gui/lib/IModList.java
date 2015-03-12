package me.jezza.fgpm.gui.lib;

import me.jezza.fgpm.mod.ModState;

public interface IModList {

    public void addModState(ModState modState);

    public void removeModState(ModState modState);

    public void forceUpdate();
}
