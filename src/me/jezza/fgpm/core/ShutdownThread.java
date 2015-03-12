package me.jezza.fgpm.core;

import me.jezza.fgpm.App;
import me.jezza.fgpm.config.ConfigHandler;

public class ShutdownThread extends Thread {

    public ShutdownThread() {
        super("Shutdown-Thread");
    }

    @Override
    public void run() {
        App.LOG.info("-- Shutdown-Thread --");
        ConfigHandler.save();
    }

    public static void add() {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }

}
