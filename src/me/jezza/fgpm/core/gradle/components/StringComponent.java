package me.jezza.fgpm.core.gradle.components;

import me.jezza.fgpm.core.gradle.lib.IGradleComponent;

public class StringComponent implements IGradleComponent {

    private final String name;

    public StringComponent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
