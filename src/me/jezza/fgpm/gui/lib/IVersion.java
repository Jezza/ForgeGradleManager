package me.jezza.fgpm.gui.lib;

import me.jezza.fgpm.mod.Version;

public interface IVersion {

    public boolean canAlter();

    public void increment();

    public void decrement();

    public String toString();

    public int compareTo(Version version);
}
