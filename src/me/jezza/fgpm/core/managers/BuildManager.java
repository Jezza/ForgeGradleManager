package me.jezza.fgpm.core.managers;

import me.jezza.fgpm.ModManager;
import me.jezza.fgpm.core.ModState;
import me.jezza.fgpm.core.managers.gradle.FieldSets;
import me.jezza.fgpm.core.managers.gradle.SourceSets;

import java.io.*;
import java.util.ArrayList;

public class BuildManager {

    private static boolean autoIncrement = false;

    private static final BuildManager INSTANCE = new BuildManager();
    private static final String buildFile = "build.gradle";

    private ArrayList<String> file = new ArrayList<String>();

    private SourceSets sourceSets = new SourceSets();
    private FieldSets fieldSets = new FieldSets();

    public static BuildManager getInstance() {
        return INSTANCE;
    }

    public void toggleAutoIncrement() {
        autoIncrement = !autoIncrement;
    }

    public void buildWithModState(ModState modState) {
        if (modState == null)
            return;

        fieldSets.setFields(modState);
        sourceSets.setDirectories(modState);

        fieldSets.writeIntoFile(file);
        sourceSets.writeIntoFile(file);

        writeFile();

        CommandManager.GradleCommand.build.executeCommand();
        if (autoIncrement)
            ModManager.getFGManager().incrementVersion();
    }

    private void writeFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(buildFile));
            for (String line : file)
                writer.write(line + (line.endsWith(System.lineSeparator()) ? "" : System.lineSeparator()));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(buildFile));
            String line = null;
            while ((line = reader.readLine()) != null)
                file.add(line);
            reader.close();

            fieldSets = FieldSets.createFromFile(file);
            sourceSets = SourceSets.createFromFile(file);

        } catch (IOException e) {
        }
    }

}
