package me.jezza.fgpm.core.gradle.components;

import me.jezza.fgpm.core.gradle.lib.IGradleComponent;

import java.util.List;

public class FunctionComponent implements IGradleComponent {

    protected List<IGradleComponent> components;
    protected String name;

    public FunctionComponent(String name, List<IGradleComponent> components) {
        this.name = name;
        this.components = components;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(name).append("(");
        if (!components.isEmpty())
            for (IGradleComponent component : components)
                stringBuilder.append(component.toString());
        return stringBuilder.append(')').toString();
    }
}
