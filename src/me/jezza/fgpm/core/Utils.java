package me.jezza.fgpm.core;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Utils {

    private static boolean debugMode = false;

    public static void checkDebugMode() {
        debugMode = "true".equalsIgnoreCase(System.getProperty("jezza.debug"));
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static boolean canContinue() {
        return !(isDebugMode() || new File("gradlew").exists());
    }

    public static String compileList(List<String> file) {
        StringBuilder sb = new StringBuilder();
        for (String line : file)
            sb.append(line);
        return sb.toString();
    }

    public static String chooseLocation() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getAbsolutePath();
        return "";
    }

    public static void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
    }
}
