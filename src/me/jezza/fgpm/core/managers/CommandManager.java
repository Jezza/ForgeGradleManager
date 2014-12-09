package me.jezza.fgpm.core.managers;

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
        }
    }

    public static enum GradleCommand {
        // Apologise for this not following java convention as a constant.
        // This is done for the user, as I use the name of the field for executing the command.
        // This means it shows up in the console, so, if they want to see what command they've ran.
        // It will show it with java convention for the name of the field, not constant.
        build,
        clean,
        setupDecompWorkspace,

        eclipse,
        idea;
        // @formatter:on

        public void executeCommand() {
            parseGradleCommand(name());
        }
    }
}