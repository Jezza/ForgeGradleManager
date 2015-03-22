package me.jezza.fgpm;

import me.jezza.fgpm.config.IJsonObject;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleReader;
import me.jezza.fgpm.gui.FGManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

public class App {

    public static final Collection<IJsonObject> HANDLERS = new ArrayList<>();

    public static final Logger LOG = LogManager.getLogger(App.class.getName());

    private static final String FGM_VERSION = "0.0.04";

    private static FGManager modManager;

    public static void main(String[] args) {
//        ShutdownThread.add();
//        Utils.checkDebugMode();
//        setLookAndFeel();

//        modManager = new FGManager();

//        if (Utils.canContinue()) {
//            Utils.showErrorMessage("Could not find gradlew.bat." + System.lineSeparator() + "Please place me in the correct folder.");
//            System.exit(0);
//        }
//        Thread.currentThread().setName("MainThread");

//        try {
//            ConfigHandler.load();
//        } catch (Exception e) {
//            LOG.error("Failed to load config.", e);
//            System.exit(-1);
//        }

//        modManager.triggerUpdate();

//        try {
//            BuildManager.getInstance().readFile();
//        } catch (Exception e) {
//            LOG.error("Failed to read gradle file.", e);
//            System.exit(-1);
//        }

//        SwingUtilities.invokeLater(modManager);
        IGradleComponent[] result = null;
        try {
            result = new GradleReader(new File(".", "build.gradle")).read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (result == null || result.length == 0) {
            App.LOG.info("No components discovered!");
            return;
        }

        App.LOG.info("Components parsed: " + result.length);

        for (IGradleComponent component : result)
            App.LOG.info(component);
    }

    public static String getVersion() {
        return FGM_VERSION;
    }

    public static FGManager getFGManager() {
        return modManager;
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.error("Failed to set Look-And-Feel.", e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
