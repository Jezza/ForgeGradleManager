package me.jezza.fgpm.core.gradle.writer;

import me.jezza.fgpm.core.gradle.lib.IGradleComponent;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class GradleWriter implements Closeable, Flushable {

    private final Writer out;
    private final boolean prettyPrinting;

    private Deque<IGradleComponent> components;

    public GradleWriter(Writer out, boolean prettyPrinting) {
        if (out == null)
            throw new NullPointerException("Writer is null");
        this.out = out;
        this.prettyPrinting = prettyPrinting;
        components = new ArrayDeque<>();
    }

    public void addComponent(IGradleComponent component) {
        components.offerLast(component);
    }

    public void addComponents(IGradleComponent... components) {
        for (IGradleComponent component : components)
            this.components.offerLast(component);
    }

    public String write(File file) {
        return "";
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
