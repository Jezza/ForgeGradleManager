package me.jezza.fgpm.core.gradle.components;

import me.jezza.fgpm.core.gradle.lib.IGradleComponent;

public class StatementComponent implements IGradleComponent {

    private String type;
    private String value;

    public StatementComponent(String type, String value) {
        this.type = type; // Apply
        this.value = value;
    }

    @Override
    public String toString() {
        return type + ": " + value;
    }
}
