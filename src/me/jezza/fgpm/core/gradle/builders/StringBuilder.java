package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.gradle.components.StringComponent;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

public class StringBuilder implements IComponentBuilder {

    public StringBuilder() {
    }

    @Override
    public IGradleComponent consume(GradleNavigator navigator) {
        return new StringComponent(navigator.asString());
    }
}
