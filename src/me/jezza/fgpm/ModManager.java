package me.jezza.fgpm;

import me.jezza.fgpm.core.Utils;
import me.jezza.fgpm.core.managers.BuildManager;
import me.jezza.fgpm.core.managers.ConfigManager;
import me.jezza.fgpm.gui.FGManager;
import me.jezza.fgpm.gui.GuiAddMenu;

import javax.swing.*;
import java.awt.*;

public class ModManager {

    public static GuiAddMenu guiAddMenu;

    private static final String FGM_VERSION = "v0.0.03";
    private static FGManager modManager;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ConfigManager.save();
            }
        });

        Utils.checkDebugMode();

        if (Utils.canContinue()) {
            Utils.showErrorMessage("Could not find gradlew.bat." + System.lineSeparator() + "Please place me in the correct folder.");
            System.exit(0);
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BuildManager.getInstance().readFile();
                    ConfigManager.load();
                    modManager = new FGManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String getVersion() {
        return FGM_VERSION;
    }

    public static FGManager getFGManager() {
        return modManager;
    }

}
