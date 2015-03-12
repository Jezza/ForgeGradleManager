package me.jezza.fgpm.core.gradle.lib;

import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

public interface IComponentBuilder {

    public void addComponent(IComponentBuilder builder);

    public void consume(GradleNavigator navigator);

    public IGradleComponent build();

}
