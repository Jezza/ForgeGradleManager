package me.jezza.fgpm.core.managers;

import me.jezza.fgpm.App;
import me.jezza.fgpm.core.Utils;

import java.io.IOException;

public class CommandManager {

    public static void parseGradleCommand(String command) {
        runCommand("gradlew " + command);
    }

    public static void runCommand(String command) {
        System.out.println("Command: " + command);
        if (Utils.isDebugMode())
            return;
        try {
            Runtime.getRuntime().exec("cmd /c start " + command);
        } catch (IOException e) {
            App.LOG.error("Failed to execute command: " + command);
        }
    }

    public static enum GradleCommand {
        build,
        setupDecompWorkspace,
        eclipse,
        idea;

        public void executeCommand() {
            parseGradleCommand(name());
        }
    }
}