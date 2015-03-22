package me.jezza.fgpm.core.gradle.components;

import me.jezza.fgpm.core.gradle.lib.IGradleComponent;

import java.util.List;

public class FieldComponent implements IGradleComponent {

    private final String name, value;
    private List<String> parts;

    public FieldComponent(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public FieldComponent(String name, String value, List<String> parts) {
        this.name = name;
        this.value = value;
        this.parts = parts;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(name);
        stringBuilder.append(" = ");
        stringBuilder.append(value);

        if (!parts.isEmpty())
            for (String part : parts)
                stringBuilder.append(" + ").append(part);

        return stringBuilder.toString();
    }
}
