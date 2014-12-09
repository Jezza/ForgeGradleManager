package me.jezza.fgpm.core;

import me.jezza.fgpm.core.managers.ConfigManager;

public class ModState {

    private String modID, commonFolder, resourceFolder, version, groupName;

    public ModState(String modID, String commonFolder, String resourceFolder, String version, String groupName) {
        this.modID = modID;
        this.commonFolder = commonFolder;
        this.resourceFolder = resourceFolder;
        this.version = version;
        this.groupName = groupName;
    }

    public String getModID() {
        return modID;
    }

    public String getCommonFolder() {
        return commonFolder;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }

    public String getVersion() {
        return version;
    }

    public String getGroupName() {
        return groupName;
    }

    public void updateVersion(String version) {
        this.version = version;
    }

    public void addState() {
        ConfigManager.addModState(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ModState))
            return false;
        return modID.equals(((ModState) obj).getModID());
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
