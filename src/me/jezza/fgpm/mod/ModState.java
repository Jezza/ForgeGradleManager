package me.jezza.fgpm.mod;

import me.jezza.fgpm.gui.lib.IVersion;

public class ModState {

    public IVersion version;
    public String modID, commonFolder, resourceFolder, groupName;

    public ModState() {
    }

    public ModState(String modID, IVersion version, String commonFolder, String resourceFolder, String groupName) {
        this.modID = modID;
        this.commonFolder = commonFolder;
        this.resourceFolder = resourceFolder;
        this.version = version;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj instanceof ModState && modID.equals(((ModState) obj).modID);
    }

    @Override
    public int hashCode() {
        return modID.hashCode();
    }

    @Override
    public String toString() {
        return modID;
    }
}
