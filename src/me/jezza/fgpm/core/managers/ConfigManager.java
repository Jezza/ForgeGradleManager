package me.jezza.fgpm.core.managers;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.jezza.fgpm.ModManager;
import me.jezza.fgpm.core.ModState;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class ConfigManager {

    private static final String modSaveFile = "FGMM.cfg";
    private static LinkedList<ModState> modList = new LinkedList<ModState>();

    public static LinkedList<ModState> getModList() {
        return modList;
    }

    public static boolean removeModState(ModState modState) {
        return modList.remove(modState);
    }

    public static boolean addModState(ModState modState) {
        if (modList.contains(modState))
            modList.remove(modState);
        return modList.add(modState);
    }

    private static File getConfig(String fileName) throws IOException {
        File saveFile = new File(fileName);
        if (!saveFile.exists())
            saveFile.createNewFile();
        return saveFile;
    }

    public static void save() {
        ModManager.getFGManager().updateModVersion();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        modList.remove(null);

        ModState firstState = ModManager.getFGManager().getSelectedModState();

        ArrayList<ModState> saveList = Lists.newArrayList();
        ArrayList<ModState> modListFromHash = Lists.newArrayList(modList.toArray(new ModState[0]));

        if (firstState != null)
            saveList.add(firstState);

        for (int i = 0; i < modListFromHash.size(); i++) {
            ModState tempState = modListFromHash.get(i);
            if (!tempState.equals(firstState))
                saveList.add(tempState);
        }

        try {
            File registryFile = getConfig(modSaveFile);
            ModState[] tempArray = saveList.toArray(new ModState[0]);

            FileWriter writer = new FileWriter(registryFile);
            gson.toJson(tempArray, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Gson gson = new Gson();

        try {
            File registryFile = getConfig(modSaveFile);

            final FileReader reader = new FileReader(registryFile);
            ModState[] tempArray = gson.fromJson(reader, ModState[].class);
            reader.close();

            modList.addAll(Arrays.asList(tempArray));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
